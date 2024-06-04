package co.tantleffbeef.pluggytesty.war;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClaimWorld {
    private final Map<Team, List<BlockPosition>> claimedChunks;
    private final Map<BlockPosition, List<ClaimedChunk>> regionClaims;
    private final World world;

    /**
     * Stores in memory the claims within a world
     * @param world the world that this object stores claims for
     */
    public ClaimWorld(World world) {
        this.claimedChunks = new HashMap<>();
        this.regionClaims = new HashMap<>();
        this.world = world;
    }

    /**
     * Represents a chunk that has been claimed by a team
     * @param team the team that claimed the chunk
     * @param position the position of the chunk in question
     */
    private record ClaimedChunk(Team team, BlockPosition position) {}

    /**
     * Returns a list of chunks that have been claimed by this team
     * @param team the team to get chunks of
     * @return a list of block positions representing the position of those chunks
     */
    public List<BlockPosition> getTeamClaims(@NotNull Team team) {
        if (!claimedChunks.containsKey(team))
            return List.of();

        return List.copyOf(claimedChunks.get(team));
    }

    public boolean isChunkClaimed(BlockPosition chunkPosition) {
        // Check if this is a valid chunk position
        if (!chunkPosition.isChunkPosition())
            throw new InvalidPositionException("must be a chunk position");

        // Check if there are any chunks in this region
        BlockPosition regionPosition = chunkPosition.getRegionPosition();
        var chunksInRegion = regionClaims.get(regionPosition);
        if (chunksInRegion == null)
            return false;

        // See if any of the chunks in the region are this one
        for (ClaimedChunk chunk :
                chunksInRegion) {
            if (chunkPosition.equals(chunk.position))
                return true;
        }

        return false;
    }

    public void claimChunk(Team team, BlockPosition chunkPosition) {
        // Check if this is a valid chunk position
        if (!chunkPosition.isChunkPosition())
            throw new InvalidPositionException("must be a chunk position");

        // The list of chunks in this region and
        // the list of chunks owned by this team
        List<ClaimedChunk> regionChunks;
        List<BlockPosition> teamChunks;

        // Grab the region list
        BlockPosition regionPosition = chunkPosition.getRegionPosition();
        if (!regionClaims.containsKey(regionPosition))
            regionClaims.put(regionPosition, new ArrayList<>());
        regionChunks = regionClaims.get(regionPosition);

        // Grab the team list
        if (!claimedChunks.containsKey(team))
            claimedChunks.put(team, new ArrayList<>());
        teamChunks = claimedChunks.get(team);

        // Add the chunk
        regionChunks.add(new ClaimedChunk(team, chunkPosition));
        teamChunks.add(chunkPosition);
    }
}
