package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
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
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArrowBeltItemType extends SimpleItemType implements InteractableItemType {

    private final Map<UUID, InventoryGUI> playerBelts;
    private final Map<UUID, Integer> playerLastShotPos;

    private final InventoryGUI DEFAULT_BELT;

    public ArrowBeltItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.PAPER);

        namespace.getServer().getPluginManager().registerEvents(new BeltShootListener(), namespace);

        playerBelts = new HashMap<>();
        playerLastShotPos = new HashMap<>();

        DEFAULT_BELT = new InventoryGUI(9, "ArrowBelt", namespace.getServer());

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

                            if (event.getWhoClicked() instanceof Player player)
                                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 2);
                        }

                    },
                    Material.GRAY_STAINED_GLASS_PANE,
                    "No arrow assigned",
                    "Click to change"
            );

            DEFAULT_BELT.addButton(selectorButton, i);
        }
    }

    private class BeltShootListener implements Listener {

        @EventHandler
        public void onShoot(EntityShootBowEvent event) {
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
            ItemStack arrowItem = getNextArrow(playerUUID, event.getConsumable(), inventory);

            event.setConsumeItem(false);
            ItemStack item = inventory.getItem(inventory.first(arrowItem.getType()));

            Bukkit.broadcastMessage("arrow ItemStack: " + arrowItem);
            Bukkit.broadcastMessage("Inventory ItemStack" + item);

            assert item != null;
            item.setAmount(item.getAmount() - 1);

            Entity arrow = event.getProjectile();
            event.setProjectile(player.getWorld().spawnArrow(arrow.getLocation(), arrow.getVelocity().normalize(),
                    (float) arrow.getVelocity().length(), 0, getArrowEntity(arrowItem)));

            Bukkit.broadcastMessage("new projectile: " + event.getProjectile());
        }

        private <T extends AbstractArrow> Class<T> getArrowEntity(ItemStack arrowItem) {

            return switch (arrowItem.getType()) {
                case SPECTRAL_ARROW -> (Class<T>) SpectralArrow.class;
                default -> (Class<T>) Arrow.class;
            };
        }

        private ItemStack getNextArrow(UUID player, ItemStack originalArrow, Inventory inventory) {

            for (int i = 0; i < 5; i++) {
                int index = playerLastShotPos.get(player);

                ItemStack arrow = getArrowOrNull(index, player);
                Bukkit.broadcastMessage(ChatColor.GREEN + "next Arrow: " + arrow + "\nindex: " + index);

                if (inventory.containsAtLeast(arrow, 1))
                    return arrow;

                playerLastShotPos.put(player, (index + 1) % 5);
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

        playerBelts.putIfAbsent(uuid, DEFAULT_BELT);

        playerBelts.get(uuid).displayTo(player);

        return false;
    }
}
