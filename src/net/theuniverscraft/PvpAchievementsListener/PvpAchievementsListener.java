package net.theuniverscraft.PvpAchievementsListener;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PvpAchievementsListener extends JavaPlugin {
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new AchievementsListener(), this);
	}
}
