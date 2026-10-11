package sulfurcraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import sulfurcraft.ConsumeHandler;
import sulfurcraft.SulfurCraft;

@EventBusSubscriber(
   modid = "sulfurcraft",
   value = {Dist.CLIENT}
)
public class SoundMuffle {
   @SubscribeEvent
   public static void onPlaySound(PlaySoundEvent event) {
      Minecraft mc = Minecraft.m_91087_();
      if (mc.f_91074_ != null && mc.f_91073_ != null) {
         MobEffectInstance kaif = mc.f_91074_.m_21124_((MobEffect)SulfurCraft.KAIF.get());
         if (kaif != null) {
            int dose = ConsumeHandler.decodeDose(kaif.m_19564_());
            float volumeFactor = 1.0F;
            float pitchFactor = 1.0F;
            switch (dose) {
               case 1:
                  pitchFactor = 0.9F;
                  break;
               case 2:
                  pitchFactor = 0.8F;
                  break;
               case 3:
                  volumeFactor = 0.5F;
                  pitchFactor = 0.7F;
                  break;
               case 4:
                  volumeFactor = 0.25F;
                  pitchFactor = 0.6F;
                  break;
               case 5:
                  pitchFactor = 0.5F;
            }

            SoundInstance original = event.getSound();
            if (original != null) {
               if (volumeFactor != 1.0F || pitchFactor != 1.0F) {
                  event.setSound(new SoundMuffle.MuffledSoundInstance(original, volumeFactor, pitchFactor));
               }
            }
         }
      }
   }

   private static final class MuffledSoundInstance implements SoundInstance {
      private final SoundInstance delegate;
      private final float volumeFactor;
      private final float pitchFactor;

      MuffledSoundInstance(SoundInstance delegate, float volumeFactor, float pitchFactor) {
         this.delegate = delegate;
         this.volumeFactor = volumeFactor;
         this.pitchFactor = pitchFactor;
      }

      public ResourceLocation m_7904_() {
         return this.delegate.m_7904_();
      }

      public WeighedSoundEvents m_6775_(SoundManager soundManager) {
         return this.delegate.m_6775_(soundManager);
      }

      public Sound m_5891_() {
         return this.delegate.m_5891_();
      }

      public SoundSource m_8070_() {
         return this.delegate.m_8070_();
      }

      public boolean m_7775_() {
         return this.delegate.m_7775_();
      }

      public boolean m_7796_() {
         return this.delegate.m_7796_();
      }

      public int m_7766_() {
         return this.delegate.m_7766_();
      }

      public float m_7769_() {
         return this.delegate.m_7769_() * this.volumeFactor;
      }

      public float m_7783_() {
         return this.delegate.m_7783_() * this.pitchFactor;
      }

      public double m_7772_() {
         return this.delegate.m_7772_();
      }

      public double m_7780_() {
         return this.delegate.m_7780_();
      }

      public double m_7778_() {
         return this.delegate.m_7778_();
      }

      public SoundInstance.Attenuation m_7438_() {
         return this.delegate.m_7438_();
      }
   }
}
