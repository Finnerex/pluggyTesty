package co.tantleffbeef.pluggytesty;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import co.tantleffbeef.mcplanes.*;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import co.tantleffbeef.pluggytesty.armor.BaseArmor;
import co.tantleffbeef.pluggytesty.armor.HeavyArmor;
import co.tantleffbeef.pluggytesty.armor.effect_listeners.*;
import co.tantleffbeef.pluggytesty.bosses.*;
import co.tantleffbeef.pluggytesty.custom.item.utility.*;
import co.tantleffbeef.pluggytesty.custom.item.weapons.*;
import co.tantleffbeef.pluggytesty.custom.item.armor.*;
import co.tantleffbeef.pluggytesty.expeditions.PTPartyManager;
import co.tantleffbeef.pluggytesty.expeditions.commands.PartyCommand;
import co.tantleffbeef.pluggytesty.misc.PlayerDeathMonitor;
import co.tantleffbeef.pluggytesty.villagers.VillagerTradesListener;
import com.jeff_media.armorequipevent.ArmorEquipEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.jar.JarFile;

@SuppressWarnings("unused")
public final class PluggyTesty extends JavaPlugin {
    private static final long PARTY_INVITE_EXPIRATION_TIME_SECONDS = 60L;

    private ResourceManager resourceManager;
    private RecipeManager recipeManager;
    private KeyManager<CustomNbtKey> nbtKeyManager;

