package io.github.smile_ns.arashipolice;

import io.github.smile_ns.arashipolice.commands.PoliceCmd;
import io.github.smile_ns.arashipolice.gui.JailPanel;
import io.github.smile_ns.arashipolice.gui.PenaltyPanel;
import io.github.smile_ns.arashipolice.gui.PlayerListPanel;
import io.github.smile_ns.arashipolice.penalty.Penalty;
import io.github.smile_ns.arashipolice.penalty.Sinner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ArashiPolice extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        try {
            ConfigManager.saveConfig();
            Penalty.tableInit();
            Penalty.dataMigration();
        } catch (Exception e) {
            e.printStackTrace();
        }
        registerEvents();
        getCommand("police").setExecutor(new PoliceCmd());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Penalty.close();
    }

    public void registerEvents() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new ArashiActionListener(), this);
        manager.registerEvents(new JailWorld(), this);
        manager.registerEvents(new PenaltyPanel(), this);
        manager.registerEvents(new JailPanel(), this);
        manager.registerEvents(new PlayerListPanel(), this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Sinner.clean(player);
    }
}
