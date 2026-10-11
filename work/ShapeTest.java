import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShapeTest {
   static VoxelShape age0 = Shapes.m_83048_(3.8D, 0.0D, 4.0D, 12.2D, 4.75D, 11.5D);
   static VoxelShape age1 = Shapes.m_83048_(2.0D, 0.0D, 2.5D, 14.0D, 15.5D, 14.5D);
   static VoxelShape lowerGrown = Shapes.m_83124_(Shapes.m_83048_(7.0D, 0.0D, 7.5D, 9.0D, 16.0D, 9.5D),
      Shapes.m_83048_(0.0D, 3.0D, 5.0D, 7.0D, 4.0D, 11.0D),
      Shapes.m_83048_(9.0D, 5.5D, 5.0D, 16.0D, 6.5D, 11.0D),
      Shapes.m_83048_(5.0D, 2.0D, 9.5D, 11.0D, 3.0D, 16.0D),
      Shapes.m_83048_(5.0D, 5.0D, 0.0D, 11.0D, 6.0D, 7.5D),
      Shapes.m_83048_(1.5D, 10.5D, 5.5D, 7.0D, 11.5D, 10.5D),
      Shapes.m_83048_(9.0D, 13.0D, 5.5D, 14.5D, 14.0D, 10.5D));
   static VoxelShape upper = Shapes.m_83124_(Shapes.m_83048_(7.0D, 0.0D, 7.5D, 9.0D, 10.5D, 9.5D),
      Shapes.m_83048_(4.0D, 6.5D, 9.5D, 12.0D, 14.5D, 12.0D),
      Shapes.m_83048_(0.0D, 2.5D, 10.5D, 16.0D, 16.0D, 12.0D),
      Shapes.m_83048_(2.0D, 1.5D, 5.5D, 7.0D, 2.5D, 10.5D),
      Shapes.m_83048_(9.0D, 4.0D, 5.5D, 14.0D, 5.0D, 10.5D));

   public static void main(String[] args) {
      String[] names = {"AGE0", "AGE1", "LOWER_GROWN", "UPPER"};
      VoxelShape[] shapes = {age0, age1, lowerGrown, upper};
      BlockPos pos = new BlockPos(0, 0, 0);

      for (int i = 0; i < 4; ++i) {
         VoxelShape s = shapes[i];
         System.out.println("=== " + names[i] + " empty=" + s.m_83281_() + " bounds=" + s.m_83215_());

         for (int j = 0; j < s.m_83299_().size(); ++j) {
            System.out.println("    box" + j + " = " + s.m_83299_().get(j));
         }

         Vec3 eye = new Vec3(8.0D, 12.0D, -24.0D);
         Vec3 target = new Vec3(8.0D, 8.0D, 8.0D);
         BlockHitResult hit = s.m_83220_(eye, target, pos);
         System.out.println("    clip eye->center hit=" + (hit == null ? "null" : "HIT " + hit.m_82450_()));
      }
   }
}
