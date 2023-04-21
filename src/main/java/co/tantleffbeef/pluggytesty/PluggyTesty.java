package co.tantleffbeef.pluggytesty;

import co.tantleffbeef.mcplanes.ResourceApi;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.pluggytesty.custom.item.MagicStickItemType;
import com.jeff_media.armorequipevent.ArmorEquipEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluggyTesty extends JavaPlugin {
    private ResourceManager resourceManager;

    @Override
    public void onEnable() {
        getLogger().info("penis hahaha");

        getLogger().info("grabbing resource manager");
        final var rApiProvider = getServer().getServicesManager().getRegistration(ResourceApi.class);
        if (rApiProvider == null)
            throw new RuntimeException("Can't find ResourceApi!");

        final var rApi = rApiProvider.getProvider();
        //final ResourceApi rApi = (ResourceApi) JavaPlugin.getProvidingPlugin(ResourceApi.class);
        resourceManager = rApi.getResourceManager();
        onItemRegistration();

        getCommand("givemewood").setExecutor(new MagicStick());
        getCommand("givemerod").setExecutor(new BoltRod());
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


        getServer().getPluginManager().registerEvents(new MagicStickInteractListener(), this);
        getServer().getPluginManager().registerEvents(new BoltRodInteractListener(), this);
        getServer().getPluginManager().registerEvents(new FrostPoleInteractListener(), this);
        getServer().getPluginManager().registerEvents(new HealingHeartInteractListener(this), this);
        //getServer().getPluginManager().registerEvents(new SummonInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new DiggaInteractListener(), this);
        getServer().getPluginManager().registerEvents(new SwordsmansDreamInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new GoerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new LauncherInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new ArmorEquipListener(), this);
        ArmorEquipEvent.registerListener(this);
    }

    private void onItemRegistration() {
        resourceManager.registerItem(new MagicStickItemType(this, "magic_stick", false,
                "Magic Stick"));
    }

    @Override
    public void onDisable() {
        getLogger().info("no more");
    }
}
