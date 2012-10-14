package swe.kbk.VoiceControl.Handelers;

import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import swe.kbk.VoiceControl.Switchable;
import swe.kbk.VoiceControl.VoiceControl;

public class RegisterHandeler implements CommandExecutor {

	private static final String MAX_WITHOUT_FAR = "20";
	VoiceControl vc;

	public RegisterHandeler(VoiceControl plugin) {
		vc = plugin;
		vc.getCommand("voiceregister").setExecutor(this);
	}

	public void registerSwitch(String password, Switchable sw) {
		if (sw != null) {
			try {
				vc.getDBHandeler().add(password, sw);
			} catch (SQLException e) {
				vc.getLogger().severe(vc + " SQLException regging switch " + password + " " + sw.getLocation());
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {

		if (!checkPermission(sender, "voicecontrol.register")) {
			if (sender instanceof Player) {
				sender.sendMessage("You do not have permission for that!");
			} else {
				sender.sendMessage("Sorry.. Only a player may use this command for now...");
			}
			return true;
		}

		int radius = 5;
		if (args.length > 1) {
			try {
				radius = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				sender.sendMessage("The range you entered was invalid.");
				return false;
			}
		} else {
			sender.sendMessage("You forgot to specify a range.");
			return false;
		}

		if (!checkPermission(sender, "voicecontrol.register.far") && radius > MAX_WITHOUT_FAR) {
			sender.sendMessage("You do not have permission for registering that far!");
			sender.sendMessage("Your registration has been capped at "
					+ MAX_WITHOUT_FAR + " blocks!");
			radius = MAX_WITHOUT_FAR;
		}

		Switchable sw = null;
		if (sender instanceof Player && args.length > 1) {
			Player p = (Player) sender;
			Location loc = p.getTargetBlock(null, 7).getLocation();
			sw = vc.getDBHandeler().getSwitchable(radius, loc.getX(),
					loc.getY(), loc.getZ(), loc.getWorld().getName());
		} else if (sender instanceof Player) {
			sender.sendMessage("Not enought arguments!");
			return false;
		} else {
			sender.sendMessage("Sorry.. Only a player may use this command for now...");
			return true;
		}

		if (args.length > 0 && sw != null) {
			sender.sendMessage("Registering new switch with password: "
					+ args[0]);
			registerSwitch(args[0], sw);
			return true;
		} else if (args.length > 0) {
			sender.sendMessage("Can't switch that! Make sure you only have air between you and the object.");
			return true;
		}

		sender.sendMessage("End of method!");
		return false;
	}

	public boolean checkPermission(CommandSender sender, String permission) {
		return ((sender instanceof Player) && ((Player) sender)
				.hasPermission(permission));
	}
}
