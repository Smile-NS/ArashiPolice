package arashi.police.arashipolice;

import java.util.ArrayList;
import java.util.List;

public class Exempt extends ConfigManager {

    private static final List<String> ipList;

    static {
        ipList = config.contains("exempt-ip") ?
                config.getStringList("exempt-ip") : new ArrayList<>();
    }

    public static void register(String ip) {
        if (ipList.contains(ip)) return;

        ipList.add(ip);
        config.set("exempt-ip", ipList);
        saveConfig();
    }

    public static void delete(String ip) {
        ipList.remove(ip);
        config.set("exempt-ip", ipList);
        saveConfig();
    }

    public static boolean containsIp(String ip) {
        return ipList.contains(ip);
    }
}
