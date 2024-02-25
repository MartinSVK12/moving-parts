package sunsetsatellite.movingparts.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.movingparts.entity.EntityContraption;

import java.util.ArrayList;
import java.util.List;

@Debug(export = true)
@Mixin(value = World.class, remap = false)
public class WorldMixin {

	@Shadow
	@Final
	public ArrayList<AABB> collidingBoundingBoxes;

	@Inject(method = "getCubes", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/Entity;getBb()Lnet/minecraft/core/util/phys/AABB;"))
	public void getCubes(Entity entity, AABB axisalignedbb, CallbackInfoReturnable<List<AABB>> cir, @Local(name = "value") Entity collidingEntity) {
		if(collidingEntity instanceof EntityContraption){
			EntityContraption contraption = (EntityContraption) collidingEntity;
			for (AABB bb : contraption.getBbs()) {
				if (axisalignedbb.intersectsWith(bb.expand(0.1,0.1,0.1))) {
					collidingBoundingBoxes.add(bb);
				}
			}
		}
	}

	/*@Inject(method = "getCollidingSolidBlockBoundingBoxes", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/Entity;getBb()Lnet/minecraft/core/util/phys/AABB;"))
	public void getCollidingSolidBlockBoundingBoxes(Entity entity, AABB axisalignedbb, CallbackInfoReturnable<List<AABB>> cir, @Local List list, @Local(name = "j2") int j2) {
		Entity collidingEntity = (Entity) list.get(j2);
		if(collidingEntity instanceof EntityContraption){
			EntityContraption contraption = (EntityContraption) collidingEntity;
			for (AABB bb : contraption.getBbs()) {
				if (axisalignedbb.intersectsWith(bb.expand(0.1,0.1,0.1))) {
					collidingBoundingBoxes.add(bb);
				}
			}
		}
	}*/
}
