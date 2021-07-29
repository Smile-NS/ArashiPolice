package io.github.smile_ns.arashipolice.gui;

import io.github.smile_ns.arashipolice.penalty.Sinner;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.*;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.*;
import static org.bukkit.Material.*;
import static org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES;

public class PenaltyPanel extends Panel {

    private static final Map<Inventory, Player> invMap = new HashMap<>();

    @Override
    protected Inventory createPanel(Player player) throws SQLException {
        Inventory optionInv = getServer().createInventory(null, 9,"OPTION");

        ItemStack move = createIcon(HIDE_ATTRIBUTES, LEATHER_BOOTS, "MOVE", player, "move", "移動を禁止します", "（フライハックも防止できます）");
        ItemStack chat = createIcon(WRITABLE_BOOK, "CHAT", player, "chat", "チャットの送信を禁止します");
        ItemStack attack = createIcon(HIDE_ATTRIBUTES, IRON_SWORD, "ATTACK", player, "attack", "攻撃を禁止します");
        ItemStack blockBreak = createIcon(HIDE_ATTRIBUTES, DIAMOND_PICKAXE, "BREAK", player, "break", "ブロックの破壊を禁止します");
        ItemStack blockPlace = createIcon(GRASS_BLOCK, "PLACE", player, "place", "ブロックの設置を禁止します");
        ItemStack interact = createIcon(OAK_DOOR, "INTERACT", player, "interact",
                "相互作用する行動を禁止します", "（ボタンやドアや感圧版、チェストの開閉などの操作）");
        ItemStack back = createBackIcon();

        ItemStack[] array = {
                null, move, chat, attack, blockBreak, blockPlace, interact, back
        };

        int[] placement = {
          0, 1, 2, 3, 4, 5, 6, 0, 7
        };

        setGuiItems(array, placement, optionInv);

        return optionInv;
    }

    private static ItemStack createIcon(Material type, String title,
                                        Player player, String penalty, String... desc) throws SQLException {
        Sinner sinner = new Sinner(player);
        String enabled = GRAY + "||" + GREEN + " 既に有効 " + GRAY + "||";
        String disabled = GRAY + "|| 有効にする ||";
        String clickToSwitch = GRAY + "" + UNDERLINE + "クリックで切り替え";

        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(GOLD + title);

        List<String> lore = new ArrayList<>();
        for (String str : desc) lore.add(GRAY + str);
        lore.addAll(Arrays.asList(
                "",
                sinner.getPenalties().contains(penalty) ? enabled : disabled,
                clickToSwitch
        ));
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack createIcon(ItemFlag flag, Material type, String title,
                                        Player player, String penalty, String...desc) throws SQLException {
        ItemStack item = createIcon(type, title, player, penalty, desc);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(flag);
        item.setItemMeta(meta);

        return item;
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
        Sinner sinner = new Sinner(target);

        String allow = "/police allow ";
        String forbid = "/police forbid ";
        String cmd;
        switch (slot) {
            case 1:
                cmd = sinner.getPenalties().contains("move") ?
                        allow + "move " : forbid + "move ";
                break;
            case 2:
                cmd = sinner.getPenalties().contains("chat") ?
                        allow + "chat " : forbid + "chat ";
                break;
            case 3:
                cmd = sinner.getPenalties().contains("attack")  ?
                        allow + "attack " : forbid + "attack ";
                break;
            case 4:
                cmd = sinner.getPenalties().contains("break")  ?
                        allow + "break " : forbid + "break ";
                break;
            case 5:
                cmd = sinner.getPenalties().contains("place")  ?
                        allow + "place " : forbid + "place ";
                break;
            case 6:
                cmd = sinner.getPenalties().contains("interact")  ?
                        allow + "interact " : forbid + "interact ";
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
