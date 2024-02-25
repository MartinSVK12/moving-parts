package sunsetsatellite.movingparts;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Override
    public void onInitialize() {
        LOGGER.info("Moving Parts initialized.");
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
