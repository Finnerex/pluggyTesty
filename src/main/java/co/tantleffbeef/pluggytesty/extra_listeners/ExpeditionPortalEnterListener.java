package co.tantleffbeef.pluggytesty.extra_listeners;

import co.tantleffbeef.pluggytesty.expeditions.ExpeditionBuilder;
import co.tantleffbeef.pluggytesty.expeditions.ExpeditionController;
import co.tantleffbeef.pluggytesty.expeditions.loading.ExpeditionInformation;
import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import co.tantleffbeef.pluggytesty.inventoryGUI.InventoryButton;
import co.tantleffbeef.pluggytesty.inventoryGUI.InventoryGUI;
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

    private InventoryGUI expeditionEnterGUI;
    private InventoryGUI expeditionConfirmGUI;
    private ExpeditionBuilder expeditionBuilder;
    private ExpeditionController expeditionController;

    public ExpeditionPortalEnterListener(Plugin plugin) {
        expeditionEnterGUI = new InventoryGUI(5 * 9, "Expeditions", Material.GRAY_STAINED_GLASS_PANE, plugin.getServer());
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
//        final Material[] icons = {Material.GREEN_STAINED_GLASS_PANE, Material.GRA}

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
