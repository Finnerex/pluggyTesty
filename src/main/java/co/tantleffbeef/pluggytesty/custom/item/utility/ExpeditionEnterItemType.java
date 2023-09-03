package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import co.tantleffbeef.pluggytesty.expeditions.ExpeditionBuilder;
import co.tantleffbeef.pluggytesty.expeditions.ExpeditionController;
import co.tantleffbeef.pluggytesty.expeditions.loading.ExpeditionInformation;
import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import co.tantleffbeef.pluggytesty.inventoryGUI.InventoryButton;
import co.tantleffbeef.pluggytesty.inventoryGUI.InventoryGUI;
import co.tantleffbeef.pluggytesty.misc.Debug;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ExpeditionEnterItemType extends SimpleItemType implements InteractableItemType {
    private final ExpeditionBuilder expeditionBuilder;
    private final ExpeditionController expeditionController;
    private final Map<String, ExpeditionInformation> expeditionTypes;

    private final Map<UUID, String> chosenExpeditions;
    private final Plugin plugin;
    private final GooberStateController gooberStateController;

    private InventoryGUI expeditionEnterGUI;
    private InventoryGUI expeditionConfirmGUI;

    public ExpeditionEnterItemType(Plugin plugin, String id, boolean customModel, String name, ExpeditionBuilder builder,
                                   ExpeditionController expController, Map<String, ExpeditionInformation> expeditionTypes, GooberStateController gooberStateController) {
        super(plugin, id, customModel, name, Material.END_ROD);
        this.expeditionBuilder = builder;
        this.expeditionController = expController;
        this.expeditionTypes = expeditionTypes;
        this.gooberStateController = gooberStateController;
        this.plugin = plugin;

        this.chosenExpeditions = new HashMap<>();

        expeditionEnterGUI = new InventoryGUI(6 * 9, "Expeditions", Material.GRAY_STAINED_GLASS_PANE, plugin.getServer());
        expeditionConfirmGUI = new InventoryGUI(1 * 9, "Continue?",  Material.GRAY_STAINED_GLASS_PANE, plugin.getServer());

        expeditionConfirmGUI.addButton(new InventoryButton(this::onConfirm, Material.GREEN_STAINED_GLASS, "Confirm", "Enter Expedition"), 3);
        expeditionConfirmGUI.addButton(new InventoryButton(this::onDecline, Material.RED_STAINED_GLASS, "Cancel", "Return to Expedition Selector"), 5);

        expeditionEnterGUI
                .addButton(new InventoryButton(
                        event -> {}, Material.RED_STAINED_GLASS_PANE, "Tier 1", "Undead"), 0)
                .addButton(new InventoryButton(
                        event -> {}, Material.RED_STAINED_GLASS_PANE, "Tier 2", "Elemental"), 9)
                .addButton(new InventoryButton(
                        event -> {}, Material.RED_STAINED_GLASS_PANE, "Tier 3", "Illager"), 9 * 2)
                .addButton(new InventoryButton(
                        event -> {}, Material.RED_STAINED_GLASS_PANE, "Tier 4", "Nether"), 9 * 3)
                .addButton(new InventoryButton(
                        event -> {}, Material.RED_STAINED_GLASS_PANE, "Tier 5", "Void"), 9 * 4)
                .addButton(new InventoryButton(
                        event -> {}, Material.RED_STAINED_GLASS_PANE, "Tier 6", "Ending"), 9 * 5)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.COARSE_DIRT, "Zombie Expedition"), 2)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.BONE_BLOCK, "Skeleton Expedition"), 3)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.NETHERRACK, "Zombified Piglin Expedition"), 4)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.ROTTEN_FLESH, "Undead Trial"), 8)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.DEEPSLATE, "Earth Expedition"), 11)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.PURPUR_BLOCK, "Air Expedition"), 12)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.PRISMARINE, "Water Expedition"), 13)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.ICE, "Ice Expedition"), 14)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.LIGHTNING_ROD, "Weather Trial"), 17)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.DARK_OAK_PLANKS, "North Outpost Expedition"), 20)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.DARK_OAK_LOG, "South Outpost Expedition"), 21)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.EMERALD_ORE, "East Outpost Expedition"), 22)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.EMERALD_BLOCK, "West Outpost Expedition"), 23)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.TOTEM_OF_UNDYING, "Mansion Trial"), 26)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.MAGMA_BLOCK, "Lava Expedition"), 29)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.SAND, "Scorched Expedition"), 30)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.SOUL_SAND, "Soul Expedition"), 31)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.GILDED_BLACKSTONE, "Piglin Expedition"), 32)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.LAVA_BUCKET, "Magma Trial"), 35)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.SCULK, "Darkness Expedition"), 38)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.END_STONE, "Wasteland Expedition"), 39)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.LIGHT_BLUE_WOOL, "Happy Animals Expedition"), 40)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.WITHER_ROSE, "Decay Trial"), 44)
                .addButton(new InventoryButton(
                        this::onExpeditionClick, Material.DRAGON_EGG, "The Final Trial"), 53);


    }

    private void onExpeditionClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player))
            return;

        String id = event.getCurrentItem().getItemMeta().getDisplayName().toLowerCase()
                .replaceAll(" ", "_").replace("_expedition", "");

        chosenExpeditions.put(player.getUniqueId(), id);
        Bukkit.broadcastMessage("clicked on: " + chosenExpeditions.get(player.getUniqueId()));

        player.closeInventory();

        expeditionConfirmGUI.displayTo(player);
    }

    private void onConfirm(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player))
            return;

        player.closeInventory();
        Bukkit.broadcastMessage("starting: " + chosenExpeditions.get(player.getUniqueId()));

        expeditionBuilder.buildExpedition(expeditionTypes.get(chosenExpeditions.get(player.getUniqueId())))
                .whenComplete((r, e) -> {
                    if (e != null)
                        Debug.alwaysError(e.toString());
                })
                .thenAccept(exp -> plugin.getServer().getScheduler().runTask(plugin, () -> {
                    exp.start(gooberStateController.wrapPlayer(player).getPartyOrCreate());
                }));

        chosenExpeditions.remove(player.getUniqueId());
    }

    private void onDecline(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player))
            return;

        chosenExpeditions.remove(player.getUniqueId());
        player.closeInventory();

        expeditionEnterGUI.displayTo(player);
    }


    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {

        expeditionEnterGUI.displayTo(player);

        return true;
    }

//    private void displayAndDecorate(Player player) {
//        expeditionEnterGUI.displayTo(player);
//    }

}
