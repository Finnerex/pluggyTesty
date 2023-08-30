package co.tantleffbeef.pluggytesty.expeditions.rooms;

import com.google.gson.JsonObject;
import org.bukkit.Location;
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

    public AirLevitationCirclesRoom(Location minimumLocation, JsonObject roomSettings) {
        var locationList = roomSettings.getAsJsonArray("levitationSpots");

        for (int i = 0; i < locationList.size(); i++) {
            var locationComponents = locationList.get(i).getAsJsonArray();
            var location = minimumLocation.clone().add(locationComponents.get(0).getAsFloat(),
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
        for (var location:
             circleLocations) {
            var world = location.getWorld();
            assert world != null;
            world.spawn(location, AreaEffectCloud.class, cloud -> {
                cloud.clearCustomEffects();
                cloud.addCustomEffect(
                        new PotionEffect(PotionEffectType.LEVITATION, 1000000, 1, true),
                        true);
            });
        }
    }
}
