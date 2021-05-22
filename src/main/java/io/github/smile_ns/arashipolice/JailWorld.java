package io.github.smile_ns.arashipolice;

import io.github.smile_ns.arashipolice.penalty.Penalty;
import io.github.smile_ns.arashipolice.penalty.Sinner;
import io.github.smile_ns.simplejson.SimpleJson;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class JailWorld extends ConfigManager {

    public static SimpleJson json;

    static {
        ConfigManager.saveConfig();
        try {
            json = new SimpleJson(
                    new File("plugins" + File.separator + "ArashiPolice" + File.separator + "jail.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
