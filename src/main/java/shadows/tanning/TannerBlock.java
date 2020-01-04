package shadows.tanning;

import java.util.Locale;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TannerBlock extends BlockHorizontal {

	public static PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);

	public TannerBlock(Material materialIn) {
		super(materialIn);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return state.getValue(TYPE) == Type.LEATHER;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		Type type = state.getValue(TYPE);
		ItemStack stack = playerIn.getHeldItem(hand);
		if (type == Type.EMPTY && stack.getItem() == Items.LEATHER) {
			playerIn.getHeldItem(hand).shrink(1);
			worldIn.setBlockState(pos, state.withProperty(TYPE, Type.LEATHER));
			return true;
		} else if (type == Type.TANNED && playerIn.addItemStackToInventory(new ItemStack(Tanning.TANNED_LEATHER))) {
			worldIn.setBlockState(pos, state.withProperty(TYPE, Type.EMPTY));
			return true;
		}

		return false;
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		super.getDrops(drops, world, pos, state, fortune);
		Type type = state.getValue(TYPE);
		if (type == Type.LEATHER) drops.add(new ItemStack(Items.LEATHER));
		else if (type == Type.TANNED) drops.add(new ItemStack(Tanning.TANNED_LEATHER));
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state) {
		return false;
	}

	public static enum Type implements IStringSerializable {
		EMPTY,
		LEATHER,
		TANNED;

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}

}
