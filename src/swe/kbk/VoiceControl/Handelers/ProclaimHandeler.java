package swe.kbk.VoiceControl.Handelers;

import java.sql.SQLException;
import java.util.List;

import swe.kbk.VoiceControl.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ProclaimHandeler implements CommandExecutor {

	VoiceControl plug;

	public ProclaimHandeler(VoiceControl plugin) {
		plug = plugin;
		plug.getCommand("proclaim").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		boolean silent = false;
		if (commandLabel.toLowerCase().equals("mumble")) {
			if (checkPermission(sender, "voicecontrol.mumble")) {
				silent = true;
			}
		}

		if(!checkPermission(sender, "voicecontrol.proclaim")) {
			sender.sendMessage("You do not have permission for that!");
			return true;
		}
		List<Switchable> todo = null;
		try {
			todo = plug.getDBHandeler().search(args);
		} catch (SQLException e) {
			plug.getLogger().severe(plug + " SQLException when searching for switchable " + args);
		}

		if (todo == null) {
			sender.sendMessage("Not Switching!");
			return true;
		}

		String mess = "You hear someone proclaiming: " + stringListConcat(args);
		for (Switchable l : todo) {
			if (l != null) {
				if (sender instanceof Player) {
					Location senderLocation = ((Player) sender).getLocation();
					if (!silent) {
						for (Player p : Bukkit.getOnlinePlayers()) {
							if (!sender.equals(p) && inRange(senderLocation, p.getLocation(), l.getRadius())) {
								p.sendMessage(mess);
							}
						}
					}
				}
				if (inRange(sender, l)) {
					sender.sendMessage("Switching!");
					l.setStatus(!l.getStatus());
				}
			}
		}
		return true;
	}

	private String stringListConcat(String[] args) {
		StringBuilder sb = new StringBuilder();
		for (String s : args) {
			sb.append(s);
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * If sender is from console, then it is omnipotent and everywhere at
	 * once It should therefor always be in range
	 * 
	 * If it was not from console but from a player, then the distance
	 * between the Switchable and the player has to be lower than the radius
	 * of the Switchable.
	 * 
	 * If either of these constraints is true, then the sender is considered
	 * in range.
	 * 
	 * @param sender the CommandSender to check.
	 * @param d the Switchable to check.
	 * @return if the CommandSender is in range of the Switchable.
	 */
	public boolean inRange(CommandSender sender, Switchable d) {
		if (sender instanceof ConsoleCommandSender) {
			//sender.sendMessage("[VoiceControl] You are the Console!");
			return true;
		} else if (sender instanceof Player){
			//sender.sendMessage("[VoiceControl] You are a Player!");
			Location switchLoc = d.getLocation();
			Location playLoc = ((Player) sender).getLocation();
			return inRange(switchLoc, playLoc, d.getRadius());
		} 

		return false;

	}

	public boolean inRange(Location loc1, Location loc2, double radius) {
		if (loc1.getWorld().equals(loc2.getWorld())) {
			double dx = loc1.getX()-loc2.getX();
			double dy = loc1.getY()-loc2.getY();
			double dz = loc1.getZ()-loc2.getZ();
			//System.out.println("loc1: " + loc1);
			//System.out.println("loc2: " + loc2);
			return Math.sqrt(dx*dx+dy*dy+dz*dz) <= radius;
		}
		return false;
	}

	public boolean checkPermission(CommandSender sender, String permission) {
		return ((sender instanceof ConsoleCommandSender) || ((sender instanceof Player) && ((Player) sender)
				.hasPermission(permission)));
	}

}
