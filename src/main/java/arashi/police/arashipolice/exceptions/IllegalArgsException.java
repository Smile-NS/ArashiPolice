package arashi.police.arashipolice.exceptions;

import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.RED;

public class IllegalArgsException extends Exception{

    private static final long serialVersionUID = 1L;
    private static final String msg = "不正な引数です";

    public IllegalArgsException(Player sender){
        super(msg);
        sender.sendMessage(RED + msg);
    }
}
