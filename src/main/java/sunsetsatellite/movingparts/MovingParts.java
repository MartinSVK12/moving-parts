package sunsetsatellite.movingparts;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.movingparts.block.BlockFrame;
import sunsetsatellite.movingparts.block.BlockMotor;
import sunsetsatellite.movingparts.block.entity.TileEntityMotor;
import sunsetsatellite.movingparts.entity.ContraptionRenderer;
import sunsetsatellite.movingparts.entity.EntityContraption;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class MovingParts implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "movingparts";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final TomlConfigHandler config;

	private static int startingBlockId = 1400;
	private static int startingItemId = 17300;

	static {
		Toml configToml = new Toml("Moving Parts configuration file.");
		configToml.addCategory("BlockIDs");
		configToml.addCategory("ItemIDs");
		configToml.addCategory("EntityIDs");
		configToml.addCategory("Other");

		configToml.addEntry("EntityIDs.contraption", 60);

		List<Field> blockFields = Arrays.stream(MovingParts.class.getDeclaredFields()).filter((F)-> Block.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
		for (Field blockField : blockFields) {
			configToml.addEntry("BlockIDs."+blockField.getName(), startingBlockId++);
		}
		List<Field> itemFields = Arrays.stream(MovingParts.class.getDeclaredFields()).filter((F)-> Item.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
		for (Field itemField : itemFields) {
			configToml.addEntry("ItemIDs."+itemField.getName(), startingItemId++);
		}

		config = new TomlConfigHandler(MOD_ID,configToml);
	}

	public static final Block motor = new BlockBuilder(MOD_ID)
		.setHardness(1.5f)
		.setResistance(10.0f)
		.setTextures(6,0)
		.setNorthTexture(3,8)
		.build(new BlockMotor("motor",config.getInt("BlockIDs.motor")));

	public static final Block frame = new BlockBuilder(MOD_ID)
		.setHardness(1.5f)
		.setResistance(10.0f)
		.setTextures(11,6)
		.build(new BlockFrame("frame",config.getInt("BlockIDs.frame")));

    @Override
    public void onInitialize() {
		EntityHelper.Core.createTileEntity(TileEntityMotor.class, "motor");
		EntityHelper.Core.createEntity(EntityContraption.class, config.getInt("EntityIDs.contraption"),"contraption");
		EntityHelper.Client.assignEntityRenderer(EntityContraption.class, new ContraptionRenderer());
        LOGGER.info("Moving Parts initialized.");
    }

	public static int round(double n){
		if((n-(int) n) > 0.5){
			return (int) n+1;
		} else {
			return (int) n;
		}
	}

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {

	}
}
