package swe.kbk.VoiceControl;

import java.util.List;
/**
 * 
 * @author Zephyyrr
 * @deprecated
 */
public class DataStructure {
	
	String password;
	List<Switchable> switches;
	
	DataStructure(String password, List<Switchable> switches) {
		this.password = password;
		this.switches = switches;
	}
	
	public String getPassword() {
		return password;
	}

	public List<Switchable> getSwitches() {
		return switches;
	}
}
