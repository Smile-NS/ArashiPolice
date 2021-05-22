package io.github.smile_ns.arashipolice;

import io.github.smile_ns.arashipolice.commands.Complement;
import io.github.smile_ns.arashipolice.commands.PoliceCmd;
import io.github.smile_ns.arashipolice.gui.JailPanel;
import io.github.smile_ns.arashipolice.gui.PenaltyPanel;
import io.github.smile_ns.arashipolice.gui.PlayerListPanel;
import io.github.smile_ns.arashipolice.penalty.Penalty;
import io.github.smile_ns.arashipolice.penalty.Sinner;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.List;

public final class ArashiPolice extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        try {
            Penalty.tableInit();
            Penalty.dataMigration();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConfigManager.saveConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Penalty.close();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerJoinEvent(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        if (JailWorld.isPrisoner(player)) JailWorld.goToJail(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Sinner.clean(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerMoveEvent(PlayerMoveEvent event) throws SQLException {
        ArashiToggleAction.move(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerToggleSneakEvent(PlayerToggleSneakEvent event) throws SQLException {
        ArashiToggleAction.move(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event) throws SQLException {
        ArashiToggleAction.chat(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent event) throws SQLException {
        ArashiToggleAction.attack(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBreakEvent(BlockBreakEvent event) throws SQLException {
        ArashiToggleAction.blockBreak(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBreakEvent(BlockPlaceEvent event) throws SQLException {
        ArashiToggleAction.blockPlace(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerInteractEvent(PlayerInteractEvent event) throws SQLException {
        ArashiToggleAction.interact(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void InventoryClickEvent(InventoryClickEvent event) throws SQLException {
        new PenaltyPanel().onClick(event);
        new JailPanel().onClick(event);
        new PlayerListPanel().onClick(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void InventoryCloseEvent(InventoryCloseEvent event) {
        PenaltyPanel.onClose(event);
        JailPanel.onClose(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerRespawnEvent(PlayerRespawnEvent event) throws SQLException {
        Player player = event.getPlayer();
        if (JailWorld.isPrisoner(player)) {
            World world = JailWorld.getJailWorld();
            if (world == null) return;
            event.setRespawnLocation(world.getSpawnLocation());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerChangedWorldEvent(PlayerChangedWorldEvent event) throws SQLException {
        Player player = event.getPlayer();
        if (JailWorld.isPrisoner(player) &&
                player.getWorld() != JailWorld.getJailWorld()) JailWorld.goToJail(player);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (!(sender instanceof Player)) {
            System.out.println("このコマンドはプレイヤー用です");
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("police")) new PoliceCmd(player, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equals("police")) return new Complement(args).police();
        return super.onTabComplete(sender, command, alias, args);
    }
}
