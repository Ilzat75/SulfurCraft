package sulfurcraft;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class OilPressMenu extends AbstractContainerMenu {
   private final AbstractMachineBE machine;
   private final ContainerLevelAccess access;

   public OilPressMenu(int windowId, Inventory inv, AbstractMachineBE machine, ContainerLevelAccess access) {
      super(SulfurCraft.OIL_PRESS_MENU.get(), windowId);
      this.machine = machine;
      this.access = access;
      this.m_38897_(new Slot(machine, 0, 56, 17));
      this.m_38897_(new Slot(machine, 1, 38, 17));
      this.m_38897_(new Slot(machine, 2, 56, 53));
      this.m_38897_(new Slot(machine, 3, 116, 35));
      this.addPlayerInv(inv);
      this.m_38884_(machine);
   }

   private void addPlayerInv(Inventory inv) {
      for (int i = 0; i < 3; i++) {
         for (int j = 0; j < 9; j++) {
            this.m_38897_(new Slot(inv, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
         }
      }
      for (int k = 0; k < 9; k++) {
         this.m_38897_(new Slot(inv, k, 8 + k * 18, 142));
      }
   }

   public boolean m_6875_(Player player) {
      return m_38889_(this.access, player, this.machine.m_58900_().m_60734_());
   }

   public ItemStack m_7648_(Player player, int index) {
      ItemStack copy = ItemStack.f_41583_;
      Slot slot = this.f_38839_.get(index);
      if (slot != null && slot.m_6657_()) {
         ItemStack in = slot.m_7993_();
         copy = in.m_41777_();
         if (index < 4) {
            if (!this.m_38903_(in, 4, 40, true)) return ItemStack.f_41583_;
         } else {
            if (!this.m_38903_(in, 0, 4, false)) return ItemStack.f_41583_;
         }
         if (in.m_41619_()) {
            slot.m_5852_(ItemStack.f_41583_);
         } else {
            slot.m_6654_();
         }
         if (in.m_41613_() == copy.m_41613_()) {
            return ItemStack.f_41583_;
         }
         slot.m_142406_(player, in);
      }
      return copy;
   }

   public AbstractMachineBE getMachine() {
      return this.machine;
   }
}
