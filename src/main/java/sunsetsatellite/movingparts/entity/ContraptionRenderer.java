package sunsetsatellite.movingparts.entity;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.BlockInstance;

public class ContraptionRenderer extends EntityRenderer<EntityContraption> {
	private final RenderBlocks blockRenderer = new RenderBlocks();

	public ContraptionRenderer() {
		this.shadowSize = 0.01F;
	}
	@Override
	public void doRender(EntityContraption entity, double d, double e, double f, float g, float h) {
		GL11.glPushMatrix();
		this.loadTexture("/terrain.png");
		if(entity.getBlocks() == null) {
			GL11.glPopMatrix();
			return;
		}
		for (BlockInstance block : entity.getBlocks()) {
			GL11.glPushMatrix();
			World world = entity.getWorld();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glTranslated(d+block.offset.x,e+block.offset.y+0.5,f+block.offset.z);
			//GL11.glTranslatef((float)d+(block.pos.x-MathHelper.floor_double(entity.x)), (float)e+(block.pos.y-MathHelper.floor_double(entity.y)), (float)f+(block.pos.z-MathHelper.floor_double(entity.z)));
			this.blockRenderer.renderBlockFallingSand(block.block, world, MathHelper.floor_double(entity.x) + block.offset.x, MathHelper.floor_double(entity.y) + block.offset.y, MathHelper.floor_double(entity.z) + block.offset.z);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}

}
