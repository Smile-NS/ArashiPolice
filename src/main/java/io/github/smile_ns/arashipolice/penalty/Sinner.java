package io.github.smile_ns.arashipolice.penalty;

import io.github.smile_ns.arashipolice.Exempt;
import io.github.smile_ns.arashipolice.sqlite.SQLiteValues;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.github.smile_ns.arashipolice.penalty.Penalty.statement;

public class Sinner {

    private static final Map<Player, Sinner> sinnerMap = new HashMap<>();
    private static final String[] penaltyTypes = {
            "move", "chat", "attack", "break", "place", "interact", "jail"
    };

    Set<String> penalties;

    Player player;

    String uuid;

    String ip;

    boolean isExemptIp;

    public Sinner(Player player) throws SQLException {
        this.player = player;
        this.ip = player.getAddress().getHostString();
        this.uuid = player.getUniqueId().toString();
        this.isExemptIp = Exempt.containsIp(ip);

        if (sinnerMap.containsKey(player)) {
            Sinner sinner = sinnerMap.get(player);
            this.penalties = sinner.getPenalties();
        } else {
            this.penalties = getPenaltiesFromSQL();
            if (isSinner()) sinnerMap.put(player, this);
        }
    }

    Sinner(String uuid, String ip) {
        this.uuid = uuid;
        this.ip = ip;
        this.penalties = new HashSet<>();
    }

    public static void clean(Player player) {
        sinnerMap.remove(player);
    }

    private Set<String> getPenaltiesFromSQL() throws SQLException {
        Set<String> set = new HashSet<>();
        for (String type : penaltyTypes) {
            String cmd = isExemptIp ?
                    "SELECT * FROM " + type + " WHERE uuid == '" + uuid + "'" :
                    "SELECT * FROM " + type + " WHERE uuid == '" + uuid + "' or ip == '" + ip + "'";
            ResultSet rs = statement.executeQuery(cmd);
            if (rs.next()) set.add(type);
        }
        return set;
    }

    public Set<String> getPenalties() {
        return penalties;
    }

    public boolean isSinner() {
        return penalties.size() != 0;
    }

    boolean register(String penalty) throws SQLException{
        if (penalties.contains(penalty)) return false;

        SQLiteValues values = new SQLiteValues().set(uuid).set(ip);
        String cmd = "INSERT INTO " + penalty + " VALUES(" + values.toString() + ")";
        statement.executeUpdate(cmd);

        if (player != null) {
            penalties.add(penalty);
            sinnerMap.put(player, this);
        }
        return true;
    }

    private boolean delete(String penalty) throws SQLException{
        if (!penalties.contains(penalty)) return false;

        String cmd = isExemptIp ?
                "DELETE FROM " + penalty + " WHERE uuid == '" + uuid + "'" :
                "DELETE FROM " + penalty + " WHERE uuid == '" + uuid + "' or ip == '" + ip + "'";
        statement.executeUpdate(cmd);
        penalties.remove(penalty);
        sinnerMap.put(player, this);
        return true;
    }

    public void impose() throws SQLException {
        register("jail");
    }

    public void release() throws SQLException {
        delete("jail");
    }

    public boolean forbidMove() throws SQLException {
        return register("move");
    }

    public boolean forbidChat() throws SQLException {
        return register("chat");
    }

    public boolean forbidAttack() throws SQLException {
        return register("attack");
    }

    public boolean forbidBreak() throws SQLException {
        return register("break");
    }

    public boolean forbidPlace() throws SQLException {
        return register("place");
    }

    public boolean forbidInteract() throws SQLException {
        return register("interact");
    }

    public void forbidAll() throws SQLException {
        forbidMove();
        forbidChat();
        forbidAttack();
        forbidBreak();
        forbidPlace();
        forbidInteract();
    }

    public boolean allowMove() throws SQLException {
        return delete("move");
    }

    public boolean allowChat() throws SQLException {
        return delete("chat");
    }

    public boolean allowAttack() throws SQLException {
        return delete("attack");
    }

    public boolean allowBreak() throws SQLException {
        return delete("break");
    }

    public boolean allowPlace() throws SQLException {
        return delete("place");
    }

    public boolean allowInteract() throws SQLException {
        return delete("interact");
    }

    public void allowAll() throws SQLException {
        allowMove();
        allowChat();
        allowAttack();
        allowBreak();
        allowPlace();
        allowInteract();
    }
}
