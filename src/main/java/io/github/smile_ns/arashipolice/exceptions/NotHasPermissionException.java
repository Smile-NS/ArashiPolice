package io.github.smile_ns.arashipolice.exceptions;

import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.RED;

public class NotHasPermissionException extends Exception{
    private static final long serialVersionUID = 1L;
    private static final String msg = "このコマンドは管理者用です";

    public NotHasPermissionException(Player sender){
        super(msg);
        sender.sendMessage(RED + msg);
    }
}
