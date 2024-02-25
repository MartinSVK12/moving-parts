package sunsetsatellite.movingparts.block.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.movingparts.MovingParts;
import sunsetsatellite.movingparts.block.BlockFrame;
import sunsetsatellite.movingparts.entity.EntityContraption;
import sunsetsatellite.movingparts.util.Network;

import java.util.List;

public class TileEntityMotor extends TileEntity {

	public boolean active = false;
	public EntityContraption collidingContraption;
	public Direction movementDirection = Direction.Y_POS;

	public TileEntityMotor() {

	}

	@Override
	public void tick() {
		super.tick();
		worldObj.markBlocksDirty(x,y,z,x,y,z);
		if(active){
			List<Entity> list = worldObj.getEntitiesWithinAABB(EntityContraption.class, getBlockType().getCollisionBoundingBoxFromPool(worldObj, x, y, z).expand(0.5, 0.5, 0.5));
			if(!list.isEmpty()){
				collidingContraption = (EntityContraption) list.get(0);
			} else {
				if(collidingContraption != null && collidingContraption.getBlocks() != null){
					for (BlockInstance block : collidingContraption.getBlocks()) {
						worldObj.setBlockAndMetadataWithNotify(MathHelper.floor_double(collidingContraption.x + block.offset.x), MovingParts.round(collidingContraption.y + block.offset.y), MathHelper.floor_double(collidingContraption.z + block.offset.z), block.block.id, block.meta);
					}
					collidingContraption.moving = false;
					collidingContraption.movementDirection = movementDirection;
					collidingContraption.remove();
					collidingContraption = null;
				} else {
					int meta = worldObj.getBlockMetadata(x,y,z);
					Direction dir = Direction.getDirectionFromSide(meta);
					if(dir.getBlock(worldObj,this) instanceof BlockFrame){
						activated(worldObj,x,y,z);
					}
				}
			}

			if(collidingContraption != null){
				collidingContraption.moving = true;
				switch (movementDirection){
					case Y_POS:
						collidingContraption.move(0,0.05f,0);
						break;
					case Y_NEG:
						collidingContraption.move(0,-0.05f,0);
						break;
				}
			}
		}
		if(!active && collidingContraption != null && collidingContraption.getBlocks() != null){
			for (BlockInstance block : collidingContraption.getBlocks()) {
				worldObj.setBlockAndMetadataWithNotify(MathHelper.floor_double(collidingContraption.x + block.offset.x), MovingParts.round(collidingContraption.y + block.offset.y), MathHelper.floor_double(collidingContraption.z + block.offset.z), block.block.id, block.meta);
			}
			collidingContraption.moving = false;
			collidingContraption.remove();
			collidingContraption = null;
		}
	}

	public void activated(World world, int x, int y, int z) {
		if(getBlockType() == null){
			return;
		}
		int meta = world.getBlockMetadata(x,y,z);
		Direction dir = Direction.getDirectionFromSide(meta);
		if(dir.getBlock(world,this) instanceof BlockFrame){
			Network net = new Network(this, BlockFrame.class);
			net.reload();
			for (BlockInstance block : net.data) {
				world.setBlockWithNotify(block.pos.x, block.pos.y, block.pos.z, 0);
			}
			EntityContraption contraption = new EntityContraption(world);
			contraption.setBlocks(net.data);
			Vec3i pos = new Vec3i(x,y,z);
			contraption.setPos(pos.x+0.5f,pos.y+0.5f,pos.z+0.5f);
			contraption.setRot(0,0);
			world.entityJoinedWorld(contraption);
			collidingContraption = contraption;
		}
	}
}
