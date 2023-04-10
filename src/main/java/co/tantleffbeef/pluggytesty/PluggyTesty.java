package co.tantleffbeef.pluggytesty;

import org.bukkit.plugin.java.JavaPlugin;

public final class PluggyTesty extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("penis hahaha");

        getCommand("givemewood").setExecutor(new MagicStick());
        getCommand("givemerod").setExecutor(new BoltRod());
        getCommand("givemepole").setExecutor(new FrostPole());
        getCommand("givemeheal").setExecutor(new HealingHeart());
        //getCommand("givemesummon").setExecutor(new Summon());
        getCommand("dontgivemeWE").setExecutor(new WorldEnder());

        getServer().getPluginManager().registerEvents(new MagicStickInteractListener(), this);
        getServer().getPluginManager().registerEvents(new BoltRodInteractListener(), this);
        getServer().getPluginManager().registerEvents(new FrostPoleInteractListener(), this);
        getServer().getPluginManager().registerEvents(new HealingHeartInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new SummonInteractListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("no more");
    }
}
