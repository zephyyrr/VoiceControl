package swe.kbk.VoiceControl;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Lever;

public class LeverSwitch implements Switchable {

	Block block;
	int rad;

	public LeverSwitch(Block lever, int radius) {
		block = lever;
		rad = radius;
	}

	@Override
	public boolean getStatus() {
		return ((Lever) block.getState().getData()).isPowered();
	}

	@Override
	public void setStatus(boolean status) {
		BlockState bs = block.getState();
		BlockFace bf = BlockFace.NORTH;
		if (bs.getData() instanceof Lever) {
			((Lever) bs.getData()).setPowered(status);
			bf = ((Lever) bs.getData()).getAttachedFace();
		}
		bs.update();
		UpdateAttached(bs, bf);
		
	}

	private void UpdateAttached(BlockState bs, BlockFace bf) {
		return;
	}

	@Override
	public Location getLocation() {
		return block.getLocation();
	}

	@Override
	public double getRadius() {
		return rad;
	}
}
