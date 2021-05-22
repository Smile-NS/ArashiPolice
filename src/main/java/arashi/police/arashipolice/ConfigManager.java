package arashi.police.arashipolice;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

public abstract class ConfigManager {

    public static final Plugin PLUGIN = getServer().getPluginManager().getPlugin("ArashiPolice");
    public static FileConfiguration config;

    public static final String DO_NOT_MIND;
    public static final boolean BROADCAST;

    static {
        assert PLUGIN != null;
        config = PLUGIN.getConfig();
        if (!config.contains("do-not-mind"))
            config.set("do-not-mind", "ドンマイ！(*ﾟ∀ﾟﾉﾉ");
        if (!config.contains("broadcast"))
            config.set("broadcast", true);

        DO_NOT_MIND = "\n" + config.getString("do-not-mind");
        BROADCAST = config.getBoolean("broadcast");
    }

    public static void saveConfig() {
        assert PLUGIN != null;
        PLUGIN.saveConfig();
        PLUGIN.reloadConfig();
        config = PLUGIN.getConfig();
    }
}
