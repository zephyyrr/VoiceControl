/**
 * 
 */
package swe.kbk.VoiceControl.Handelers;

import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import swe.kbk.VoiceControl.VoiceControl;

/**
 * @author johan
 *
 */
public class UnregisterHandeler implements CommandExecutor {

	VoiceControl vc;
	
	/**
	 * 
	 */
	public UnregisterHandeler(VoiceControl plugin) {
		vc = plugin;
		vc.getCommand("voiceunregister").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] args) {
		
		if(!checkPermission(sender, "voicecontrol.unregister")) {
			if (sender instanceof Player) {
				sender.sendMessage("You do not have permission for that!");
			} else {
				sender.sendMessage("Sorry.. Only a player may use this command for now...");
			}
			return true;
		}
		
		Location loc = null;
		if (sender instanceof Player && args.length > 0) {
			Player p = (Player) sender;
			loc = p.getTargetBlock(null, 7).getLocation();
		} else if (sender instanceof Player) {
			sender.sendMessage("Not enought arguments!");
			return false;
		} else {
			sender.sendMessage("Sorry.. Only a player may use this command for now...");
			return true;
		}

		if (args.length > 0 && loc != null) {
			sender.sendMessage("Unregistering switch with password: " + args[0]);
			unregisterSwitch(args[0], loc);
			return true;
		} else if (args.length > 0) {
			sender.sendMessage("I do not know what that is! Make sure you only have air between you and the object.");
			return true;
		}

		sender.sendMessage("End of method!");
		return false;
	}
	
	public void unregisterSwitch(String password, Location loc) {
		if (loc != null) {
			try {
				vc.getDBHandeler().remove(password, loc);
			} catch (SQLException e) {
				vc.getLogger().severe(vc + " SQLException vurring switch " + password + " " + loc);
			}
		}
	}
	
	public boolean checkPermission(CommandSender sender, String permission) {
		return ((sender instanceof Player) && ((Player) sender).hasPermission(permission));
	}

}
