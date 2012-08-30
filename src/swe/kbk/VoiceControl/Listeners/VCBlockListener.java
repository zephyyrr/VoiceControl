package swe.kbk.VoiceControl.Listeners;

import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import swe.kbk.VoiceControl.VoiceControl;

public class VCBlockListener implements Listener {
	
	private VoiceControl plugin;

	public VCBlockListener(VoiceControl plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler // EventPriority.NORMAL by default
	public void onBlockBreak(BlockBreakEvent event) {
		Location loc = event.getBlock().getLocation();
		try {
			plugin.getDBHandeler().remove(loc);
		} catch (SQLException e) {
			//plugin.getLogger().severe(plugin + " SQLException when removing block " + loc);
		}
	}

}
