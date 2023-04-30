package co.tantleffbeef.pluggytesty;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import co.tantleffbeef.mcplanes.RecipeManager;
import co.tantleffbeef.mcplanes.ResourceApi;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import co.tantleffbeef.pluggytesty.armor.HeavyArmor;
import co.tantleffbeef.pluggytesty.armor.effect_listeners.*;
import co.tantleffbeef.pluggytesty.bosses.*;
import co.tantleffbeef.pluggytesty.custom.item.armor.SmithListener;
import co.tantleffbeef.pluggytesty.custom.item.weapons.BoltRodItemType;
import co.tantleffbeef.pluggytesty.custom.item.weapons.ClusterBombItemType;
import co.tantleffbeef.pluggytesty.custom.item.weapons.MagicStickItemType;
import co.tantleffbeef.pluggytesty.custom.item.armor.CraftListener;
import co.tantleffbeef.pluggytesty.custom.item.weapons.AxeOfYourMotherItemType;
import co.tantleffbeef.pluggytesty.expeditions.PTPartyManager;
import co.tantleffbeef.pluggytesty.expeditions.commands.PartyCommand;
import co.tantleffbeef.pluggytesty.misc.PlayerDeathMonitor;
import co.tantleffbeef.pluggytesty.utility.*;
import co.tantleffbeef.pluggytesty.villagers.VillagerTradesListener;
import co.tantleffbeef.pluggytesty.weapons.*;
import com.jeff_media.armorequipevent.ArmorEquipEvent;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.jar.JarFile;

@SuppressWarnings("unused")
public final class PluggyTesty extends JavaPlugin {
    private static final long PARTY_INVITE_EXPIRATION_TIME_SECONDS = 60L;

    private ResourceManager resourceManager;
    private RecipeManager recipeManager;

    @Override
    public void onEnable() {
        getLogger().info("penis hahaha");

        getLogger().info("grabbing resource manager");
        getLogger().info("poopie steam machine");
        final var rApiProvider = getServer().getServicesManager().getRegistration(ResourceApi.class);
        if (rApiProvider == null)
            throw new RuntimeException("Can't find ResourceApi!");

        final var rApi = rApiProvider.getProvider();
        //final ResourceApi rApi = (ResourceApi) JavaPlugin.getProvidingPlugin(ResourceApi.class);
        resourceManager = rApi.getResourceManager();
        recipeManager = rApi.getRecipeManager();

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

        getCommand("givemepole").setExecutor(new FrostPole());
        getCommand("givemeheal").setExecutor(new HealingHeart());
        //getCommand("givemesummon").setExecutor(new Summon());
        getCommand("givemed").setExecutor(new Digga());
        getCommand("givemesword").setExecutor(new SwordsmansDream());
        getCommand("givemego").setExecutor(new Goer());
        getCommand("givemecock").setExecutor((new Launcher()));
        getCommand("summonjawn").setExecutor(new BossJawn(this));
        getCommand("summonseaman").setExecutor(new BossSeaman(this));
        getCommand("giveheavyarmor").setExecutor(new HeavyArmor());
        getCommand("summongru").setExecutor(new BossGru(this));
        getCommand("summonbouncer").setExecutor(new BossFireWorker(this));
        getCommand("givemedash").setExecutor(new Dash());
        getCommand("givemeBow").setExecutor(new RandomEffectBow());


        getServer().getPluginManager().registerEvents(new FrostPoleInteractListener(), this);
        getServer().getPluginManager().registerEvents(new HealingHeartInteractListener(this), this);
        //getServer().getPluginManager().registerEvents(new SummonInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new DashInteractListener(), this);
        getServer().getPluginManager().registerEvents(new DiggaInteractListener(), this);
        getServer().getPluginManager().registerEvents(new SwordsmansDreamInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new GoerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new LauncherInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new RandomEffectBowInteractListener(), this);

        getServer().getPluginManager().registerEvents(new ArmorEquipListener(this), this);
        getServer().getPluginManager().registerEvents(new BowShootListener(), this);
        getServer().getPluginManager().registerEvents(new ExpChangeListener(), this);
        getServer().getPluginManager().registerEvents(new FallDamageListener(), this);
        getServer().getPluginManager().registerEvents(new DamageEffectListener(), this);
        getServer().getPluginManager().registerEvents(new EntityEffectListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathMonitor(), this);

        getServer().getPluginManager().registerEvents(new VillagerTradesListener(), this);
        getServer().getPluginManager().registerEvents(new CraftListener(), this);
        getServer().getPluginManager().registerEvents(new SmithListener(), this);


        ArmorEquipEvent.registerListener(this);
    }

    private void registerItems() {
        // Weapons
        resourceManager.registerItem(new MagicStickItemType(this, "magic_stick", false,
                "Magic Stick"));
        resourceManager.registerItem(new AxeOfYourMotherItemType(this, "mother_axe", false,
                ChatColor.AQUA + "Axe of Your Mother"));
        resourceManager.registerItem(new BoltRodItemType(this, "bolt_rod", false,
                "Bolt Rod"));
        resourceManager.registerItem(new ClusterBombItemType(this, "cluster_bomb", false, "Cluster Bomb"));
    }


    @Override
    public void onDisable() {
        getLogger().info("no more");
    }
}
