package io.github.smile_ns.arashipolice.gui;

import io.github.smile_ns.arashipolice.JailWorld;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.*;

public class JailPanel extends Panel {

    private static final Map<Inventory, Player> invMap = new HashMap<>();

    @Override
    protected Inventory createPanel(Player player) throws SQLException {
        Inventory jailInv = getServer().createInventory(null, 9,"JAIL");

        String desc = GRAY + "プレイヤーを投獄します";
        String cond = JailWorld.isPrisoner(player) ?
                GRAY + "|| " + GREEN + "既に投獄済み" + GRAY + " ||" : GRAY + "|| 投獄する ||";
        String clickToSwitch = GRAY + "" + UNDERLINE + "クリックで切り替え";

        ItemStack jail = new ItemStack(Material.IRON_BARS);
        ItemMeta meta = jail.getItemMeta();
        assert meta != null;
        meta.setDisplayName(GOLD + "JAIL");
        meta.setLore(Arrays.asList(
            desc, cond, clickToSwitch
        ));
        jail.setItemMeta(meta);

        ItemStack back = createBackIcon();

        ItemStack[] array = {
                null, jail, back
        };
        int[] placement = {
                0, 0, 0, 0, 1, 0, 0, 0, 2
        };

        setGuiItems(array, placement, jailInv);

        return jailInv;
    }

    @Override
    public void open(Player player, Player target) throws SQLException {
        Inventory inv = createPanel(target);
        player.openInventory(inv);
        invMap.put(inv, target);
    }

    @Override
    public void open(Player player) {
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) throws SQLException {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        Inventory inv = player.getOpenInventory().getTopInventory();

        if (!invMap.containsKey(inv)) return;
        event.setCancelled(true);
        if (inv != event.getClickedInventory()) return;

        Player target = invMap.get(inv);

        String cmd;
        switch (slot) {
            case 4:
                cmd = JailWorld.isPrisoner(target) ? "/police jail release " : "/police jail impose ";
                break;
            case 8:
                player.closeInventory();
                new PlayerListPanel().open(player);
                return;
            default:
                return;
        }

        player.closeInventory();
        player.chat(cmd + target.getName());
        open(player, target);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        invMap.remove(inv);
    }
}
