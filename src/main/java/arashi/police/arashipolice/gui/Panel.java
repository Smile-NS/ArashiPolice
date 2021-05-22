package arashi.police.arashipolice.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

import static org.bukkit.ChatColor.*;
import static org.bukkit.Material.ARROW;

public abstract class Panel {

    abstract protected Inventory createPanel(Player player) throws Exception;

    abstract public void open(Player player, Player target) throws Exception;
    abstract public void open(Player player) throws Exception;
    abstract public void onClick(InventoryClickEvent event) throws Exception;

    protected void setGuiItems(ItemStack[] itemArray, int[] placements, Inventory inv) {
        for (int i = 0;i < placements.length;i++) {
            ItemStack item = itemArray[placements[i]];
            if (item == null) continue;
            inv.setItem(i, item);
        }
    }

    protected ItemStack createBackIcon() {
        ItemStack back = new ItemStack(ARROW);
        ItemMeta meta = back.getItemMeta();
        assert meta != null;
        meta.setDisplayName(GREEN + "戻る");
        meta.setLore(Collections.singletonList(GRAY + "プレイヤーリストに戻ります"));
        back.setItemMeta(meta);
        return back;
    }
}
