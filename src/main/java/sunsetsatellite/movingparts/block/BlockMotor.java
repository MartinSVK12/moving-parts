package sunsetsatellite.movingparts.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.movingparts.block.entity.TileEntityMotor;

import java.util.Random;

public class BlockMotor extends BlockTileEntityRotatable {
	public BlockMotor(String key, int id) {
		super(key, id, Material.metal);
	}

	@Override
	protected TileEntity getNewBlockEntity() {
		return new TileEntityMotor();
	}

	@Override
	public int tickRate() {
		return 1;
	}

	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		/*TileEntityMotor tile = (TileEntityMotor)world.getBlockTileEntity(i, j, k);
		if(world.isBlockIndirectlyGettingPowered(i, j, k))
		{
			boolean previous = tile.active;
			tile.active = true;
			if(previous != tile.active){
				tile.activated(world, i,j,k);
			}
		} else if (!world.isBlockIndirectlyGettingPowered(i, j, k)) {
			tile.active = false;
		}*/
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
	{
		/*if(l > 0 && Block.blocksList[l].canProvidePower())
		{
			boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k);
			boolean flag2 = !world.isBlockIndirectlyGettingPowered(i, j, k) && !world.isBlockIndirectlyGettingPowered(i, j + 1, k);
			if(flag || flag2)
			{
				world.scheduleBlockUpdate(i, j, k, id, tickRate());
			}
		}*/
	}

	@Override
	public boolean isSolidRender() {
		return false;
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		TileEntityMotor tile = (TileEntityMotor)world.getBlockTileEntity(x, y, z);
		if(tile != null){
			if(player.isSneaking()){
				if(tile.movementDirection == Direction.Y_POS){
					tile.movementDirection = Direction.Y_NEG;
					player.addChatMessage("Motor set to move down.");
				} else {
					tile.movementDirection = Direction.Y_POS;
					player.addChatMessage("Motor set to move up.");
				}
            } else {
				tile.active = !tile.active;
            }
            return true;
        }
		return false;
	}

	@Override
	public int getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
		TileEntityMotor tile = (TileEntityMotor)blockAccess.getBlockTileEntity(x, y, z);
		int meta = blockAccess.getBlockMetadata(x,y,z);
		int index;
		int[] orientationLookUpVertical = new int[]{1, 0, 2, 3, 4, 5, /**/ 0, 1, 2, 3, 4, 5};
		if(meta == 0 || meta == 1){
			index = orientationLookUpVertical[6 * meta + side.getId()];
		} else {
			index = Sides.orientationLookUpHorizontal[6 * meta + side.getId()];
			if(tile != null && tile.active && index == Side.NORTH.getId()){
				return BlockMotor.texCoordToIndex(3,9);
			}
		}
		return this.atlasIndices[index];
	}
}
