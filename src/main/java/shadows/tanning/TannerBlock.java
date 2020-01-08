package shadows.tanning;

import java.util.List;
import java.util.Locale;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TannerBlock extends BlockHorizontal {

	public static PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);
	public static final AxisAlignedBB AABB_1 = new AxisAlignedBB(1 / 16D, 0, 15 / 16D, 15 / 16D, 14 / 16D, 1);
	public static final AxisAlignedBB AABB_2 = new AxisAlignedBB(0, 0, 1 / 16D, 1 / 16D, 14 / 16D, 15 / 16D);
	public static final AxisAlignedBB AABB_3 = new AxisAlignedBB(1 / 16D, 0, 0, 15 / 16D, 14 / 16D, 1 / 16D);
	public static final AxisAlignedBB AABB_4 = new AxisAlignedBB(15 / 16D, 0, 1 / 16D, 1, 14 / 16D, 15 / 16D);
	public static final AxisAlignedBB[] BBS = { AABB_1, AABB_2, AABB_3, AABB_4 };

	public TannerBlock(Material materialIn) {
		super(materialIn);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return state.getValue(TYPE) == Type.LEATHER;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return hasTileEntity(state) ? new TannerTileEntity() : null;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		Type type = state.getValue(TYPE);
		ItemStack stack = playerIn.getHeldItem(hand);
		if (type == Type.EMPTY && stack.getItem() == Items.LEATHER) {
			if (!playerIn.capabilities.isCreativeMode) playerIn.getHeldItem(hand).shrink(1);
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

	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}

	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 0b11)).withProperty(TYPE, Type.values()[meta >> 2]);
	}

	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex() | state.getValue(TYPE).ordinal() << 2;
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE, FACING);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return BBS[state.getValue(FACING).getHorizontalIndex()];
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BBS[state.getValue(FACING).getHorizontalIndex()];
	}

	@Override
	@Deprecated
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, BBS[state.getValue(FACING).getHorizontalIndex()]);
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
