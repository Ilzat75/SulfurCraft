package sulfurcraft;

import java.nio.file.Path;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.Pack.Position;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

@Mod("sulfurcraft")
public class SulfurCraft {
   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
   public static final String MODID = "sulfurcraft";
   public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "sulfurcraft");
   public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "sulfurcraft");
   public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "sulfurcraft");
   public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "sulfurcraft");
   public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "sulfurcraft");
   public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "sulfurcraft");
   public static final IntegerProperty SUNFLOWER_AGE = IntegerProperty.m_61631_("age", 0, 3);
   public static final RegistryObject<Block> FRYER = BLOCKS.register("fryer", () -> new MachineBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.m_284310_().m_60955_().m_60913_(3.5F, 6.0F).m_60918_(SoundType.f_56743_).m_60999_(), (pos, state) -> new FryerBlockEntity(pos, state)));
   public static final RegistryObject<Block> OIL_PRESS = BLOCKS.register("oil_press", () -> new MachineBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.m_284310_().m_60955_().m_60913_(3.5F, 6.0F).m_60918_(SoundType.f_56743_).m_60999_(), (pos, state) -> new OilPressBlockEntity(pos, state)));
   public static final RegistryObject<Block> SUNFLOWER_CROP = BLOCKS.register("sunflower_crop", () -> new SunflowerCropBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.m_284310_().m_60910_().m_60955_().m_60966_().m_60977_().m_60918_(SoundType.f_56758_)));
   public static final RegistryObject<Item> FRYER_ITEM = ITEMS.register("fryer", () -> new BlockItem(FRYER.get(), new Item.Properties()));
   public static final RegistryObject<Item> OIL_PRESS_ITEM = ITEMS.register("oil_press", () -> new BlockItem(OIL_PRESS.get(), new Item.Properties()));
   public static final RegistryObject<Item> SUNFLOWER_SEEDS = ITEMS.register("sunflower_seeds", () -> new ItemNameBlockItem(SUNFLOWER_CROP.get(), new Item.Properties().m_41489_(new FoodProperties.Builder().m_38760_(1).m_38758_(0.1F).m_38767_())));
   public static final RegistryObject<Item> SULFUR_CRYSTAL = ITEMS.register("sulfur_crystal", () -> new Item(new Item.Properties()));
   public static final RegistryObject<Item> SALT = ITEMS.register("salt", () -> new Item(new Item.Properties()));
   public static final RegistryObject<Item> YEAST = ITEMS.register("yeast", () -> new Item(new Item.Properties()));
   public static final RegistryObject<Item> HAPPY_PILLS = ITEMS.register("happy_pills", () -> new Item(new Item.Properties()) {
          public int m_8105_(ItemStack stack) {
             return 32;
          }

          public UseAnim m_6164_(ItemStack stack) {
             return UseAnim.EAT;
          }

          public InteractionResultHolder<ItemStack> m_7203_(Level level, Player player, InteractionHand hand) {
             player.m_6672_(hand);
             return InteractionResultHolder.m_19096_(player.m_21120_(hand));
          }

          public SoundEvent m_6061_() {
             return SoundEvents.f_11912_;
          }

          public ItemStack m_5922_(ItemStack stack, Level level, LivingEntity entity) {
             if (entity instanceof Player player) {
                if (player.m_150110_().f_35937_) {
                   return stack;
                }
             }

             stack.m_41774_(1);
             return stack;
          }
       });
   public static final RegistryObject<Item> BEER = ITEMS.register("beer", () -> new Item(new Item.Properties()) {
          public int m_8105_(ItemStack stack) {
             return 32;
          }

          public UseAnim m_6164_(ItemStack stack) {
             return UseAnim.DRINK;
          }

          public InteractionResultHolder<ItemStack> m_7203_(Level level, Player player, InteractionHand hand) {
             player.m_6672_(hand);
             return InteractionResultHolder.m_19096_(player.m_21120_(hand));
          }

          @Nullable
          public SoundEvent m_6023_() {
             return SoundEvents.f_11911_;
          }

          @Nullable
          public SoundEvent m_6061_() {
             return SoundEvents.f_11911_;
          }

          public ItemStack m_5922_(ItemStack stack, Level level, LivingEntity entity) {
             if (entity instanceof Player player) {
                if (player.m_150110_().f_35937_) {
                   return stack;
                }
             }

             stack.m_41774_(1);
             return stack;
          }
       });
   public static final RegistryObject<Item> CHEESE = ITEMS.register("cheese", () -> new Item(new Item.Properties().m_41489_(new FoodProperties.Builder().m_38760_(4).m_38758_(0.6F).m_38767_())));
   public static final RegistryObject<Item> BURGER = ITEMS.register("burger", () -> new Item(new Item.Properties().m_41489_(new FoodProperties.Builder().m_38760_(14).m_38758_(0.8F).m_38767_())));
   public static final RegistryObject<Item> BIG_BURGER = ITEMS.register("big_burger", () -> new Item(new Item.Properties().m_41489_(new FoodProperties.Builder().m_38760_(19).m_38758_(1.0F).m_38767_())));
   public static final RegistryObject<Item> COLA = ITEMS.register("cola", () -> new Item(new Item.Properties().m_41489_(new FoodProperties.Builder().m_38760_(1).m_38758_(0.4F).m_38767_())) {
          public int m_8105_(ItemStack stack) {
             return 32;
          }

          public UseAnim m_6164_(ItemStack stack) {
             return UseAnim.DRINK;
          }

          public InteractionResultHolder<ItemStack> m_7203_(Level level, Player player, InteractionHand hand) {
             player.m_6672_(hand);
             return InteractionResultHolder.m_19096_(player.m_21120_(hand));
          }

          @Nullable
          public SoundEvent m_6023_() {
             return SoundEvents.f_11911_;
          }

          @Nullable
          public SoundEvent m_6061_() {
             return SoundEvents.f_11911_;
          }
       });
   public static final RegistryObject<MobEffect> KAIF = EFFECTS.register("kaif", () -> new MobEffect(MobEffectCategory.NEUTRAL, 16740071) {
          public boolean m_19486_() {
             return false;
          }
       });
   public static final RegistryObject<Item> SUNFLOWER_OIL = ITEMS.register("sunflower_oil", () -> new Item(new Item.Properties()));
   public static final RegistryObject<Item> FRENCH_FRIES = ITEMS.register("french_fries", () -> new Item(new Item.Properties().m_41489_(new FoodProperties.Builder().m_38760_(5).m_38758_(0.6F).m_38767_())));
   public static final RegistryObject<SoundEvent> FRYER_LOOP = SOUND_EVENTS.register("fryer_loop", () -> SoundEvent.m_262824_(new ResourceLocation("sulfurcraft", "fryer_loop")));
   public static final RegistryObject<SoundEvent> OIL_PRESS_LOOP = SOUND_EVENTS.register("oil_press_loop", () -> SoundEvent.m_262824_(new ResourceLocation("sulfurcraft", "oil_press_loop")));
   public static final RegistryObject<BlockEntityType<FryerBlockEntity>> FRYER_BE = BLOCK_ENTITY_TYPES.register("fryer", () -> BEFactory.builder(FryerBlockEntity::new, FRYER.get()).m_58966_(null));
   public static final RegistryObject<BlockEntityType<OilPressBlockEntity>> OIL_PRESS_BE = BLOCK_ENTITY_TYPES.register("oil_press", () -> BEFactory.builder(OilPressBlockEntity::new, OIL_PRESS.get()).m_58966_(null));
   public static final RegistryObject<MenuType<FryerMenu>> FRYER_MENU = MENU_TYPES.register("fryer", () -> IForgeMenuType.create((windowId, inv, data) -> {
      BlockPos pos = data.m_130135_();
      net.minecraft.world.level.Level level = net.minecraft.client.Minecraft.m_91087_().f_91073_;
      return new FryerMenu(windowId, inv, (AbstractMachineBE) level.m_7702_(pos), net.minecraft.world.inventory.ContainerLevelAccess.m_39289_(level, pos));
   }));
   public static final RegistryObject<MenuType<OilPressMenu>> OIL_PRESS_MENU = MENU_TYPES.register("oil_press", () -> IForgeMenuType.create((windowId, inv, data) -> {
      BlockPos pos = data.m_130135_();
      net.minecraft.world.level.Level level = net.minecraft.client.Minecraft.m_91087_().f_91073_;
      return new OilPressMenu(windowId, inv, (AbstractMachineBE) level.m_7702_(pos), net.minecraft.world.inventory.ContainerLevelAccess.m_39289_(level, pos));
   }));

   public static class SunflowerCropBlock extends DoublePlantBlock implements BonemealableBlock {
      private static final VoxelShape AGE0 = Block.m_49796_(4.0D, 0.0D, 4.5D, 12.0D, 5.0D, 11.5D);
      private static final VoxelShape AGE1 = Block.m_49796_(3.0D, 0.0D, 3.0D, 13.0D, 15.5D, 13.0D);
      private static final VoxelShape LOWER_TALL = Block.m_49796_(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
      private static final VoxelShape STEM_UP = Block.m_49796_(6.5D, 0.0D, 7.0D, 9.5D, 10.5D, 10.0D);
      private static final VoxelShape AGE2_UPPER = Shapes.m_83110_(STEM_UP, Block.m_49796_(1.0D, 3.5D, 9.5D, 15.0D, 16.0D, 12.0D));
      private static final VoxelShape AGE3_UPPER = Shapes.m_83110_(STEM_UP, Block.m_49796_(0.0D, 2.5D, 9.5D, 16.0D, 16.0D, 12.0D));

      public SunflowerCropBlock(BlockBehaviour.Properties props) {
         super(props);
         this.m_49959_(this.m_49966_().m_61124_(SUNFLOWER_AGE, 0).m_61124_(DoublePlantBlock.f_52858_, DoubleBlockHalf.LOWER));
      }

      protected void m_7926_(StateDefinition.Builder<Block, BlockState> builder) {
         super.m_7926_(builder);
         builder.m_61104_(SUNFLOWER_AGE);
      }

      public BlockState m_5573_(BlockPlaceContext ctx) {
         return this.m_49966_().m_61124_(SUNFLOWER_AGE, 0);
      }

      public void m_6402_(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.LivingEntity placer, ItemStack stack) {
         if (!level.m_5776_()) {
            LOGGER.info("[Sunflower] setPlacedBy at " + pos + " state=" + state);
         }
      }

      protected boolean m_6266_(BlockState state, LevelReader level, BlockPos pos) {
         return !level.m_8055_(pos.m_7495_()).m_60795_();
      }

      public boolean m_6724_(BlockState state) {
         return true;
      }

      public void m_213898_(BlockState state, ServerLevel level, BlockPos pos, RandomSource rnd) {
         if (state.m_61143_(DoublePlantBlock.f_52858_) == DoubleBlockHalf.UPPER) {
            return;
         }
         growSunflower(level, pos, state, rnd, true);
      }

      public boolean m_7370_(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
         return state.m_61143_(SUNFLOWER_AGE) < 3;
      }

      public boolean m_214167_(Level level, RandomSource rnd, BlockPos pos, BlockState state) {
         return state.m_61143_(SUNFLOWER_AGE) < 3;
      }

      public void m_214148_(ServerLevel level, RandomSource rnd, BlockPos pos, BlockState state) {
         growSunflower(level, pos, state, rnd, false);
      }

      public VoxelShape m_5940_(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
         int age = state.m_61143_(SUNFLOWER_AGE);
         boolean upper = state.m_61143_(DoublePlantBlock.f_52858_) == DoubleBlockHalf.UPPER;
         if (upper && age < 2) {
            return Shapes.m_83040_();
         }
         if (age == 0) {
            return AGE0;
         }
         if (age == 1) {
            return AGE1;
         }
         if (age == 2) {
            return upper ? AGE2_UPPER : LOWER_TALL;
         }
         return upper ? AGE3_UPPER : LOWER_TALL;
      }

      public VoxelShape m_60742_(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
         return Shapes.m_83040_();
      }

      public BlockState m_7417_(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
         DoubleBlockHalf half = state.m_61143_(DoublePlantBlock.f_52858_);
         int age = state.m_61143_(SUNFLOWER_AGE);

         if (direction == Direction.DOWN && neighborState.m_60795_()) {
            LOGGER.info("[Sunflower] updateShape break (support became air) at " + pos + " half=" + half + " age=" + age);
            return Blocks.f_50016_.m_49966_();
         }
         if (direction == Direction.UP && half == DoubleBlockHalf.LOWER && age >= 2 && !neighborState.m_60713_(this)) {
            LOGGER.info("[Sunflower] updateShape break (upper removed) at " + pos + " age=" + age);
            return Blocks.f_50016_.m_49966_();
         }
         return state;
      }
   }

   private static void growSunflower(ServerLevel level, BlockPos pos, BlockState state, RandomSource rnd, boolean chance) {
      int age = state.m_61143_(SUNFLOWER_AGE);
      BlockPos base = state.m_61143_(DoublePlantBlock.f_52858_) == DoubleBlockHalf.UPPER ? pos.m_7495_() : pos;
      BlockState baseState = level.m_8055_(base);
      int cur = baseState.m_61143_(SUNFLOWER_AGE);
      if (cur >= 3) {
         return;
      }

      if (level.m_46849_(base, 0) < 8) {
         return;
      }

      if (chance && rnd.m_188503_(8) != 0) {
         return;
      }

      if (cur == 1) {
         BlockPos up = base.m_7494_();
         if (level.m_8055_(up).m_60795_()) {
            level.m_7731_(base, baseState.m_61124_(SUNFLOWER_AGE, 2), 3);
            level.m_7731_(up, baseState.m_61124_(SUNFLOWER_AGE, 2).m_61124_(DoublePlantBlock.f_52858_, DoubleBlockHalf.UPPER), 3);
         }

      } else {
         BlockState next = baseState.m_61124_(SUNFLOWER_AGE, cur + 1);
         level.m_7731_(base, next, 3);
         if (cur == 2) {
            BlockPos up = base.m_7494_();
            BlockState upState = level.m_8055_(up);
            if (upState.m_61143_(DoublePlantBlock.f_52858_) == DoubleBlockHalf.UPPER) {
               level.m_7731_(up, upState.m_61124_(SUNFLOWER_AGE, 3), 3);
            }
         }
      }
   }

   public SulfurCraft() {
      IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
      ITEMS.register(bus);
      EFFECTS.register(bus);
      BLOCKS.register(bus);
      BLOCK_ENTITY_TYPES.register(bus);
      MENU_TYPES.register(bus);
      SOUND_EVENTS.register(bus);
      bus.addListener(this::addCreative);
      bus.addListener(this::addPackFinders);
      bus.addListener(this::onCommonSetup);
      MinecraftForge.EVENT_BUS.register(this);
   }

   private void onCommonSetup(FMLCommonSetupEvent event) {
      event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(new ConsumeHandler.BeerBrewingRecipe()));
   }

   private void addCreative(BuildCreativeModeTabContentsEvent event) {
      if (event.getTabKey() == CreativeModeTabs.f_256968_) {
         event.accept(SULFUR_CRYSTAL);
         event.accept(SALT);
         event.accept(YEAST);
      }

      if (event.getTabKey() == CreativeModeTabs.f_256791_) {
         event.accept(FRYER_ITEM);
         event.accept(OIL_PRESS_ITEM);
      }

      if (event.getTabKey() == CreativeModeTabs.f_256839_) {
         event.accept(HAPPY_PILLS);
         event.accept(BEER);
         event.accept(CHEESE);
         event.accept(BURGER);
         event.accept(BIG_BURGER);
         event.accept(COLA);
         event.accept(SUNFLOWER_SEEDS);
         event.accept(SUNFLOWER_OIL);
         event.accept(FRENCH_FRIES);
      }

   }

   private void addPackFinders(AddPackFindersEvent event) {
      if (event.getPackType() == PackType.SERVER_DATA) {
         Path root = ModList.get().getModFileById("sulfurcraft").getFile().findResource(new String[]{"sulfurcraft_data"});
         Pack pack = Pack.m_245512_("builtin/sulfurcraft", Component.m_237113_("SulfurCraft recipes"), true, (id) -> new PathPackResources(id, root, true), new Pack.Info(Component.m_237113_("SulfurCraft built-in recipes"), 15, 15, FeatureFlags.f_244332_, false), PackType.SERVER_DATA, Position.TOP, false, PackSource.f_10528_);
         if (pack != null) {
            event.addRepositorySource((pc) -> pc.accept(pack));
         }

      }
   }
}
