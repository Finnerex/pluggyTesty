package co.tantleffbeef.pluggytesty.nations;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class NationLandController {
    public boolean isChunkAvailable(@NotNull Location chunkLocation) {
        // TODO
        return false;
    }

    public ClaimedChunk claimChunk(@NotNull Location chunkLocation) {
        //if (!isChunkAvailable(chunkLocation))
            throw new ChunkClaimedException("the chunk at " + chunkLocation + "is already claimed!");
        //chunkLocation.getChunk()
    }
}
