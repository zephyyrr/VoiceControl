package swe.kbk.VoiceControl;

import java.sql.SQLException;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import swe.kbk.VoiceControl.Handelers.RegisterHandeler;
import swe.kbk.VoiceControl.Handelers.UnregisterHandeler;
import swe.kbk.VoiceControl.Handelers.ProclaimHandeler;
import swe.kbk.VoiceControl.Listeners.VCBlockListener;

/**
 * 
 * 
 * 
 * @author Johan "Zephyyrr" Fogelström
 * 
 */
public class VoiceControl extends JavaPlugin {

	DBHandeler dbh;
	//public static VoiceControl plugin;

	public VoiceControl() {
		try {
			dbh = new DBHandeler(this);
			dbh.SetupDB();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			getLogger().severe(this + " SQLException when setting up DB");
		}
	}

	public void onEnable() {
		new ProclaimHandeler(this);
		new RegisterHandeler(this);
		new UnregisterHandeler(this);
		new VCBlockListener(this);
		log.info("VoiceControl loaded!");

	}

	public void onDisable() {

		getCommand("proclaim").setExecutor(null);
		getCommand("voiceregister").setExecutor(null);
		getCommand("voiceunregister").setExecutor(null);
		dbh.Close();

	}
	
	public String toString() {
		return "[VoiceControl]";
	}

	public DBHandeler getDBHandeler() {
		return dbh;
	}
}
