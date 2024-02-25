package sunsetsatellite.movingparts.entity;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;

import java.util.ArrayList;
import java.util.List;

public class EntityContraption extends Entity {

	private ArrayList<BlockInstance> blocks;
	private final ArrayList<AABB> bbs = new ArrayList<>();
	public boolean moving = false;
	public Direction movementDirection = Direction.Y_POS;

	public EntityContraption(World world) {
		super(world);
		setSize(1f,1f);
		noPhysics = true;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	public ArrayList<AABB> getBbs() {
		return bbs;
	}

	@Override
	public void move(double xd, double yd, double zd) {
		super.move(xd, yd, zd);
		if(y == yo){
			return;
		}
		for (AABB bb : bbs) {
			bb.offset(xd, yd, zd);
		}
	}

	@Override
	public void baseTick() {
		super.baseTick();
		for (AABB aabb : bbs) {
			List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, aabb.expand(0.2, 0.0, 0.2));
			if (list != null && !list.isEmpty()) {
				for (Entity entity : new ArrayList<>(list)) {
					if ((entity instanceof EntityLiving || entity instanceof EntityItem) && moving && entity.yd < 0.1f && movementDirection == Direction.Y_POS) {
						if(entity instanceof EntityItem){
							entity.push(0, 0.1f, 0);
						} else {
							entity.push(0, 0.35f, 0);
						}
					}
				}
			}
		}
	}

	@Override
	public void setPos(double x, double y, double z) {
		super.setPos(x, y, z);
		if(blocks != null){
			bbs.clear();
			for (BlockInstance block : blocks) {
				bbs.add(new AABB(x + block.offset.x, y + block.offset.y, z + block.offset.z, x + block.offset.x + 1, y + block.offset.y + 1, z + block.offset.z + 1));
			}
			for (AABB bb : bbs) {
				bb.offset(-0.5,-0.5,-0.5);
			}
		}
		bb.setBounds(x,y,z,x+1,y+1,z+1);
		bb.offset(-0.5,-0.5,-0.5);
	}

	public float getShadowHeightOffs() {
		return 0.0F;
	}

	public World getWorld() {
		return this.world;
	}

	public boolean isPickable() {
		return !this.removed;
	}

	protected boolean makeStepSound() {
		return false;
	}

	public boolean showBoundingBoxOnHover() {
		return true;
	}

	public void setBlocks(ArrayList<BlockInstance> blocks) {
		this.blocks = blocks;
	}

	public ArrayList<BlockInstance> getBlocks() {
		return blocks;
	}

	@Override
	protected void init() {

	}

	@Override
	public boolean hurt(Entity attacker, int baseDamage, DamageType type) {
		remove();
		return super.hurt(attacker, baseDamage, type);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag) {

	}

	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag) {

	}
}
