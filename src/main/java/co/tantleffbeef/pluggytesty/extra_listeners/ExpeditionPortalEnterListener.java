package co.tantleffbeef.pluggytesty.extra_listeners;

import co.tantleffbeef.pluggytesty.expeditions.ExpeditionBuilder;
import co.tantleffbeef.pluggytesty.expeditions.ExpeditionController;
import co.tantleffbeef.pluggytesty.expeditions.loading.ExpeditionInformation;
import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import co.tantleffbeef.pluggytesty.inventoryGUI.InventoryButton;
import co.tantleffbeef.pluggytesty.inventoryGUI.InventoryGUI;
import co.tantleffbeef.pluggytesty.misc.Debug;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

import org.bukkit.Material;

import java.util.Map;
import java.util.function.Consumer;

public class ExpeditionPortalEnterListener implements Listener {
    private final ExpeditionBuilder expeditionBuilder;
    private final ExpeditionController expeditionController;
    private final Map<String, ExpeditionInformation> expeditionTypes;
    private final Plugin plugin;

    private InventoryGUI expeditionEnterGUI;
    private InventoryGUI expeditionConfirmGUI;

    public ExpeditionPortalEnterListener(Plugin plugin, ExpeditionBuilder builder, ExpeditionController expController, Map<String, ExpeditionInformation> expeditionTypes) {
        this.expeditionBuilder = builder;
        this.expeditionController = expController;
        this.expeditionTypes = expeditionTypes;
        this.plugin = plugin;

        expeditionEnterGUI = new InventoryGUI(6 * 9, "Expeditions", Material.GRAY_STAINED_GLASS_PANE, plugin.getServer());
        expeditionConfirmGUI = new InventoryGUI(1 * 9, "Continue?",  Material.GRAY_STAINED_GLASS_PANE, plugin.getServer());

        expeditionConfirmGUI.addButton(new InventoryButton(this::onConfirm, Material.GREEN_STAINED_GLASS, "Confirm", "Enter Expedition"), 4);
        expeditionConfirmGUI.addButton(new InventoryButton(this::onDecline, Material.RED_STAINED_GLASS, "Cancel", "Return to Expedition Selector"), 6);

        // column major list of names of the buttons
        final String[] names =
                {"Tier 1",                     "Tier 2",                  "Tier 3",                   "Tier 4",              "Tier 5",             "Tier 6",
                "Zombie Expedition",           "Earth Expedition",        "North Outpost Expedition", "Lava Expedition",     "Darkness Expedition",
                "Skeleton Expedition",         "Air Expedition",          "South Outpost Expedition", "Scorched Expedition", "Wasteland Expedition",
                "Zombified Piglin Expedition", "Water Expedition",        "East Outpost Expedition",  "Soul Expedition",     "Happy Animals Expedition",
                                               "Ice Expedition",          "West Outpost Expedition",  "Piglin Expediton",
                "Undead Trial",                "Weather Trial",           "Mansion Trial",            "Magma Trial",         "Decay Trial",        "The Final Trial"};
        final String[] lores = {"Undead", "Elemental", "Illager", "Nether", "Void", "Ending"};
        final Material[] icons =
                {Material.RED_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE,
                // stuff
                Material.COARSE_DIRT, Material.DEEPSLATE, Material.DARK_OAK_PLANKS, Material.MAGMA_BLOCK, Material.END_STONE,
                Material.BONE_BLOCK, Material.PURPUR_BLOCK, Material.DARK_OAK_LOG, Material.SAND, Material.SCULK,
                Material.NETHERRACK, Material.PRISMARINE, Material.EMERALD_ORE, Material.SOUL_SAND, Material.LIGHT_BLUE_WOOL,
                                     Material.ICE, Material.EMERALD_BLOCK, Material.GILDED_BLACKSTONE,
                Material.ROTTEN_FLESH, Material.LIGHTNING_ROD, Material.TOTEM_OF_UNDYING, Material.LAVA, Material.WITHER_ROSE, Material.DRAGON_EGG};

        int goofy = 0;

        for (int i = 0; i < 6*9; i++) {
            if((i > 5 && i < 12) || (i != 53 && i % 6 == 5) || (i > 35 && i < 48)) { // i love hard coding the excluded slots
                goofy++;
                continue;
            }

            Consumer<InventoryClickEvent> eventConsumer = this::onExpeditionClick;
            if (i < 6)
                eventConsumer = event -> {};

            String name = i - goofy >= names.length ? " " : names[i - goofy];
            String lore = i - goofy >= lores.length ? " " : lores[i - goofy];
            Material icon = i - goofy >= icons.length ? Material.AIR : icons[i - goofy];

            expeditionEnterGUI.addButton(
                    new InventoryButton(
                            eventConsumer,
                            icon,
                            name,
                            lore
                    ), i
            );
        }

    }

    private void onExpeditionClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player))
            return;

        expeditionConfirmGUI.displayTo(player);
    }

    private void onConfirm(InventoryClickEvent event) {
//        expeditionBuilder.buildExpedition(expeditionTypes.get("air"))
//                .whenComplete((r, e) -> {
//                    if (e != null)
//                        Debug.alwaysError(e.toString());
//                })
//                .thenAccept(exp -> plugin.getServer().getScheduler().runTask(plugin, () -> {
//                    exp.start(GooberStateController);
//                }));
    }

    private void onDecline(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player))
            return;

        player.closeInventory();

        expeditionEnterGUI.displayTo(player);
    }

    @EventHandler
    public void onPortalEnter(EntityPortalEnterEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        expeditionEnterGUI.displayTo(player);

    }

//    private void displayAndDecorate(Player player) {
//        expeditionEnterGUI.displayTo(player);
//    }

}
