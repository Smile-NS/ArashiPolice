package io.github.smile_ns.arashipolice.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.*;

public class PlayerListPanel extends Panel {

    private static final Inventory playerListInv = getServer().createInventory(null, 45,"PLAYER LIST");

    private static void setPlayerListPanel() {
        List<Player> playerList = new ArrayList<>(getServer().getOnlinePlayers());
        playerListInv.clear();

        for (int i = 0;i < playerList.size();i++) {
            Player player = playerList.get(i);

            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(GREEN + player.getName());
            meta.setOwningPlayer(player);
            meta.setLore(new ArrayList<>(Arrays.asList(
                    GRAY + "左クリック: ペナルティオプションを開きます",
                    GRAY + "右クリック： 投獄オプションを開きます"
            )));
            item.setItemMeta(meta);

            playerListInv.setItem(i, item);
        }
    }

    @Override
    protected Inventory createPanel(Player player) {
        return null;
    }

    @Override
    public void open(Player player, Player target) {
    }

    @Override
    public void open(Player player) {
        setPlayerListPanel();
        player.openInventory(playerListInv);
    }

    @Override
    public void onClick(InventoryClickEvent event) throws SQLException {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        Inventory inv = player.getOpenInventory().getTopInventory();
        InventoryAction action = event.getAction();

        if (inv != playerListInv) return;
        event.setCancelled(true);
        if (inv != event.getClickedInventory()) return;
        if (slot == -1) return;

        ItemStack item = playerListInv.getItem(slot);
        if (item == null) return;
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        assert meta != null;
        Player owner = (Player) meta.getOwningPlayer();

        if (action == InventoryAction.PICKUP_ALL)
            new PenaltyPanel().open(player, owner);
        else if (action == InventoryAction.PICKUP_HALF)
            new JailPanel().open(player, owner);
    }
}
