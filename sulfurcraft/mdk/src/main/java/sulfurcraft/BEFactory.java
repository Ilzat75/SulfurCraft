package sulfurcraft;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public final class BEFactory {
   @SuppressWarnings("unchecked")
   public static <T extends BlockEntity> BlockEntityType.Builder<T> builder(BiFunction<BlockPos, BlockState, T> factory, Block... blocks) {
      try {
         Class<?> supplierClass = Class.forName("net.minecraft.world.level.block.entity.BlockEntityType$BlockEntitySupplier");
         Object supplier = Proxy.newProxyInstance(
               supplierClass.getClassLoader(),
               new Class<?>[]{supplierClass},
               (proxy, method, args) -> {
                  String n = method.getName();
                  if (n.equals("toString")) return "BEFactorySupplier";
                  if (n.equals("hashCode")) return System.identityHashCode(proxy);
                  if (n.equals("equals")) return proxy == args[0];
                  return factory.apply((BlockPos) args[0], (BlockState) args[1]);
               });
         Method ofMethod = findOf(supplierClass);
         return (BlockEntityType.Builder<T>) ofMethod.invoke(null, supplier, (Object) blocks);
      } catch (Exception e) {
         throw new RuntimeException("Failed to create BlockEntityType.Builder", e);
      }
   }

   private static Method findOf(Class<?> supplierClass) throws NoSuchMethodException {
      for (String name : new String[]{"m_155273_", "of"}) {
         try {
            return BlockEntityType.Builder.class.getMethod(name, supplierClass, Block[].class);
         } catch (NoSuchMethodException ignored) {
         }
      }
      throw new NoSuchMethodException("BlockEntityType.Builder.of / m_155273_");
   }
}
