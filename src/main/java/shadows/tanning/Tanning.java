package shadows.tanning;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@Mod(modid = Tanning.MODID, name = Tanning.MODNAME, version = Tanning.VERSION, acceptableRemoteVersions = "*")
public class Tanning {

	public static final String MODID = "tanning";
	public static final String MODNAME = "Tanning";
	public static final String VERSION = "1.0.0";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static int duration = 500;

	public static final Item TANNED_LEATHER = new Item().setTranslationKey(MODID + ".tanned_leather");
	@ObjectHolder("tanning:tanning_station")
	public static final TannerBlock TANNING_STATION = null;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) throws Exception {
		MinecraftForge.EVENT_BUS.register(this);
		Configuration cfg = new Configuration(e.getSuggestedConfigurationFile());
		duration = cfg.getInt("Tanning Duration", "general", duration, 1, Integer.MAX_VALUE, "The value, in ticks, that it takes to tan a piece of leather.");
		if (cfg.hasChanged()) cfg.save();
	}

	@SubscribeEvent
	public void register(Register<Block> e) {
		Block b = new TannerBlock(Material.WOOD).setRegistryName("tanning_station").setTranslationKey(MODID + ".tanning_station").setHardness(0.5F);
		e.getRegistry().register(b);
		ForgeRegistries.ITEMS.register(new ItemBlock(b).setRegistryName(b.getRegistryName()));
		GameRegistry.registerTileEntity(TannerTileEntity.class, new ResourceLocation(MODID, "tanner_tile"));
		ForgeRegistries.ITEMS.register(TANNED_LEATHER.setRegistryName("tanned_leather"));
	}

}
