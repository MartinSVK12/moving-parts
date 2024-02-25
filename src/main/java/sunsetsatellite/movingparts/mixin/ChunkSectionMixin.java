package sunsetsatellite.movingparts.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.movingparts.entity.EntityContraption;

import java.util.List;

@Mixin(value = ChunkSection.class, remap = false)
public class ChunkSectionMixin {

	@Shadow
	public List<Entity> entities;

	@Inject(method = "getEntitiesWithin(Lnet/minecraft/core/entity/Entity;Lnet/minecraft/core/util/phys/AABB;Ljava/util/List;)V", at = @At("HEAD"))
	public void getEntitiesWithin(Entity toExclude, AABB aabb, List<Entity> entityList, CallbackInfo ci) {
		for (Entity entity : entities) {
			if(entity instanceof EntityContraption && toExclude != entity){
				EntityContraption contraption = (EntityContraption) entity;
				for (AABB bb : contraption.getBbs()) {
					if (aabb.intersectsWith(bb)) {
						entityList.add(entity);
					}
				}
			}
		}
	}

	@Inject(method = "getEntitiesWithin(Ljava/lang/Class;Lnet/minecraft/core/util/phys/AABB;Ljava/util/List;)V", at = @At("HEAD"))
	public void getEntitiesWithin(Class<? extends Entity> ofClass, AABB aabb, List<Entity> entityList, CallbackInfo ci) {
		for (Entity entity : entities) {
			if(entity instanceof EntityContraption){
				if(ofClass.isInstance(entity)){
					EntityContraption contraption = (EntityContraption) entity;
					for (AABB bb : contraption.getBbs()) {
						if (aabb.intersectsWith(bb)) {
							entityList.add(entity);
						}
					}
				}
			}
		}
	}
}
