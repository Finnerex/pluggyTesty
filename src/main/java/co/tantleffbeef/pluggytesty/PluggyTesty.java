package co.tantleffbeef.pluggytesty;

import org.bukkit.plugin.java.JavaPlugin;

public final class PluggyTesty extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("penis haha");
        getCommand("givemewood").setExecutor(new MagicStick());

    }

    @Override
    public void onDisable() {
        System.out.println("kys");
    }
}
