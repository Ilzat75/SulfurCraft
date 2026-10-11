package sulfurcraft.client;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class MachineLoopSound extends AbstractTickableSoundInstance {
   private boolean stopped = false;

   public MachineLoopSound(SoundEvent sound, BlockPos pos) {
      super(sound, SoundSource.BLOCKS, RandomSource.m_216335_(0L));
      this.f_119575_ = pos.m_123341_() + 0.5D;
      this.f_119576_ = pos.m_123342_() + 0.5D;
      this.f_119577_ = pos.m_123343_() + 0.5D;
      this.f_119573_ = 1.0F;
      this.f_119574_ = 1.0F;
      this.f_119578_ = true;
      this.f_119579_ = 0;
      this.f_119580_ = SoundInstance.Attenuation.LINEAR;
      this.f_119582_ = false;
   }

   public boolean m_7801_() {
      if (this.stopped) {
         this.m_119609_();
         return true;
      }
      return false;
   }

   public void m_7788_() {
      this.stopped = true;
   }

   public void stop() {
      this.stopped = true;
   }
}
