package co.tantleffbeef.pluggytesty.expeditions.rooms;

import co.tantleffbeef.pluggytesty.expeditions.RoomTransform;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AirLevitationCirclesRoom implements Room {
    private final List<Player> players = new ArrayList<>();
    private final List<Location> circleLocations = new ArrayList<>();
    private final List<AreaEffectCloud> areaEffectClouds = new ArrayList<>();

    private RoomTransform transform;

    public AirLevitationCirclesRoom(RoomTransform transform, JsonObject roomSettings) {
        var locationList = roomSettings.getAsJsonArray("levitationSpots");

        this.transform = transform;

        for (int i = 0; i < locationList.size(); i++) {
            var locationComponentsObject = locationList.get(i);
            if (locationComponentsObject == null || locationComponentsObject.isJsonNull())
                continue;

            var locationComponents = locationComponentsObject.getAsJsonArray();

            var location = transform.getLocation(locationComponents.get(0).getAsFloat(),
                    locationComponents.get(1).getAsFloat(),
                    locationComponents.get(2).getAsFloat());
            
            circleLocations.add(location);
        }
    }

    @Override
    public @NotNull Collection<Player> getPlayers() {
        return players;
    }

    @Override
    public void onFirstPlayerEnterRoom(@NotNull Player player) {
        transform.getLocation(5, 5, 5).getBlock().setType(Material.REDSTONE_BLOCK);

        for (var location:
             circleLocations) {
            var world = location.getWorld();
            assert world != null;
            world.spawn(location, AreaEffectCloud.class, cloud -> {
                // set cloud settings
                cloud.clearCustomEffects();
                cloud.addCustomEffect(
                        new PotionEffect(PotionEffectType.LEVITATION, 3 * 20, 6, true),
                        true);

                cloud.setDuration(1000000);

                // add it to the list
                areaEffectClouds.add(cloud);
            });
        }
    }

    @Override
    public void onLastPlayerExitRoom(@NotNull Player player) {
        for (var cloud:
             areaEffectClouds) {
            cloud.remove();
        }
    }
}
