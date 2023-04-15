package co.tantleffbeef.pluggytesty;

import co.tantleffbeef.mcplanes.ResourceApi;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.pluggytesty.custom.item.MagicStickItemType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluggyTesty extends JavaPlugin {
    private ResourceManager resourceManager;

    @Override
    public void onLoad() {
        final RegisteredServiceProvider<ResourceApi> rapiProvider = getServer().getServicesManager()
                .getRegistration(ResourceApi.class);
        if (rapiProvider == null)
            throw new RuntimeException("Resource API cannot be found");
        final ResourceApi rApi = rapiProvider.getProvider();

        rApi.registerInitialBuildListener(this::onItemRegistration);
        resourceManager = rApi.getResourceManager();
    }

    @Override
    public void onEnable() {
        getLogger().info("penis hahaha");

        getCommand("givemewood").setExecutor(new MagicStick());
        getCommand("givemerod").setExecutor(new BoltRod());
        getCommand("givemepole").setExecutor(new FrostPole());
        getCommand("givemeheal").setExecutor(new HealingHeart());
        //getCommand("givemesummon").setExecutor(new Summon());
        getCommand("givemed").setExecutor(new Digga());
        getCommand("givemesword").setExecutor(new SwordsmansDream());
        getCommand("givemego").setExecutor(new Goer());
        getCommand("givemecock").setExecutor((new Launcher()));

        getServer().getPluginManager().registerEvents(new MagicStickInteractListener(), this);
        getServer().getPluginManager().registerEvents(new BoltRodInteractListener(), this);
        getServer().getPluginManager().registerEvents(new FrostPoleInteractListener(), this);
        getServer().getPluginManager().registerEvents(new HealingHeartInteractListener(this), this);
        //getServer().getPluginManager().registerEvents(new SummonInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new DiggaInteractListener(), this);
        getServer().getPluginManager().registerEvents(new SwordsmansDreamInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new GoerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new LauncherInteractListener(this), this);

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
