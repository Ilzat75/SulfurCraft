package sulfurcraft;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(
   modid = "sulfurcraft"
)
public class ConsumeHandler {
   private static final int NAUSEA_TICKS = 200;
   private static final int POISON_SHORT = 200;
   private static final int POISON_MID = 400;
   private static final int BLINDNESS_TICKS = 600;
   private static final int[] KAIF_DURATION = new int[]{3600, 12000, 36000, 54000, 72000};
   private static final int[] HANGOVER_DURATION = new int[]{2400, 6000, 12000, 18000, Integer.MAX_VALUE};
   private static final int BEER_TICKS = 4800;
   private static final int BEER_NAUSEA_TICKS = 1200;
   private static final int BEER_STRENGTH_LONG_TICKS = 9600;
   private static final int BEER_EXTRA_POISON_TICKS = 60;
   private static final String KEY_PILLS_DOSE = "sulfurcraft:pills_dose";
   private static final String KEY_BEER_BOTTLES = "sulfurcraft:beer_bottles";

   @SubscribeEvent
   public static void onUseFinish(LivingEntityUseItemEvent.Finish event) {
      LivingEntity entity = event.getEntity();
      if (entity instanceof Player player) {
         ItemStack stack = event.getItem();
         if (!stack.m_41619_()) {
            if (stack.m_150930_((Item)SulfurCraft.HAPPY_PILLS.get())) {
               if (!player.m_9236_().f_46443_) {
                  handlePills(player);
               }
            } else if (stack.m_150930_((Item)SulfurCraft.BEER.get())) {
               if (!player.m_9236_().f_46443_) {
                  handleBeer(player);
               }
            } else if (stack.m_150930_(Items.f_42455_) && !player.m_9236_().f_46443_) {
               handleMilk(player);
            }

         }
      }
   }

   @SubscribeEvent
   public static void onEffectExpired(MobEffectEvent.Expired event) {
      LivingEntity entity = event.getEntity();
      if (entity instanceof Player player) {
         if (!player.m_9236_().f_46443_) {
            MobEffectInstance inst = event.getEffectInstance();
            if (inst != null && inst.m_19544_() == SulfurCraft.KAIF.get()) {
               applyHangover(player);
            }

         }
      }
   }

   private static void handlePills(Player player) {
      int count = (int)player.getPersistentData().m_128454_("sulfurcraft:pills_dose") + 1;
      if (count >= 6) {
         player.getPersistentData().m_128473_("sulfurcraft:pills_dose");
         player.m_6074_();
      } else {
         player.getPersistentData().m_128356_("sulfurcraft:pills_dose", (long)count);
         int duration = KAIF_DURATION[count - 1];
         player.m_7292_(new MobEffectInstance((MobEffect)SulfurCraft.KAIF.get(), duration, count, false, true, true));
         switch (count) {
            case 1:
               player.m_7292_(doseEffect(MobEffects.f_19604_, 200));
               break;
            case 2:
               player.m_7292_(doseEffect(MobEffects.f_19604_, 200));
               break;
            case 3:
               player.m_7292_(doseEffect(MobEffects.f_19614_, 200));
               break;
            case 4:
               player.m_7292_(doseEffect(MobEffects.f_19614_, 400));
               break;
            case 5:
               player.m_7292_(doseEffect(MobEffects.f_19614_, 200));
               player.m_7292_(doseEffect(MobEffects.f_19610_, 600));
         }

      }
   }

   private static MobEffectInstance doseEffect(MobEffect effect, int ticks) {
      return new MobEffectInstance(effect, ticks, 0, false, true, true);
   }

   private static void applyHangover(Player player) {
      int count = (int)player.getPersistentData().m_128454_("sulfurcraft:pills_dose");
      if (count >= 1 && count <= 5) {
         int duration = count >= 5 ? HANGOVER_DURATION[4] : HANGOVER_DURATION[count - 1];
         player.m_7292_(new MobEffectInstance(MobEffects.f_19613_, duration, 0, false, true, true));
         player.m_7292_(new MobEffectInstance(MobEffects.f_19612_, duration, 0, false, true, true));
         player.getPersistentData().m_128473_("sulfurcraft:pills_dose");
      } else {
         player.getPersistentData().m_128473_("sulfurcraft:pills_dose");
      }
   }

   private static void handleMilk(Player player) {
      int count = (int)player.getPersistentData().m_128454_("sulfurcraft:pills_dose");
      player.getPersistentData().m_128473_("sulfurcraft:pills_dose");
      player.getPersistentData().m_128473_("sulfurcraft:beer_bottles");
      if (count >= 1) {
         if (player.m_9236_().m_7654_() != null) {
            player.m_9236_().m_7654_().execute(() -> {
               if (player.m_6084_() && player.m_9236_() instanceof ServerLevel) {
                  int duration = count >= 5 ? HANGOVER_DURATION[4] : HANGOVER_DURATION[count - 1];
                  player.m_7292_(new MobEffectInstance(MobEffects.f_19613_, duration, 0, false, true, true));
                  player.m_7292_(new MobEffectInstance(MobEffects.f_19612_, duration, 0, false, true, true));
               }

            });
         }

      }
   }

   public static int decodeDose(int amp) {
      return amp & 7;
   }

   private static void handleBeer(Player player) {
      MobEffectInstance existingStr = player.m_21124_(MobEffects.f_19600_);
      int bottle;
      if (existingStr != null) {
         int prev = (int)player.getPersistentData().m_128454_("sulfurcraft:beer_bottles");
         bottle = prev + 1;
      } else {
         bottle = 1;
         player.getPersistentData().m_128356_("sulfurcraft:beer_bottles", 0L);
      }

      player.getPersistentData().m_128356_("sulfurcraft:beer_bottles", (long)bottle);
      long nauseaLong = bottle - 1 >= 40 ? 2147483647L : (100L << (bottle - 1)) * 5L / 8L;
      int nauseaTicks = nauseaLong > 2147483647L ? 2147483647 : (int)nauseaLong;
      long strengthLong = bottle - 1 >= 40 ? 2147483647L : 140L << (bottle - 1);
      int strengthTicks = strengthLong > 2147483647L ? 2147483647 : (int)strengthLong;
      player.m_7292_(new MobEffectInstance(MobEffects.f_19604_, nauseaTicks, 0, true, true, true));
      player.m_7292_(new MobEffectInstance(MobEffects.f_19600_, strengthTicks, 0, true, true, true));
      if (bottle >= 4) {
         player.m_7292_(new MobEffectInstance(MobEffects.f_19614_, 60 * (bottle - 3), 0, true, true, true));
      }

   }

   @ParametersAreNonnullByDefault
   public static final class BeerBrewingRecipe extends BrewingRecipe {
      private static final ItemStack WATER_POTION = new ItemStack(Items.f_42589_);

      public BeerBrewingRecipe() {
         super(Ingredient.m_43927_(new ItemStack[]{new ItemStack(Items.f_42589_)}), Ingredient.m_43929_(new ItemLike[]{(ItemLike)SulfurCraft.YEAST.get()}), new ItemStack((ItemLike)SulfurCraft.BEER.get()));
      }

      public boolean isInput(@Nullable ItemStack stack) {
         return stack != null && stack.m_150930_(Items.f_42589_) && "minecraft:water".equals(stack.m_41784_().m_128461_("Potion"));
      }

      public Ingredient getInput() {
         return Ingredient.m_43927_(new ItemStack[]{WATER_POTION});
      }

      static {
         WATER_POTION.m_41784_().m_128359_("Potion", "minecraft:water");
      }
   }
}
