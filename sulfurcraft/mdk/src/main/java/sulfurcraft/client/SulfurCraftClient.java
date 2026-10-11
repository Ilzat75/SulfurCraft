package sulfurcraft.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import sulfurcraft.SulfurCraft;

@EventBusSubscriber(modid = "sulfurcraft", bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class SulfurCraftClient {
   @SubscribeEvent
   public static void onClientSetup(FMLClientSetupEvent event) {
      event.enqueueWork(() -> {
         MenuScreens.m_96206_(SulfurCraft.OIL_PRESS_MENU.get(), OilPressScreen::new);
         MenuScreens.m_96206_(SulfurCraft.FRYER_MENU.get(), FryerScreen::new);
      });
   }
}
