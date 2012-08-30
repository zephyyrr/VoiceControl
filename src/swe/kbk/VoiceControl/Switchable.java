/**
 * 
 */
package swe.kbk.VoiceControl;

import org.bukkit.Location;

/**
 * @author johan
 *
 */
public interface Switchable {
	
	boolean getStatus();
	void setStatus(boolean status);
	Location getLocation();
	double getRadius();

}
