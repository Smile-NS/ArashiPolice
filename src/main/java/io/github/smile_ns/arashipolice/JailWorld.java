package io.github.smile_ns.arashipolice;

import io.github.smile_ns.arashipolice.penalty.Penalty;
import io.github.smile_ns.arashipolice.penalty.Sinner;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.sql.SQLException;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class JailWorld extends ConfigManager implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerJoinEvent(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        if (isPrisoner(player)) goToJail(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerRespawnEvent(PlayerRespawnEvent event) throws SQLException {
        Player player = event.getPlayer();
        if (isPrisoner(player)) {
            World world = getJailWorld();
            if (world == null) return;
            event.setRespawnLocation(world.getSpawnLocation());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerChangedWorldEvent(PlayerChangedWorldEvent event) throws SQLException {
        Player player = event.getPlayer();
        if (isPrisoner(player) &&
                player.getWorld() != getJailWorld()) goToJail(player);
    }

    public static void addPlayer(Player player) throws SQLException {
        Sinner sinner = new Sinner(player);
        sinner.impose();
        Penalty.save();
    }

    public static void removePlayer(Player player) throws SQLException {
        Sinner sinner = new Sinner(player);
        sinner.release();
        Penalty.save();
    }

    public static void recordWorld(World world) {
        config.set("jail", world.getUID().toString());
        saveConfig();
    }

    public static void goToJail(Player player) {
        World world = getJailWorld();
        assert world != null;
        Location loc = world.getSpawnLocation();
        player.teleport(loc);
    }

    public static boolean isPrisoner(Player player) throws SQLException {
        return new Sinner(player).getPenalties().contains("jail");
    }

    public static World getJailWorld() {
        String uuid = config.getString("jail");
        if (uuid == null) return null;
        return getServer().getWorld(UUID.fromString(uuid));
    }
}
