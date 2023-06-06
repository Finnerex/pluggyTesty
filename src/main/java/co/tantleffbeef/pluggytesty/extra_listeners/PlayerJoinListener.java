package co.tantleffbeef.pluggytesty.extra_listeners;

import co.tantleffbeef.pluggytesty.goober.Goober;
import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerJoinListener implements Listener {

    private final Scoreboard levelBoard;
    private final GooberStateController gooberStateController;

    public PlayerJoinListener(Scoreboard levelBoard, GooberStateController gooberStateController) {
        this.levelBoard = levelBoard;
        this.gooberStateController = gooberStateController;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        Goober goober = gooberStateController.wrapPlayer(player);

        levelBoard.getObjective("gooberLevel")
                .getScore(player.getName())
                .setScore(goober.getLevel());

        event.getPlayer().setScoreboard(levelBoard);

    }

}
