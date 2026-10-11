package sulfurcraft;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class OilPressBlockEntity extends AbstractMachineBE {
   private int batch;

   public OilPressBlockEntity(BlockPos pos, BlockState state) {
      super(SulfurCraft.OIL_PRESS_BE.get(), pos, state);
   }

   protected boolean canPlaceInSlot(int i, ItemStack stack) {
      if (i == 0) return stack.m_41720_() == SulfurCraft.SUNFLOWER_SEEDS.get();
      if (i == 1) return stack.m_41720_() == Items.f_42415_;
      if (i == 3) return false;
      return true;
   }

   protected int getMaxWorkTime() {
      return 140;
   }

   protected boolean canWork() {
      boolean seed = !this.items.get(0).m_41619_();
      boolean bottle = !this.items.get(1).m_41619_();
      boolean outOk = this.batch < 64 || canAcceptOil();
      return seed && bottle && outOk;
   }

   private boolean canAcceptOil() {
      ItemStack out = this.items.get(3);
      return out.m_41619_() || (out.m_41720_() == SulfurCraft.SUNFLOWER_OIL.get() && out.m_41613_() < 64);
   }

   protected void doWork(Level level) {
      ItemStack seed = this.items.get(0);
      if (seed.m_41613_() > 1) seed.m_41764_(seed.m_41613_() - 1);
      else this.items.set(0, ItemStack.f_41583_);
      this.batch++;
      if (this.batch >= 64) {
         this.items.set(1, ItemStack.f_41583_);
         ItemStack out = this.items.get(3);
         if (out.m_41619_()) {
            this.items.set(3, new ItemStack(SulfurCraft.SUNFLOWER_OIL.get(), 1));
         } else if (out.m_41613_() < 64) {
            out.m_41764_(out.m_41613_() + 1);
         }
         this.batch = 0;
      }
   }

   protected void saveExtra(CompoundTag tag) {
      tag.m_128405_("Batch", this.batch);
   }

   protected void loadExtra(CompoundTag tag) {
      this.batch = tag.m_128451_("Batch");
   }

   protected int getExtraData(int i) {
      return i == 3 ? this.batch : 0;
   }

   protected void setExtraData(int i, int v) {
      if (i == 3) this.batch = v;
   }

   protected int getDataSize() {
      return 4;
   }

   public int getBatch() {
      return this.batch;
   }

   public SoundEvent getLoopSound() {
      return SulfurCraft.OIL_PRESS_LOOP.get();
   }

   public String getMachineName() {
      return "oil_press";
   }

   public AbstractContainerMenu m_7208_(int windowId, Inventory inv, Player player) {
      return new OilPressMenu(windowId, inv, this, ContainerLevelAccess.m_39289_(this.m_58904_(), this.m_58899_()));
   }
}
