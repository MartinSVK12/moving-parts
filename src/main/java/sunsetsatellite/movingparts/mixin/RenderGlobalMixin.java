package sunsetsatellite.movingparts.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.movingparts.entity.EntityContraption;

@Mixin(value = RenderGlobal.class, remap = false)
public abstract class RenderGlobalMixin {

	@Shadow
	public abstract void drawOutlinedBoundingBox(AABB axisalignedbb);

	@Inject(method = "drawDebugEntityOutlines", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderGlobal;drawOutlinedBoundingBox(Lnet/minecraft/core/util/phys/AABB;)V", ordinal = 1))
	public void drawDebugEntityOutlines(ICamera camera, float partialTicks, CallbackInfo ci, @Local(name = "entity",ordinal = 0) Entity entity, @Local(ordinal = 0) double offsetX, @Local(ordinal = 1) double offsetY, @Local(ordinal = 2) double offsetZ) {
		if(entity instanceof EntityContraption){
			EntityContraption contraption = (EntityContraption) entity;
			for (AABB bb : contraption.getBbs()) {
				drawOutlinedBoundingBox(bb.getOffsetBoundingBox(-offsetX, -offsetY, -offsetZ));
			}
		}
	}
}
