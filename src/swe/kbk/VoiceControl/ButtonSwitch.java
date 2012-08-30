package swe.kbk.VoiceControl;

//import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Button;

public class ButtonSwitch implements Switchable, Runnable{

	transient Block b;
	int rad;

	public ButtonSwitch(Block button, int radius) {
		b = button;
		rad = radius;
	}

	@Override
	public boolean getStatus() {
		return (b.getState() instanceof Button) ? ((Button) b.getState().getData()).isPowered() : false;
	}

	@Override
	public void setStatus(boolean status) {
		BlockState bs = b.getState();
		if (bs.getData() instanceof Button) {
			((Button) bs.getData()).setPowered(status);
			//Bukkit.getScheduler().scheduleAsyncDelayedTask(VoiceControl.plugin, this, 60L);
		}
		bs.update();

	}

	@Override
	public Location getLocation() {
		return b.getLocation();
	}

	@Override
	public double getRadius() {
		return rad;
	}

	@Override
	public void run() {
		setStatus(false);
	}
}
