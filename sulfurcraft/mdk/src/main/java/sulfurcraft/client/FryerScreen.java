package sulfurcraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import sulfurcraft.AbstractMachineBE;
import sulfurcraft.FryerBlockEntity;
import sulfurcraft.FryerMenu;

public class FryerScreen extends AbstractContainerScreen<FryerMenu> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("sulfurcraft", "textures/gui/container/machine.png");
   private MachineLoopSound loopSound;

   public FryerScreen(FryerMenu menu, Inventory inv, Component title) {
      super(menu, inv, title);
   }

   protected void m_7286_(GuiGraphics g, float partial, int mouseX, int mouseY) {
      int x = this.f_97726_;
      int y = this.f_97727_;
      g.m_280163_(TEXTURE, x, y, 0.0F, 0.0F, 176, 166, 176, 166);
      AbstractMachineBE machine = this.f_97732_.getMachine();
      if (machine.isLit()) {
         g.m_280509_(x + 57, y + 37, x + 69, y + 49, 0xFFFF6600);
      }
      int cook = machine.getCookTime();
      if (cook > 0) {
         int w = cook * 24 / 100;
         g.m_280509_(x + 79, y + 37, x + 79 + w, y + 43, 0xFFFFAA00);
      }
      int oil = ((FryerBlockEntity) machine).getOil();
      if (oil > 0) {
         int max = FryerBlockEntity.getMaxOilStatic();
         int w = oil * 24 / max;
         g.m_280509_(x + 79, y + 52, x + 79 + w, y + 58, 0xFF3399FF);
      }
      this.updateSound(machine);
   }

   private void updateSound(AbstractMachineBE machine) {
      if (machine.isLit()) {
         if (this.loopSound == null) {
            this.loopSound = new MachineLoopSound(machine.getLoopSound(), machine.m_58899_());
            Minecraft.m_91087_().m_91106_().m_120372_(this.loopSound);
         }
      } else if (this.loopSound != null) {
         this.loopSound.stop();
         this.loopSound = null;
      }
   }

   public void m_7379_() {
      super.m_7379_();
      if (this.loopSound != null) {
         this.loopSound.stop();
         this.loopSound = null;
      }
   }
}
