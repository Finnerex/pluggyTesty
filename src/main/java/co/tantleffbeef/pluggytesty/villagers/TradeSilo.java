package co.tantleffbeef.pluggytesty.villagers;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TradeSilo {

    public static MerchantRecipe createRecipe(int currencyAmt, ItemStack output) {
        MerchantRecipe res = new MerchantRecipe(output, 0, 10, false, 0, 0f);
        res.addIngredient(new ItemStack(Material.SUNFLOWER, currencyAmt));

        return res;
    }

    public static MerchantRecipe createRecipe(int currencyAmt, ItemStack input2, ItemStack output) {
        MerchantRecipe res = new MerchantRecipe(output, 0, 10, false, 0, 0f);
        res.addIngredient(new ItemStack(Material.SUNFLOWER, currencyAmt));
        res.addIngredient(input2);

        return res;
    }

    public static MerchantRecipe upgradeRecipe(int lvl) { // level is the villager current level, so this will make them reach level + 1.
        assert lvl >= 1 && lvl <= 4;
        int exp = 0;
        Material cost = Material.BEDROCK;

        switch(lvl) {
            case 1 -> {exp = 10; cost = Material.COBBLESTONE;}
            case 2 -> {exp = 70; cost = Material.IRON_BLOCK;}
            case 3 -> {exp = 150; cost = Material.GOLD_BLOCK;}
            case 4 -> {exp = 250; cost = Material.DIAMOND_BLOCK;}
        }
        MerchantRecipe res = new MerchantRecipe(new ItemStack(Material.RED_WOOL, 1), 0, 1, false, exp, 0f);
        res.addIngredient(new ItemStack(cost, 64));

        return res;
    }
    public final static int[][] tradeAmts = {
            {2, 1, 1, 1, 2}, // Armorer
            {2, 2, 1, 2, 1}, // Butcher
            {0, 0, 0, 0, 0}, // Cartographer
            {3, 2, 1, 1, 3}, // Cleric
            {2, 2, 1, 1, 1}, // Farmer
            {2, 1, 1, 1, 1}, // Fisherman
            {0, 0, 0, 0, 0}, // Fletcher
            {0, 0, 0, 0, 0}, // Leatherworker
            {2, 2, 2, 2, 2}, // Librarian
            {2, 2, 2, 1, 2}, // Mason
            {0, 0, 0, 0, 0}, // Shepherd
            {2, 2, 2, 2, 2}, // Toolsmith
            {1, 1, 1, 1, 1}, // Weaponsmith
    };

    public final static List<List<MerchantRecipe>> armorerTrades = Arrays.asList( // CHANGE THESE TO BE THE CORRECT TRIMS YOU BUFFOON
            Arrays.asList(createRecipe(1, new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, 2)), createRecipe(1, new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1), new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 2))), // Novice tier,
            Arrays.asList(createRecipe(1, new ItemStack(Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, 2)), createRecipe(1, new ItemStack(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, 2)), createRecipe(1, new ItemStack(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, 2))), // Apprentice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, 2)), createRecipe(1, new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, 2)), createRecipe(1, new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, 2))), // Journeyman tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, 2)), createRecipe(1, new ItemStack(Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, 2)), createRecipe(1, new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, 2))), // Expert tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, 2)), createRecipe(1, new ItemStack(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, 2)), createRecipe(1, new ItemStack(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, 2)), createRecipe(1, new ItemStack(Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, 2)), createRecipe(1, new ItemStack(Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, 1), new ItemStack(Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, 2)))  // Master tier
    );

    public final static List<List<MerchantRecipe>> butcherTrades = Arrays.asList(
            Arrays.asList(createRecipe(1, new ItemStack(Material.EGG, 8)), createRecipe(1, new ItemStack(Material.FEATHER, 12)), createRecipe(1, new ItemStack(Material.ROTTEN_FLESH, 32))), // Novice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.MUTTON, 5)), createRecipe(1, new ItemStack(Material.CHICKEN, 5)), createRecipe(1, new ItemStack(Material.RABBIT, 7)), createRecipe(1, new ItemStack(Material.MILK_BUCKET, 1))), // Apprentice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.PORKCHOP, 3)), createRecipe(1, new ItemStack(Material.BEEF, 3))), // Journeyman tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.COOKED_MUTTON, 6)), createRecipe(1, new ItemStack(Material.COOKED_CHICKEN, 6)), createRecipe(1, new ItemStack(Material.COOKED_RABBIT, 8)), createRecipe(1, new ItemStack(Material.RABBIT_STEW, 1))), // Expert tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.COOKED_PORKCHOP, 5)), createRecipe(1, new ItemStack(Material.COOKED_BEEF, 53)))  // Master tier
    );

    public final static List<List<MerchantRecipe>> clericTrades = Arrays.asList( // add potions
            Arrays.asList(createRecipe(1, new ItemStack(Material.GLASS_BOTTLE, 3)), createRecipe(1, new ItemStack(Material.BLAZE_POWDER, 1)), createRecipe(1, new ItemStack(Material.GUNPOWDER, 4)), createRecipe(1, new ItemStack(Material.REDSTONE, 4)), createRecipe(1, new ItemStack(Material.GLOWSTONE_DUST, 4)), createRecipe(1, new ItemStack(Material.DRAGON_BREATH, 1)), createRecipe(1, new ItemStack(Material.FERMENTED_SPIDER_EYE, 3))), // Novice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.GLASS_BOTTLE, 3)), createRecipe(1, new ItemStack(Material.BLAZE_POWDER, 1)), createRecipe(1, new ItemStack(Material.GUNPOWDER, 4)), createRecipe(1, new ItemStack(Material.REDSTONE, 4))), // Apprentice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.GLASS_BOTTLE, 3)), createRecipe(1, new ItemStack(Material.BLAZE_POWDER, 1)), createRecipe(1, new ItemStack(Material.GUNPOWDER, 4)), createRecipe(1, new ItemStack(Material.REDSTONE, 4))), // Journeyman tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.GLASS_BOTTLE, 3)), createRecipe(1, new ItemStack(Material.BLAZE_POWDER, 1)), createRecipe(1, new ItemStack(Material.GUNPOWDER, 4)), createRecipe(1, new ItemStack(Material.REDSTONE, 4))), // Expert tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.GLASS_BOTTLE, 3)), createRecipe(1, new ItemStack(Material.BLAZE_POWDER, 1)), createRecipe(1, new ItemStack(Material.GUNPOWDER, 4)), createRecipe(1, new ItemStack(Material.REDSTONE, 4)))  // Master tier
    );

    public final static List<List<MerchantRecipe>> farmerTrades = Arrays.asList( // give stews the correct effects and change torchflower seeds 2 to pitcher pods
            Arrays.asList(createRecipe(1, new ItemStack(Material.WHEAT_SEEDS, 16)), createRecipe(1, new ItemStack(Material.BEETROOT_SEEDS, 16)), createRecipe(1, new ItemStack(Material.CARROT, 2)), createRecipe(1, new ItemStack(Material.POTATO, 2)), createRecipe(1, new ItemStack(Material.BEETROOT, 12)), createRecipe(1, new ItemStack(Material.WHEAT, 9))), // Novice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.DRIED_KELP, 32)), createRecipe(1, new ItemStack(Material.COOKIE, 24)), createRecipe(1, new ItemStack(Material.GLOW_BERRIES, 12)), createRecipe(1, new ItemStack(Material.SWEET_BERRIES, 12)), createRecipe(1, new ItemStack(Material.MELON_SEEDS, 4)), createRecipe(1, new ItemStack(Material.PUMPKIN_SEEDS, 4))), // Apprentice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.BREAD, 8)), createRecipe(1, new ItemStack(Material.BEETROOT_SOUP, 1)), createRecipe(1, new ItemStack(Material.MUSHROOM_STEW, 1)), createRecipe(1, new ItemStack(Material.SUSPICIOUS_STEW, 1)), createRecipe(1, new ItemStack(Material.APPLE, 3))), // Journeyman tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.TORCHFLOWER_SEEDS, 6)), createRecipe(1, new ItemStack(Material.TORCHFLOWER_SEEDS, 37)), createRecipe(1, new ItemStack(Material.PUMPKIN_PIE, 8)), createRecipe(1, new ItemStack(Material.HONEY_BOTTLE, 4))), // Expert tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.SUSPICIOUS_STEW, 1)), createRecipe(1, new ItemStack(Material.GOLDEN_CARROT, 2)), createRecipe(1, new ItemStack(Material.GOLDEN_APPLE, 1)))  // Master tier
    );

    public final static List<List<MerchantRecipe>> fishermanTrades = Arrays.asList( // add enchanted stuff at the end
            Arrays.asList(createRecipe(1, new ItemStack(Material.COD, 3)), createRecipe(1, new ItemStack(Material.SALMON, 2)), createRecipe(1, new ItemStack(Material.PUFFERFISH, 1)), createRecipe(1, new ItemStack(Material.TROPICAL_FISH, 6)), createRecipe(1, new ItemStack(Material.INK_SAC, 4)), createRecipe(1, new ItemStack(Material.BONE_MEAL, 3)), createRecipe(1, new ItemStack(Material.KELP, 6)), createRecipe(1, new ItemStack(Material.LILY_PAD, 5)), createRecipe(1, new ItemStack(Material.COCOA_BEANS, 2))), // Novice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.COD_BUCKET, 1)), createRecipe(1, new ItemStack(Material.SALMON_BUCKET, 1)), createRecipe(1, new ItemStack(Material.PUFFERFISH_BUCKET, 1)), createRecipe(1, new ItemStack(Material.TROPICAL_FISH_BUCKET, 1)), createRecipe(1, new ItemStack(Material.TURTLE_HELMET, 1))), // Apprentice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.NAUTILUS_SHELL, 1)), createRecipe(1, new ItemStack(Material.PRISMARINE_SHARD, 12)), createRecipe(1, new ItemStack(Material.PRISMARINE_CRYSTALS, 6))), // Journeyman tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.AXOLOTL_BUCKET, 1)), createRecipe(1, new ItemStack(Material.TADPOLE_BUCKET, 1)), createRecipe(1, new ItemStack(Material.SADDLE, 1)), createRecipe(1, new ItemStack(Material.NAME_TAG, 1)), createRecipe(1, new ItemStack(Material.SPONGE, 2))), // Expert tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.BOW, 1)), createRecipe(1, new ItemStack(Material.FISHING_ROD, 1)), createRecipe(1, new ItemStack(Material.BOOK, 1)))  // Master tier
    );

    public final static List<List<MerchantRecipe>> librarianTrades = Arrays.asList(
            Arrays.asList(createRecipe(1, new ItemStack(Material.BOOK, 1)), createRecipe(1, new ItemStack(Material.BOOK, 1))), // Novice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.BOOK, 1)), createRecipe(1, new ItemStack(Material.BOOK, 1))), // Apprentice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.BOOK, 1)), createRecipe(1, new ItemStack(Material.BOOK, 1))), // Journeyman tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.BOOK, 1)), createRecipe(1, new ItemStack(Material.BOOK, 1))), // Expert tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.BOOK, 1)), createRecipe(1, new ItemStack(Material.BOOK, 1)))  // Master tier
    );

    public final static List<List<MerchantRecipe>> masonTrades = Arrays.asList(
            Arrays.asList(createRecipe(1, new ItemStack(Material.POLISHED_ANDESITE, 12)), createRecipe(1, new ItemStack(Material.POLISHED_GRANITE, 12)), createRecipe(1, new ItemStack(Material.POLISHED_DIORITE, 12)), createRecipe(1, new ItemStack(Material.POLISHED_DEEPSLATE, 8)), createRecipe(1, new ItemStack(Material.STONE_BRICKS, 8))), // Novice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.SANDSTONE, 8)), createRecipe(1, new ItemStack(Material.RED_SANDSTONE, 8)), createRecipe(1, new ItemStack(Material.BRICKS, 6)), createRecipe(1, new ItemStack(Material.MUD_BRICKS, 6))), // Apprentice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.QUARTZ_BLOCK, 4)), createRecipe(1, new ItemStack(Material.RED_NETHER_BRICKS, 10)), createRecipe(1, new ItemStack(Material.POLISHED_BLACKSTONE, 8))), // Journeyman tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.TERRACOTTA, 8))), // Expert tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.COPPER_BLOCK, 4)), createRecipe(1, new ItemStack(Material.PURPUR_BLOCK, 8)), createRecipe(1, new ItemStack(Material.END_STONE_BRICKS, 8)))  // Master tier
    );

    public final static List<List<MerchantRecipe>> toolsmithTrades = Arrays.asList(
            Arrays.asList(createRecipe(1, new ItemStack(Material.WOODEN_PICKAXE, 1)), createRecipe(1, new ItemStack(Material.WOODEN_SHOVEL, 1)), createRecipe(1, new ItemStack(Material.WOODEN_HOE, 1)), createRecipe(1, new ItemStack(Material.BRUSH, 1))), // Novice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.STONE_PICKAXE, 1)), createRecipe(1, new ItemStack(Material.STONE_SHOVEL, 1)), createRecipe(1, new ItemStack(Material.STONE_HOE, 1))), // Apprentice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.IRON_PICKAXE, 1)), createRecipe(1, new ItemStack(Material.IRON_SHOVEL, 1)), createRecipe(1, new ItemStack(Material.IRON_HOE, 1))), // Journeyman tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.GOLDEN_PICKAXE, 1)), createRecipe(1, new ItemStack(Material.GOLDEN_SHOVEL, 1)), createRecipe(1, new ItemStack(Material.GOLDEN_HOE, 1))), // Expert tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.DIAMOND_PICKAXE, 1)), createRecipe(1, new ItemStack(Material.DIAMOND_SHOVEL, 1)), createRecipe(1, new ItemStack(Material.DIAMOND_HOE, 1)))  // Master tier
    );

    public final static List<List<MerchantRecipe>> weaponsmithTrades = Arrays.asList(
            Arrays.asList(createRecipe(1, new ItemStack(Material.WOODEN_SWORD, 1)), createRecipe(1, new ItemStack(Material.WOODEN_AXE, 1))), // Novice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.STONE_SWORD, 1)), createRecipe(1, new ItemStack(Material.STONE_AXE, 1))), // Apprentice tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.IRON_SWORD, 1)), createRecipe(1, new ItemStack(Material.IRON_AXE, 1))), // Journeyman tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.GOLDEN_SWORD, 1)), createRecipe(1, new ItemStack(Material.GOLDEN_AXE, 1))), // Expert tier
            Arrays.asList(createRecipe(1, new ItemStack(Material.DIAMOND_SWORD, 1)), createRecipe(1, new ItemStack(Material.DIAMOND_AXE, 1)))  // Master tier
    );

}
