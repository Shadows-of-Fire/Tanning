	package shadows.tanning;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TannerTileEntity extends TileEntity implements ITickable {

	protected int ticks;

	@Override
	public void update() {
		if (ticks++ >= Tanning.duration) {
			this.world.setBlockState(pos, this.world.getBlockState(pos).withProperty(TannerBlock.TYPE, TannerBlock.Type.TANNED));
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		tag.setInteger("ticks", ticks);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		ticks = tag.getInteger("ticks");
		return super.writeToNBT(tag);
	}

}
