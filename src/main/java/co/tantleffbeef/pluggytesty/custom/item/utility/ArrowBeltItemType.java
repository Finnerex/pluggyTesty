package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import co.tantleffbeef.pluggytesty.custom.item.weapons.arrows.CustomArrow;
import co.tantleffbeef.pluggytesty.inventoryGUI.InventoryButton;
import co.tantleffbeef.pluggytesty.inventoryGUI.InventoryGUI;
import co.tantleffbeef.pluggytesty.inventoryGUI.InventorySelectorButton;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ArrowBeltItemType extends SimpleItemType implements InteractableItemType {

    private final Map<UUID, InventoryGUI> playerBelts;
    private final Map<UUID, Integer> playerLastShotPos;
    private final Collection<EntityShootBowEvent> eventsToIgnore;

    KeyManager<CustomNbtKey> keyManager;
    ResourceManager resourceManager;
    private final Plugin plugin;

    public ArrowBeltItemType(Plugin namespace, String id, boolean customModel, String name, KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager) {
        super(namespace, id, customModel, name, Material.PAPER);

        namespace.getServer().getPluginManager().registerEvents(new BeltShootListener(), namespace);

        this.keyManager = keyManager;
        this.resourceManager = resourceManager;

        this.plugin = namespace;
        playerBelts = new HashMap<>();
        playerLastShotPos = new HashMap<>();
        eventsToIgnore = new ArrayList<>();
    }

    private class BeltShootListener implements Listener {

        @EventHandler
        public void onShoot(EntityShootBowEvent event) {
            if (eventsToIgnore.contains(event)) {
                eventsToIgnore.remove(event);
                return;
            }

            ItemStack bow = event.getBow();

            Bukkit.broadcastMessage("event hapopenent");

            // not dealing with crossbows here
            if (bow == null || bow.getType() == Material.CROSSBOW || event.getConsumable() == null)
                return;

            if (!(event.getEntity() instanceof Player player))
                return;

            UUID playerUUID = player.getUniqueId();

            if (playerBelts.get(playerUUID) == null)
                return;

            playerLastShotPos.putIfAbsent(playerUUID, 0);

            Inventory inventory = player.getInventory();

            ItemStack arrowItem = getNextArrow(playerUUID, event.getConsumable(), player.getInventory());

            ItemStack item = null;
            for (ItemStack itemStack : inventory.getContents()) {
                Bukkit.broadcastMessage(ChatColor.AQUA + "Item Stack: " + itemStack);
                if (arrowItem.isSimilar(itemStack)) {
                    item = itemStack;
                    break;
                }
            }

            assert item != null;

            event.setCancelled(true);

            EntityShootBowEvent newEvent = new EntityShootBowEvent(player, bow, item, getArrowEntity(arrowItem, event),
                    event.getHand(), event.getForce(), event.shouldConsumeItem());
            eventsToIgnore.add(newEvent);
        }

        private <T extends AbstractArrow> AbstractArrow getArrowEntity(ItemStack arrowItem, EntityShootBowEvent event) {

            Class<T> theClass = switch (arrowItem.getType()) {
                case SPECTRAL_ARROW -> (Class<T>) SpectralArrow.class;
                default -> (Class<T>) Arrow.class;
            };

            assert event.getProjectile() instanceof AbstractArrow;
            AbstractArrow oldArrow = (AbstractArrow) event.getProjectile();

            AbstractArrow newArrow = event.getEntity().getWorld().spawnArrow(oldArrow.getLocation(), oldArrow.getVelocity().normalize(),
                    (float) oldArrow.getVelocity().length(), 0, theClass);

            // Make it the same arrow (this is goofy)
            newArrow.setPickupStatus(oldArrow.getPickupStatus());
            newArrow.setCritical(oldArrow.isCritical());
            newArrow.setShooter(oldArrow.getShooter());
//            newArrow.setDamage(oldArrow.getDamage());
//            newArrow.setPierceLevel(oldArrow.getPierceLevel());
            newArrow.setShotFromCrossbow(oldArrow.isShotFromCrossbow());


            if (newArrow instanceof Arrow potionableArrow && arrowItem.getItemMeta() instanceof PotionMeta potionMeta) {
                potionableArrow.setBasePotionData(potionMeta.getBasePotionData());
                potionableArrow.setColor(potionMeta.getColor());

                for (PotionEffect effect : potionMeta.getCustomEffects()) {
                    potionableArrow.addCustomEffect(effect, true);
                }
                Bukkit.broadcastMessage(ChatColor.GOLD + "potion effect applied");
            }

            return newArrow;

        }

        private ItemStack getNextArrow(UUID player, ItemStack originalArrow, Inventory inventory) {

            for (int i = 0; i < 5; i++) {
                int index = playerLastShotPos.get(player);

                ItemStack arrow = getArrowOrNull(index, player);
                Bukkit.broadcastMessage(ChatColor.GREEN + "next Arrow: " + arrow + "\nindex: " + index);

                playerLastShotPos.put(player, (index + 1) % 5);

                if (inventory.containsAtLeast(arrow, 1))
                    return arrow;
            }

            return originalArrow;
        }

        private ItemStack getArrowOrNull(int position, UUID player) {

            ItemStack itemStack = playerBelts.get(player).getButton(position + 2).getIcon();
            Material type = itemStack.getType();

            if (type == Material.SPECTRAL_ARROW || type == Material.ARROW || type == Material.TIPPED_ARROW)
                return itemStack;

            return null;
        }
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        UUID uuid = player.getUniqueId();

        if (playerBelts.get(uuid) == null) {

            InventoryGUI DEFAULT_BELT = new InventoryGUI(9, "ArrowBelt", plugin.getServer());

            for (int i = 2; i < 7; i++) {
                int buttonSlot = i;
                InventoryButton selectorButton = new InventorySelectorButton(
                        (event) -> {
                            ItemStack item = event.getCurrentItem();
                            event.setCancelled(true);

                            if (item == null)
                                return;

                            item = item.clone();

                            if (item.getType() == Material.ARROW || item.getType() == Material.SPECTRAL_ARROW || item.getType() == Material.TIPPED_ARROW) {
                                item.setAmount(1);
                                playerBelts.get(event.getWhoClicked().getUniqueId()).setIcon(buttonSlot, item);

                                if (event.getWhoClicked() instanceof Player p)
                                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 2);
                            }

                        },
                        Material.GRAY_STAINED_GLASS_PANE,
                        "No arrow assigned",
                        "Click to change"
                );

                DEFAULT_BELT.addButton(selectorButton, i);
            }

            playerBelts.put(uuid, DEFAULT_BELT);
        }


        playerBelts.get(uuid).displayTo(player);

        return false;
    }
}
