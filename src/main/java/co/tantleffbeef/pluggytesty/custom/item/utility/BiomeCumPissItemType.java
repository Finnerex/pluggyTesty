package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import co.tantleffbeef.pluggytesty.inventoryGUI.InventoryButton;
import co.tantleffbeef.pluggytesty.inventoryGUI.InventoryGUI;
import co.tantleffbeef.pluggytesty.inventoryGUI.UnlockableButton;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

public class BiomeCumPissItemType extends SimpleItemType implements InteractableItemType {

    private Map<UUID, InventoryGUI> itemGUIs;
    private final InventoryGUI initialGUI;
    private final InventoryGUI confirmRecordGUI;
    private final InventoryGUI DEFAULT_GUI;
//    private final Map<Biome, Material> buttons;
    private final Plugin plugin;

    public BiomeCumPissItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.COMPASS);
        plugin = namespace;

        itemGUIs = new HashMap<>();

        Material[] buttonMaterials = {Material.WATER_BUCKET, Material.WATER_BUCKET, Material.WATER_BUCKET, Material.WATER_BUCKET, Material.WATER_BUCKET, Material.MYCELIUM, Material.STONE, Material.STONE, Material.STONE, Material.STONE, Material.CHERRY_LOG, Material.STONE, Material.STONE, Material.STONE, Material.STONE, Material.STONE, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.FERN, Material.FERN, Material.FERN, Material.FERN, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.WATER_BUCKET, Material.WATER_BUCKET, Material.WATER_BUCKET, Material.WATER_BUCKET, Material.WATER_BUCKET, Material.WATER_BUCKET, Material.WATER_BUCKET, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.SNOW, Material.SNOW, Material.SAND, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.TERRACOTTA, Material.TERRACOTTA, Material.TERRACOTTA, Material.DEEPSLATE, Material.DEEPSLATE, Material.DEEPSLATE, Material.NETHERRACK, Material.NETHERRACK, Material.NETHERRACK, Material.NETHERRACK, Material.NETHERRACK};
        Biome[] biomes = {Biome.DEEP_OCEAN, Biome.WARM_OCEAN, Biome.LUKEWARM_OCEAN, Biome.COLD_OCEAN, Biome.FROZEN_OCEAN, Biome.MUSHROOM_FIELDS, Biome.JAGGED_PEAKS, Biome.FROZEN_PEAKS, Biome.STONY_PEAKS, Biome.MEADOW, Biome.CHERRY_GROVE, Biome.GROVE, Biome.SNOWY_SLOPES, Biome.WINDSWEPT_HILLS, Biome.WINDSWEPT_FOREST, Biome.WINDSWEPT_GRAVELLY_HILLS, Biome.FOREST, Biome.FLOWER_FOREST, Biome.TAIGA, Biome.OLD_GROWTH_PINE_TAIGA, Biome.OLD_GROWTH_SPRUCE_TAIGA, Biome.SNOWY_TAIGA, Biome.BIRCH_FOREST, Biome.OLD_GROWTH_BIRCH_FOREST, Biome.DARK_FOREST, Biome.JUNGLE, Biome.SPARSE_JUNGLE, Biome.BAMBOO_JUNGLE, Biome.RIVER, Biome.FROZEN_RIVER, Biome.SWAMP, Biome.MANGROVE_SWAMP, Biome.BEACH, Biome.SNOWY_BEACH, Biome.STONY_SHORE, Biome.PLAINS, Biome.SUNFLOWER_PLAINS, Biome.SNOWY_PLAINS, Biome.ICE_SPIKES, Biome.DESERT, Biome.SAVANNA, Biome.SAVANNA_PLATEAU, Biome.WINDSWEPT_SAVANNA, Biome.BADLANDS, Biome.ERODED_BADLANDS, Biome.WOODED_BADLANDS, Biome.DEEP_DARK, Biome.DRIPSTONE_CAVES, Biome.LUSH_CAVES, Biome.NETHER_WASTES, Biome.SOUL_SAND_VALLEY, Biome.CRIMSON_FOREST, Biome.WARPED_FOREST, Biome.BASALT_DELTAS};
        String[] buttonNames = new String[] {"Deep Ocean", "Warm Ocean", "Lukewarm Ocean", "Cold Ocean", "Frozen Ocean", "Mushroom Fields", "Jagged Peaks", "Frozen Peaks", "Stony Peaks", "Meadow", "Cherry Grove", "Grove", "Snowy Slopes", "Windswept Hills", "Windswept Forest", "Windswept Gravelly Hills", "Forest", "Flower Forest", "Taiga", "Old Growth Pine Taiga", "Old Growth Spruce Taiga", "Snowy Taiga", "Birch Forest", "Old Growth Birch Forest", "Dark Forest", "Jungle", "Sparse Jungle", "Bamboo Jungle", "River", "Frozen River", "Swamp", "Mangrove Swamp", "Beach", "Snowy Beach", "Stony Shore", "Plains", "Sunflower Plains", "Snowy Plains", "Ice Spikes", "Desert", "Savanna", "Savanna Plateau", "Windswept Savanna", "Badlands", "Eroded Badlands", "Wooded Badlands", "Deep Dark", "Dripstone Caves", "Lush Caves", "Nether Wastes", "Soul Sand Valley", "Crimson Forest", "Warped Forest", "Basalt Deltas"};


        initialGUI = new InventoryGUI(9, "Biome Compass", Material.GRAY_STAINED_GLASS_PANE, namespace.getServer());
        confirmRecordGUI = new InventoryGUI(9, "Confirm Record Biome", Material.GRAY_STAINED_GLASS_PANE, namespace.getServer());
        DEFAULT_GUI = new InventoryGUI(6 * 9, "Track Biome", namespace.getServer());

        initialGUI
                .addButton(new InventoryButton(
                        (event) -> {
                            if (!(event.getWhoClicked() instanceof Player player))
                                return;

                            player.closeInventory();
                            confirmRecordGUI.displayTo(player);

                        }, Material.ORANGE_CONCRETE, "Record Current Biome"
                ), 3)
                .addButton(new InventoryButton(
                        (event) -> {
                            if (!(event.getWhoClicked() instanceof Player player))
                                return;

                            player.closeInventory();
                            itemGUIs.get(player.getUniqueId()).displayTo(player);

                        }, Material.GREEN_CONCRETE, "Track Biome"
                ), 5);


        confirmRecordGUI
                .addButton(new InventoryButton(
                        (event) -> {
                            if (!(event.getWhoClicked() instanceof Player player))
                                return;

                            Location l = player.getLocation();
                            Biome biome = player.getWorld().getBiome(l);

                            InventoryGUI gui = itemGUIs.get(player.getUniqueId());
                            int slot = List.of(biomes).indexOf(biome);

                            if (!(gui.getButton(slot) instanceof UnlockableButton button))
                                return;

                            button.unlock();
                            button.setLore(l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ());

                            player.sendMessage("Recorded biome: " + biome);

                        }, Material.GREEN_STAINED_GLASS, "Record Current Biome"
                ), 3)
                .addButton(new InventoryButton(
                        (event) -> {
                            if (!(event.getWhoClicked() instanceof Player player))
                                return;

                            player.closeInventory();
                            initialGUI.displayTo(player);

                        }, Material.RED_STAINED_GLASS, "Cancel"
                ), 5);



        DEFAULT_GUI
                .addUnlockableButtons(
                        (event) -> {
                            if (!(event.getWhoClicked() instanceof Player player))
                                return;

                            ItemStack item = event.getCurrentItem();

                            if (item.getType() == Material.RED_STAINED_GLASS) { // not recorded
                                player.sendMessage(ChatColor.RED + "You have not recorded this biome yet.");
                                return;
                            }

                            ItemStack compass = player.getInventory().getItemInMainHand();
                            //assert CustomItemType.asInstanceOf(this.getClass(), compass, keyManager, resourceManager) != null; yeah but no;
                            assert compass.getItemMeta() instanceof CompassMeta;

                            CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
                            compassMeta.setLodestone(parseLocationString(item.getItemMeta().getLore().get(0), player.getWorld()));
                            compassMeta.setLodestoneTracked(false);

                            compass.setItemMeta(compassMeta);

                            player.sendMessage("Now tracking: " + item.getItemMeta().getDisplayName());
                        },
                        0,
                        buttonMaterials,
                        buttonNames,
                        false,
                        Material.RED_STAINED_GLASS);

    }

    private Location parseLocationString(String locationString, World world) {
        String[] xyz = locationString.split(", ");
        return new Location(world, Integer.parseInt(xyz[0]), Integer.parseInt(xyz[1]), Integer.parseInt(xyz[2]));
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, @Nullable Block targetBlock) {

        itemGUIs.putIfAbsent(player.getUniqueId(), DEFAULT_GUI.clone());

        initialGUI.displayTo(player);

        return false;
    }

}
