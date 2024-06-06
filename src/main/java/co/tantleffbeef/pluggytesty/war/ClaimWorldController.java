package co.tantleffbeef.pluggytesty.war;

import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

public class ClaimWorldController {
    private final Map<World, ClaimWorld> worlds;

    public ClaimWorldController(HashMap<World, ClaimWorld> worlds) {
        this.worlds = worlds;
    }

    public ClaimWorldController() {
        this(new HashMap<>());
    }

    public ClaimWorld getClaimWorld(World world) {
        if (worlds.containsKey(world))
            return worlds.get(world);

        var claimWorld = new ClaimWorld();
        worlds.put(world, claimWorld);

        return claimWorld;
    }
}
