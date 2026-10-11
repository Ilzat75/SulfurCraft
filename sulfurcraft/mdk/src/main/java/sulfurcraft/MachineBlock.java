package sulfurcraft;

import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class MachineBlock extends HorizontalDirectionalBlock implements net.minecraft.world.level.block.EntityBlock {
   private final BiFunction<BlockPos, BlockState, BlockEntity> factory;

   public MachineBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties props, BiFunction<BlockPos, BlockState, BlockEntity> factory) {
      super(props);
      this.factory = factory;
      this.m_49959_(this.m_49966_().m_61124_(HorizontalDirectionalBlock.f_54117_, Direction.NORTH));
   }

   protected void m_7926_(StateDefinition.Builder<Block, BlockState> builder) {
      builder.m_61104_(HorizontalDirectionalBlock.f_54117_);
   }

   public BlockState m_5573_(net.minecraft.world.item.context.BlockPlaceContext ctx) {
      return this.m_49966_().m_61124_(HorizontalDirectionalBlock.f_54117_, ctx.m_8125_().m_122424_());
   }

   public net.minecraft.world.MenuProvider m_7246_(BlockState state, Level level, BlockPos pos) {
      BlockEntity be = level.m_7702_(pos);
      return be instanceof net.minecraft.world.MenuProvider mp ? mp : null;
   }

   public InteractionResult m_6227_(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (!level.m_5776_()) {
         BlockEntity be = level.m_7702_(pos);
         if (be instanceof net.minecraft.world.MenuProvider mp) {
            NetworkHooks.openScreen((ServerPlayer) player, mp, pos);
         }
      }
      return InteractionResult.m_19078_(level.m_5776_());
   }

   public BlockEntity m_142194_(BlockPos pos, BlockState state) {
      return this.factory.apply(pos, state);
   }

   @SuppressWarnings("unchecked")
   public <T extends BlockEntity> BlockEntityTicker<T> m_142354_(Level level, BlockState state, BlockEntityType<T> type) {
      if (level.m_5776_()) {
         return null;
      }
      return (l, p, s, be) -> {
         if (be instanceof AbstractMachineBE mbe) {
            mbe.tick(l, p, s);
         }
      };
   }

   public void m_6807_(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
      if (!state.m_60713_(newState.m_60734_())) {
         BlockEntity be = level.m_7702_(pos);
         if (be instanceof AbstractMachineBE mbe) {
            Containers.m_19002_(level, pos, mbe);
         }
         level.m_46717_(pos, this);
      }
      super.m_6807_(state, level, pos, newState, movedByPiston);
   }
}
