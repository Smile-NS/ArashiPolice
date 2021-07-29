package io.github.smile_ns.arashipolice;

import io.github.smile_ns.arashipolice.penalty.Sinner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.sql.SQLException;

import static io.github.smile_ns.arashipolice.ConfigManager.DO_NOT_MIND;
import static org.bukkit.ChatColor.*;

public class ArashiActionListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void move(PlayerMoveEvent event) throws SQLException {
        Player player = event.getPlayer();
        Sinner sinner = new Sinner(player);

        if (!sinner.isSinner()) return;
        if (sinner.getPenalties().contains("move")) {
            event.setCancelled(true);
            player.sendMessage(RED + "あなたが移動することは禁止されています" + DO_NOT_MIND);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void move(PlayerToggleSneakEvent event) throws SQLException {
        Player player = event.getPlayer();
        Sinner sinner = new Sinner(player);

        if (!sinner.isSinner()) return;
        if (sinner.getPenalties().contains("move")) {
            event.setCancelled(true);
            player.setSneaking(false);
            player.sendMessage(RED + "あなたが移動することは禁止されています" + DO_NOT_MIND);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void chat(AsyncPlayerChatEvent event) throws SQLException {
        Player player = event.getPlayer();
        Sinner sinner = new Sinner(player);

        if (!sinner.isSinner()) return;
        if (sinner.getPenalties().contains("chat")) {
            event.setCancelled(true);
            player.sendMessage(RED + "あなたがメッセージを送信することは禁止されています" + DO_NOT_MIND);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void attack(EntityDamageByEntityEvent event) throws SQLException {
        Entity entity = event.getDamager();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;
        Sinner sinner = new Sinner(player);

        if (!sinner.isSinner()) return;
        if (sinner.getPenalties().contains("attack")) {
            event.setCancelled(true);
            player.sendMessage(RED + "あなたが攻撃することは禁止されています" + DO_NOT_MIND);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockBreak(BlockBreakEvent event) throws SQLException {
        Player player = event.getPlayer();
        Sinner sinner = new Sinner(player);

        if (!sinner.isSinner()) return;
        if (sinner.getPenalties().contains("break")) {
            event.setCancelled(true);
            player.sendMessage(RED + "あなたがブロックを破壊することは禁止されています" + DO_NOT_MIND);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockPlace(BlockPlaceEvent event) throws SQLException {
        Player player = event.getPlayer();
        Sinner sinner = new Sinner(player);

        if (!sinner.isSinner()) return;
        if (sinner.getPenalties().contains("place")) {
            event.setCancelled(true);
            player.sendMessage(RED + "あなたがブロックを設置することは禁止されています" + DO_NOT_MIND);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void interact(PlayerInteractEvent event) throws SQLException {
        Player player = event.getPlayer();
        Sinner sinner = new Sinner(player);

        if (!sinner.isSinner()) return;
        if (sinner.getPenalties().contains("interact")) {
            event.setCancelled(true);
            player.sendMessage(RED + "あなたの相互作用する行動は禁止されています" + DO_NOT_MIND);
        }
    }
}
