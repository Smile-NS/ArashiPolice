package io.github.smile_ns.arashipolice.commands;

import io.github.smile_ns.arashipolice.Exempt;
import io.github.smile_ns.arashipolice.JailWorld;
import io.github.smile_ns.arashipolice.exceptions.IllegalArgsException;
import io.github.smile_ns.arashipolice.exceptions.NotHasPermissionException;
import io.github.smile_ns.arashipolice.exceptions.PlayerNotFoundException;
import io.github.smile_ns.arashipolice.gui.PlayerListPanel;
import io.github.smile_ns.arashipolice.penalty.Penalty;
import io.github.smile_ns.arashipolice.penalty.Sinner;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.*;

public class PoliceCmd implements CommandExecutor, TabCompleter {

    private Player sender;
    private String[] cmdArgs;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("このコマンドはプレイヤー用です");
            return true;
        }
        this.sender = (Player) sender;
        this.cmdArgs = args;

        try {
            select();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equals("police")) return null;
        List<String> first = new ArrayList<>(Arrays.asList("forbid", "allow", "jail", "exempt-ip"));
        List<String> penalty = new ArrayList<>(Arrays.asList("move", "chat", "attack", "break", "place", "interact", "all"));
        List<String> jail = new ArrayList<>(Arrays.asList("release", "impose", "world"));
        List<String> exempt = new ArrayList<>(Arrays.asList("register", "delete"));

        if (args.length == 1 ) {
            if (args[0].length() == 0) return first;
            else return expansionArg(0, first, args);
        }

        if (args.length == 2 ) {

            switch (args[0]) {
                case "forbid":
                case "allow":
                    if (args[1].length() == 0) return penalty;
                    else return expansionArg(1, penalty, args);
                case "jail":
                    if (args[1].length() == 0) return jail;
                    else return expansionArg(1, jail, args);
                case "exempt-ip":
                    if (args[1].length() == 0) return exempt;
                    else return expansionArg(1, exempt, args);
            }
        }
        return null;
    }

    private List<String> expansionArg(int index, List<String> list, String[] args){
        for (String str : list){
            if (str.startsWith(args[index])) return Collections.singletonList(str);
        }
        return null;
    }

    private void select() throws Exception {
        if (!sender.hasPermission("police")) throw new NotHasPermissionException(sender);
        if (cmdArgs.length == 0) {
            new PlayerListPanel().open(sender);
            return;
        }

        switch (cmdArgs[0]) {
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
        if (cmdArgs.length != 3) throw new IllegalArgsException(sender);

        Player player = getServer().getPlayerExact(cmdArgs[2]);
        if (player == null) throw new PlayerNotFoundException(sender);

        Sinner sinner = new Sinner(player);
        String forbidden = RED + "既に禁止されています";
        switch (cmdArgs[1]) {
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
        if (cmdArgs.length != 3) throw new IllegalArgsException(sender);

        Player player = getServer().getPlayerExact(cmdArgs[2]);
        if (player == null) throw new PlayerNotFoundException(sender);

        Sinner sinner = new Sinner(player);
        String notForbidden = RED + "禁止されていません";
        switch (cmdArgs[1]) {
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
        if (cmdArgs.length != 3) throw new IllegalArgsException(sender);

        Player player;
        switch (cmdArgs[1]) {
            case "release":
                player = getServer().getPlayerExact(cmdArgs[2]);
                if (player == null) throw new PlayerNotFoundException(sender);
                releaseJail(player);
                break;
            case "impose":
                player = getServer().getPlayerExact(cmdArgs[2]);
                if (player == null) throw new PlayerNotFoundException(sender);
                imposeJail(player);
                break;
            case "world":
                World world = getServer().getWorld(cmdArgs[2]);
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
        if (cmdArgs.length != 3) throw new IllegalArgsException(sender);

        String ip = cmdArgs[2];
        switch (cmdArgs[1]) {
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
