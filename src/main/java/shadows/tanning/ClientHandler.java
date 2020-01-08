package shadows.tanning;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Tanning.MODID, value = Side.CLIENT)
public class ClientHandler {

	@SubscribeEvent
	public static void models(ModelRegistryEvent e) {
		ModelLoader.setCustomModelResourceLocation(Tanning.TANNED_LEATHER, 0, new ModelResourceLocation(Tanning.TANNED_LEATHER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Tanning.TANNING_STATION), 0, new ModelResourceLocation(Tanning.TANNING_STATION.getRegistryName(), "normal"));
	}

}
