package co.tantleffbeef.pluggytesty;

import org.bukkit.plugin.java.JavaPlugin;

public final class PluggyTesty extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("penis haha");
        getCommand("givemewood").setExecutor(new MagicStick());
        getCommand("givemerod").setExecutor(new BoltRod());
        //getCommand("givemepole").setExecutor(new FrostPole());
        getServer().getPluginManager().registerEvents(new MagicStickInteractListener(), this);
        getServer().getPluginManager().registerEvents(new BoltRodInteractListener(this), this);
        //getServer().getPluginManager().registerEvents(new FrostPoleInteractListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("no more");
    }
}
