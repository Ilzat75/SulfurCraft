package sulfurcraft;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

public abstract class AbstractMachineBE extends BlockEntity implements Container, MenuProvider, ContainerData {
   public static final int SLOT_FUEL = 2;
   public static final int SLOT_OUTPUT = 3;
   public static final int SIZE = 4;
   protected java.util.List<ItemStack> items;
   protected int litTime;
   protected int litDuration;
   protected boolean lit;
   protected int cookTime;

   protected AbstractMachineBE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
      super(type, pos, state);
      this.items = net.minecraft.core.NonNullList.m_122780_(SIZE, ItemStack.f_41583_);
   }

   public java.util.List<ItemStack> getItems() {
      return this.items;
   }

   public int m_6643_() {
      return SIZE;
   }

   public boolean m_7983_() {
      for (ItemStack s : this.items) {
         if (!s.m_41619_()) return false;
      }
      return true;
   }

   public ItemStack m_8020_(int i) {
      return this.items.get(i);
   }

   public ItemStack m_7407_(int i, int count) {
      if (count <= 0) return ItemStack.f_41583_;
      ItemStack in = this.items.get(i);
      if (in.m_41619_()) return ItemStack.f_41583_;
      if (in.m_41613_() <= count) {
         ItemStack ret = in.m_41777_();
         this.items.set(i, ItemStack.f_41583_);
         this.m_6596_();
         return ret;
      }
      ItemStack ret = in.m_41620_(count);
      in.m_41764_(in.m_41613_() - count);
      this.m_6596_();
      return ret;
   }

   public ItemStack m_8016_(int i) {
      ItemStack ret = this.items.get(i).m_41777_();
      this.items.set(i, ItemStack.f_41583_);
      return ret;
   }

   public void m_6836_(int i, ItemStack stack) {
      this.items.set(i, stack);
      if (stack.m_41613_() > this.m_6893_()) {
         stack.m_41764_(this.m_6893_());
      }
      this.m_6596_();
   }

   public int m_6893_() {
      return 64;
   }

   public boolean m_6542_(Player player) {
      return Container.m_272074_(this, player);
   }

   public void m_5856_(Player player) {
   }

   public void m_5785_(Player player) {
   }

   public void m_6211_() {
      for (int i = 0; i < this.items.size(); i++) {
         this.items.set(i, ItemStack.f_41583_);
      }
   }

   public boolean m_7013_(int i, ItemStack stack) {
      return canPlaceInSlot(i, stack);
   }

   protected boolean canPlaceInSlot(int i, ItemStack stack) {
      return true;
   }

   protected void m_183515_(CompoundTag tag) {
      super.m_183515_(tag);
      for (int i = 0; i < this.items.size(); i++) {
         ItemStack s = this.items.get(i);
         if (!s.m_41619_()) {
            tag.m_128365_("Slot" + i, s.m_41783_());
         }
      }
      tag.m_128405_("LitTime", this.litTime);
      tag.m_128405_("LitDuration", this.litDuration);
      tag.m_128379_("Lit", this.lit);
      tag.m_128405_("CookTime", this.cookTime);
      this.saveExtra(tag);
   }

   public void m_142466_(CompoundTag tag) {
      super.m_142466_(tag);
      this.items = net.minecraft.core.NonNullList.m_122780_(SIZE, ItemStack.f_41583_);
      for (int i = 0; i < this.items.size(); i++) {
         if (tag.m_128425_("Slot" + i, 10)) {
            this.items.set(i, ItemStack.m_41712_(tag.m_128469_("Slot" + i)));
         }
      }
      this.litTime = tag.m_128451_("LitTime");
      this.litDuration = tag.m_128451_("LitDuration");
      this.lit = tag.m_128441_("Lit");
      this.cookTime = tag.m_128451_("CookTime");
      this.loadExtra(tag);
   }

   protected void saveExtra(CompoundTag tag) {
   }

   public net.minecraft.nbt.CompoundTag m_5995_() {
      net.minecraft.nbt.CompoundTag tag = new net.minecraft.nbt.CompoundTag();
      this.m_183515_(tag);
      return tag;
   }

   protected void loadExtra(CompoundTag tag) {
   }

   public int m_6413_(int i) {
      switch (i) {
         case 0: return this.cookTime;
         case 1: return this.litTime;
         case 2: return this.litDuration;
         default: return this.getExtraData(i);
      }
   }

   public void m_8050_(int i, int v) {
      switch (i) {
         case 0: this.cookTime = v; break;
         case 1: this.litTime = v; break;
         case 2: this.litDuration = v; break;
         default: this.setExtraData(i, v);
      }
   }

   protected int getExtraData(int i) {
      return 0;
   }

   protected void setExtraData(int i, int v) {
   }

   public int m_6499_() {
      return this.getDataSize();
   }

   protected int getDataSize() {
      return 3;
   }

   protected int getBurnTime() {
      return ForgeHooks.getBurnTime(this.items.get(SLOT_FUEL), null);
   }

   protected boolean canBurnFuel() {
      ItemStack fuel = this.items.get(SLOT_FUEL);
      return !fuel.m_41619_() && getBurnTime() > 0;
   }

   protected void consumeFuel() {
      ItemStack fuel = this.items.get(SLOT_FUEL);
      if (fuel.m_41613_() > 1) {
         fuel.m_41764_(fuel.m_41613_() - 1);
      } else {
         this.items.set(SLOT_FUEL, ItemStack.f_41583_);
      }
   }

   public void tick(Level level, BlockPos pos, BlockState state) {
      if (level.m_5776_()) return;
      boolean changed = false;
      if (this.litTime > 0) this.litTime--;
      if (!this.lit && canBurnFuel()) {
         int burn = getBurnTime();
         if (burn > 0) {
            this.litDuration = burn;
            this.litTime = burn;
            consumeFuel();
            changed = true;
         }
      }
      boolean wasLit = this.lit;
      this.lit = this.litTime > 0;
      if (wasLit != this.lit) {
         changed = true;
         level.m_7260_(pos, state, state, 3);
      }
      if (this.lit && canWork()) {
         this.cookTime++;
         if (this.cookTime >= getMaxWorkTime()) {
            doWork(level);
            this.cookTime = 0;
            changed = true;
         }
      } else if (this.cookTime > 0) {
         this.cookTime = Math.max(0, this.cookTime - 2);
         changed = true;
      }
      if (changed) this.m_6596_();
   }

   public boolean isLit() {
      return this.lit;
   }

   public int getCookTime() {
      return this.cookTime;
   }

   public int getLitTime() {
      return this.litTime;
   }

   protected abstract int getMaxWorkTime();

   protected abstract boolean canWork();

   protected abstract void doWork(Level level);

   public abstract net.minecraft.sounds.SoundEvent getLoopSound();

   public abstract String getMachineName();

   public net.minecraft.network.chat.Component m_5446_() {
      return net.minecraft.network.chat.Component.m_237113_("sulfurcraft.container." + getMachineName());
   }

   public abstract AbstractContainerMenu m_7208_(int windowId, Inventory inv, Player player);
}
