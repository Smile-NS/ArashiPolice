package io.github.smile_ns.arashipolice.penalty;

import io.github.smile_ns.simplejson.SimpleJson;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static io.github.smile_ns.arashipolice.ConfigManager.BROADCAST;
import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.*;

public class Penalty {

    public static final String URL = replaceSep(
            "plugins\\ArashiPolice\\players.db");
    public static Connection connection;
    public static Statement statement;

    public Penalty() {
        open();
    }

    public static void tableInit() throws Exception {
        openDirect();
        String[] commands = {
                "CREATE TABLE IF NOT EXISTS move(uuid TEXT, ip TEXT)",
                "CREATE TABLE IF NOT EXISTS chat(uuid TEXT, ip TEXT)",
                "CREATE TABLE IF NOT EXISTS attack(uuid TEXT, ip TEXT)",
                "CREATE TABLE IF NOT EXISTS break(uuid TEXT, ip TEXT)",
                "CREATE TABLE IF NOT EXISTS place(uuid TEXT, ip TEXT)",
                "CREATE TABLE IF NOT EXISTS interact(uuid TEXT, ip TEXT)",
                "CREATE TABLE IF NOT EXISTS jail(uuid TEXT, ip TEXT)"
        };
        for (String cmd : commands) statement.executeUpdate(cmd);
    }

    public static void dataMigration() throws Exception {
        File players = new File("plugins\\ArashiPolice\\players.json");
        if (players.exists()) {
            SimpleJson json = new SimpleJson(players);
            json.setSeparator("/");

            players.delete();
            for (String node : json.getKeySet("arashi")) {
                for (Object val : json.getList("arashi/" + node)) {
                    Sinner sinner = val.toString().contains(".") ?
                            new Sinner(null, val.toString()) : new Sinner(val.toString(), null);
                    sinner.register(node);
                }
            }
        }

        File jail = new File("plugins\\ArashiPolice\\jail.json");
        if (jail.exists()) {
            SimpleJson json = new SimpleJson(jail);
            json.setSeparator("/");

            jail.delete();
            for (Object val : json.getList("players")) {
                Sinner sinner = val.toString().contains(".") ?
                        new Sinner(null, val.toString()) : new Sinner(val.toString(), null);
                sinner.register("jail");
            }
        }

        if (players.exists() || jail.exists()) save();
    }

    public static void open() {
        try {
            if (!connection.isClosed()) return;
            openDirect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void log(Player player, String msg) {
        player.sendMessage(GRAY + msg);
        if (BROADCAST) getServer().broadcastMessage(
                DARK_PURPLE + "ArashiPolice" + GRAY + ">> " + DARK_PURPLE + msg);
    }

    public static void openDirect() throws Exception {
        File db = new File(replaceSep(URL));
        if (!db.exists()) db.createNewFile();
        connection = DriverManager.getConnection("jdbc:sqlite:" + URL);
        statement = connection.createStatement();
        statement.setQueryTimeout(30);
    }

    public static void close() {
        try {
            if(connection != null) connection.close();
            if(statement != null) statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        close();
        open();
    }

    public static String replaceSep(String path) {
        return path.replaceAll("\"", File.separator);
    }
}
