package sulfurcraft;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class FryerBlockEntity extends AbstractMachineBE {
   private static final Item POTATO = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "potato"));
   private static final int MAX_OIL = 100;
   private int oil;

   public FryerBlockEntity(BlockPos pos, BlockState state) {
      super(SulfurCraft.FRYER_BE.get(), pos, state);
   }

   protected boolean canPlaceInSlot(int i, ItemStack stack) {
      if (i == 0) return stack.m_41720_() == POTATO;
      if (i == 1) return stack.m_41720_() == SulfurCraft.SUNFLOWER_OIL.get();
      if (i == 3) return false;
      return true;
   }

   protected int getMaxWorkTime() {
      return 100;
   }

   protected boolean canWork() {
      boolean potato = !this.items.get(0).m_41619_();
      boolean hasOil = this.oil > 0;
      boolean outOk = this.items.get(3).m_41619_() || (this.items.get(3).m_41720_() == SulfurCraft.FRENCH_FRIES.get() && this.items.get(3).m_41613_() < 64);
      return potato && hasOil && outOk;
   }

   protected void doWork(Level level) {
      ItemStack potato = this.items.get(0);
      if (potato.m_41613_() > 1) potato.m_41764_(potato.m_41613_() - 1);
      else this.items.set(0, ItemStack.f_41583_);
      this.oil--;
      ItemStack out = this.items.get(3);
      if (out.m_41619_()) {
         this.items.set(3, new ItemStack(SulfurCraft.FRENCH_FRIES.get(), 1));
      } else if (out.m_41613_() < 64) {
         out.m_41764_(out.m_41613_() + 1);
      }
   }

   public void tick(Level level, BlockPos pos, BlockState state) {
      if (!level.m_5776_() && this.oil < MAX_OIL) {
         ItemStack bottle = this.items.get(1);
         if (!bottle.m_41619_() && bottle.m_41720_() == SulfurCraft.SUNFLOWER_OIL.get()) {
            if (bottle.m_41613_() > 1) bottle.m_41764_(bottle.m_41613_() - 1);
            else this.items.set(1, ItemStack.f_41583_);
            this.oil = Math.min(MAX_OIL, this.oil + 10);
            this.m_6596_();
         }
      }
      super.tick(level, pos, state);
   }

   protected void saveExtra(CompoundTag tag) {
      tag.m_128405_("Oil", this.oil);
   }

   protected void loadExtra(CompoundTag tag) {
      this.oil = tag.m_128451_("Oil");
   }

   protected int getExtraData(int i) {
      return i == 3 ? this.oil : 0;
   }

   protected void setExtraData(int i, int v) {
      if (i == 3) this.oil = v;
   }

   protected int getDataSize() {
      return 4;
   }

   public int getOil() {
      return this.oil;
   }

   public static int getMaxOilStatic() {
      return MAX_OIL;
   }

   public SoundEvent getLoopSound() {
      return SulfurCraft.FRYER_LOOP.get();
   }

   public String getMachineName() {
      return "fryer";
   }

   public AbstractContainerMenu m_7208_(int windowId, Inventory inv, Player player) {
      return new FryerMenu(windowId, inv, this, ContainerLevelAccess.m_39289_(this.m_58904_(), this.m_58899_()));
   }
}
