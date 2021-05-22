package arashi.police.arashipolice.exceptions;

import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class PlayerNotFoundException extends Exception{
    private static final long serialVersionUID = 1L;
    private static final String msg = "プレイヤーが見つかりませんでした";

    public PlayerNotFoundException(Player sender){
        super(msg);
        sender.sendMessage(RED + msg);
    }

    public PlayerNotFoundException(){
        super(msg);
    }
}