    @Override
    public void onEnable() {
        getLogger().info("penis hahaha");

        getLogger().info("grabbing resource manager");
        getLogger().info("poopie steam machine");
        final var rApiProvider = getServer().getServicesManager().getRegistration(ResourceApi.class);
        if (rApiProvider == null)
            throw new RuntimeException("Can't find ResourceApi!");

        final var rApi = rApiProvider.getProvider();
        resourceManager = rApi.getResourceManager();
        recipeManager = rApi.getRecipeManager();
        nbtKeyManager = rApi.getNbtKeyManager();

        registerItems();
//        registerRecipes();

        // Adds all the textures and models in the resources folder to the resource pack
        try (JarFile jar = new JarFile(getFile())) {
            resourceManager.addAssetsFolder(jar);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final var partyManager = new PTPartyManager();
        final var commandManager = new PaperCommandManager(this);
        commandManager.getCommandCompletions().registerCompletion("partyPlayers", context -> {
            final var player = context.getPlayer();
            final var party = partyManager.getPartyWith(player);

            if (party == null)
                throw new InvalidCommandArgument();

            return party.getAllPlayers().stream()
                    .map(OfflinePlayer::getName)
                    .toList();
        });

        commandManager.registerCommand(new PartyCommand(this, getServer(), partyManager, PARTY_INVITE_EXPIRATION_TIME_SECONDS));

        getCommand("summonjawn").setExecutor(new BossJawn(this));
        getCommand("summonseaman").setExecutor(new BossSeaman(this));
        getCommand("giveheavyarmor").setExecutor(new HeavyArmor());
        getCommand("summongru").setExecutor(new BossGru(this));
        getCommand("summonbouncer").setExecutor(new BossFireWorker(this));


        getServer().getPluginManager().registerEvents(new RandomEffectBowInteractListener(nbtKeyManager, resourceManager), this);

        // Trims / Armor effects
        getServer().getPluginManager().registerEvents(new ArmorEquipListener(this), this);
        getServer().getPluginManager().registerEvents(new BowShootListener(), this);
        getServer().getPluginManager().registerEvents(new ExpChangeListener(), this);
        getServer().getPluginManager().registerEvents(new FallDamageListener(), this);
        getServer().getPluginManager().registerEvents(new DamageEffectListener(), this);
        getServer().getPluginManager().registerEvents(new EntityEffectListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerUnswimListener(), this);

        // Special Keep-inventory
        getServer().getPluginManager().registerEvents(new PlayerDeathMonitor(), this);

        getServer().getPluginManager().registerEvents(new VillagerTradesListener(), this);
        getServer().getPluginManager().registerEvents(new CraftListener(), this);
        getServer().getPluginManager().registerEvents(new SmithListener(), this);


        ArmorEquipEvent.registerListener(this);

        registerRecipes();

    }

    private void registerItems() {
        // Weapons
        resourceManager.registerItem(new MagicStickItemType(this, "magic_stick", false, "Magic Stick"));
        resourceManager.registerItem(new AxeOfYourMotherItemType(this, "mother_axe", false, ChatColor.AQUA + "Axe of Your Mother"));
        resourceManager.registerItem(new BoltRodItemType(this, "bolt_rod", false, "Bolt Rod"));
        resourceManager.registerItem(new ClusterBombItemType(this, "cluster_bomb", false, "Cluster Bomb"));
        resourceManager.registerItem(new FrostPoleItemType(this, "frost_pole", false, ChatColor.AQUA + "Frost Pole"));
        resourceManager.registerItem(new SwordsmansDreamItemType(this, "swordsmans_dream", false, ChatColor.AQUA + "Swordsman's Dream"));
        resourceManager.registerItem(new RandomEffectBowItemType(this, "random_bow", false, "Random Effect Bow"));
        resourceManager.registerItem(new LauncherItemType(this, "launcher", false, "Launcher"));
        resourceManager.registerItem(new MinotaursAxeItemType(this, "minotaurs_axe", false, ChatColor.DARK_GREEN + "Minotaur's Axe"));
        resourceManager.registerItem(new ZapinatorItemType(this, "zapinator", false, ChatColor.GOLD + "Zapinator"));
        resourceManager.registerItem(new FlamelashItemType(this, "flamelash", false, ChatColor.GOLD + "Flamelash"));

        //Utility
        resourceManager.registerItem(new GoItemType(this, "go", false, "Go!"));
        resourceManager.registerItem(new HealingHeartItemType(this, "healing_heart", false, ChatColor.RED + "Healing Heart"));
        resourceManager.registerItem(new DashItemType(this, "dash", false, "Dash"));
        resourceManager.registerItem(new DiggaItemType(this, "digga", false, "Digga"));
    }


    @Override
    public void onDisable() {
        getLogger().info("no more");
    }

    private void registerRecipes() {
        final ShapedRecipe chainHelm = new ShapedRecipe(NamespacedKey.minecraft("chainmail_helmet"), BaseArmor.cH())
                .shape(
                        "ccc",
                        "c c",
                        "   ")
                .setIngredient('c', Material.CHAIN);
        getServer().addRecipe(chainHelm);
        recipeManager.registerUnlockableRecipe(NamespacedKey.minecraft("chainmail_helmet"), Material.CHAIN);


        final ShapedRecipe chainBoots = new ShapedRecipe(NamespacedKey.minecraft("chainmail_boots"), BaseArmor.cB())
                .shape(
                        "   ",
                        "c c",
                        "c c")
                .setIngredient('c', Material.CHAIN);
        getServer().addRecipe(chainBoots);
        recipeManager.registerUnlockableRecipe(NamespacedKey.minecraft("chainmail_boots"), Material.CHAIN);


        final ShapedRecipe chainChestplate = new ShapedRecipe(NamespacedKey.minecraft("chainmail_chestplate"), BaseArmor.cC())
                .shape(
                        "c c",
                        "ccc",
                        "ccc")
                .setIngredient('c', Material.CHAIN);
        getServer().addRecipe(chainChestplate);
        recipeManager.registerUnlockableRecipe(NamespacedKey.minecraft("chainmail_chestplate"), Material.CHAIN);


        final ShapedRecipe chainLeggings = new ShapedRecipe(NamespacedKey.minecraft("chainmail_leggings"), BaseArmor.cL())
                .shape(
                        "ccc",
                        "c c",
                        "c c")
                .setIngredient('c', Material.CHAIN);
        getServer().addRecipe(chainLeggings);
        recipeManager.registerUnlockableRecipe(NamespacedKey.minecraft("chainmail_leggings"), Material.CHAIN);
    }

}