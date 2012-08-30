package swe.kbk.VoiceControl.Handelers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoiceGetHandeler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		// TODO Implement onCommand for VoiceGetHandeler
		return false;
	}
	
	public boolean checkPermission(CommandSender sender, String permission) {
		return ((sender instanceof Player) && ((Player) sender).hasPermission(permission));
	}

}
