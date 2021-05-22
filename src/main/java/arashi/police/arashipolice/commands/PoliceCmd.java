package arashi.police.arashipolice.commands;

import arashi.police.arashipolice.Exempt;
import arashi.police.arashipolice.JailWorld;
import arashi.police.arashipolice.exceptions.IllegalArgsException;
import arashi.police.arashipolice.exceptions.NotHasPermissionException;
import arashi.police.arashipolice.exceptions.PlayerNotFoundException;
import arashi.police.arashipolice.gui.PlayerListPanel;
import arashi.police.arashipolice.penalty.Penalty;
import arashi.police.arashipolice.penalty.Sinner;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.*;

public class PoliceCmd {

    private final Player sender;
    private final String[] args;

    public PoliceCmd(Player sender, String[] args) {
        this.sender = sender;
        this.args = args;

        try {
            select();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void select() throws Exception {
        if (!sender.hasPermission("police")) throw new NotHasPermissionException(sender);
        if (args.length == 0) {
            new PlayerListPanel().open(sender);
            return;
        }

        switch (args[0]) {
            case "forbid":
                forbid();
                break;
            case "allow":
                allow();
                break;
            case "jail":
                jail();
                break;
            case "exempt-ip":
                exempt();
                break;
            default:
                throw new IllegalArgsException(sender);
        }
    }

    private void forbid() throws Exception {
        if (args.length != 3) throw new IllegalArgsException(sender);

        Player player = getServer().getPlayerExact(args[2]);
        if (player == null) throw new PlayerNotFoundException(sender);

        Sinner sinner = new Sinner(player);
        String forbidden = RED + "既に禁止されています";
        switch (args[1]) {
            case "move":
                if (sinner.forbidMove())
                    Penalty.log(sender, player.getName() + " が移動することを禁止しました");
                else sender.sendMessage(forbidden);
                break;
            case "chat":
                if (sinner.forbidChat())
                    Penalty.log(sender, player.getName() + " がメッセージを送信することを禁止しました");
                else sender.sendMessage(forbidden);
                break;
            case "attack":
                if (sinner.forbidAttack())
                    Penalty.log(sender, player.getName() + " が攻撃することを禁止しました");
                else sender.sendMessage(forbidden);
                break;
            case "break":
                if (sinner.forbidBreak())
                    Penalty.log(sender, player.getName() + " がブロックを破壊することを禁止しました");
                else sender.sendMessage(forbidden);
                break;
            case "place":
                if (sinner.forbidPlace())
                    Penalty.log(sender, player.getName() + " がブロックを設置することを禁止しました");
                else sender.sendMessage(forbidden);
                break;
            case "interact":
                if (sinner.forbidInteract())
                    Penalty.log(sender, player.getName() + " の相互作用する行動を禁止しました");
                else sender.sendMessage(forbidden);
                break;
            case "all":
                sinner.forbidAll();
                Penalty.log(sender, player.getName() + " の全ての行動を禁止しました");
                break;
            default:
                throw new IllegalArgsException(sender);
        }
        Penalty.save();
    }

    private void allow() throws Exception {
        if (args.length != 3) throw new IllegalArgsException(sender);

        Player player = getServer().getPlayerExact(args[2]);
        if (player == null) throw new PlayerNotFoundException(sender);

        Sinner sinner = new Sinner(player);
        String notForbidden = RED + "禁止されていません";
        switch (args[1]) {
            case "move":
                if (sinner.allowMove())
                    Penalty.log(sender, player.getName() + " が移動することを許可しました");
                else sender.sendMessage(notForbidden);
                break;
            case "chat":
                if (sinner.allowChat())
                    Penalty.log(sender, player.getName() + " がメッセージを送信することを許可しました");
                else sender.sendMessage(notForbidden);
                break;
            case "attack":
                if (sinner.allowAttack())
                    Penalty.log(sender, player.getName() + " が攻撃することを許可しました");
                else sender.sendMessage(notForbidden);
                break;
            case "break":
                if (sinner.allowBreak())
                    Penalty.log(sender, player.getName() + " がブロックを破壊することを許可しました");
                else sender.sendMessage(notForbidden);
                break;
            case "place":
                if (sinner.allowPlace())
                    Penalty.log(sender, player.getName() + " がブロックを設置することを許可しました");
                else sender.sendMessage(notForbidden);
                break;
            case "interact":
                if (sinner.allowInteract())
                    Penalty.log(sender, player.getName() + " の相互作用する行動を許可しました");
                else sender.sendMessage(notForbidden);
                break;
            case "all":
                sinner.allowAll();
                Penalty.log(sender, player.getName() + " の全ての行動を許可しました");
                break;
            default:
                throw new IllegalArgsException(sender);
        }
        Penalty.save();
    }

    private void jail() throws Exception {
        if (args.length != 3) throw new IllegalArgsException(sender);

        Player player;
        switch (args[1]) {
            case "release":
                player = getServer().getPlayerExact(args[2]);
                if (player == null) throw new PlayerNotFoundException(sender);
                releaseJail(player);
                break;
            case "impose":
                player = getServer().getPlayerExact(args[2]);
                if (player == null) throw new PlayerNotFoundException(sender);
                imposeJail(player);
                break;
            case "world":
                World world = getServer().getWorld(args[2]);
                if (world == null) {
                    sender.sendMessage(RED + "ワールドが見つかりませんでした");
                    return;
                }
                JailWorld.recordWorld(world);
                sender.sendMessage(GRAY + world.getName() + " をJAILワールドとして登録しました");
                break;
            default:
                throw new IllegalArgsException(sender);
        }
    }

    private void releaseJail(Player player) throws Exception {
        if (JailWorld.getJailWorld() == null) {
            sender.sendMessage(RED + "JAILワールドが登録されていません");
            return;
        }
        String playerName = player.getName();
        if (!JailWorld.isPrisoner(player)) {
            sender.sendMessage(RED + playerName + " は囚人ではありません");
            return;
        }
        JailWorld.removePlayer(player);
        Location loc = getServer().getWorld("world").getSpawnLocation();
        player.teleport(loc);

        Penalty.log(sender, player.getName() + " を解放しました");
    }

    private void imposeJail(Player player) throws Exception {
        if (JailWorld.getJailWorld() == null) {
            sender.sendMessage(RED + "JAILワールドが登録されていません");
            return;
        }
        JailWorld.addPlayer(player);
        JailWorld.goToJail(player);

        Penalty.log(sender, player.getName() + " を投獄しました");
    }

    private void exempt() throws Exception {
        if (args.length != 3) throw new IllegalArgsException(sender);

        String ip = args[2];
        switch (args[1]) {
            case "register":
                if (Exempt.containsIp(ip)) {
                    sender.sendMessage(RED+ "既に登録されています");
                    return;
                }
                Exempt.register(ip);
                sender.sendMessage(GRAY + "登録しました");
                break;
            case "delete":
                if (!Exempt.containsIp(ip)) {
                    sender.sendMessage(RED + "登録されていません");
                    return;
                }
                Exempt.delete(ip);
                sender.sendMessage(GRAY + "削除しました");
                break;
            default:
                throw new IllegalArgsException(sender);
        }
    }
}
