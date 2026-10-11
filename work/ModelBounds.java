import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ModelBounds {
   static double minX, minY, minZ, maxX, maxY, maxZ;

   public static void main(String[] args) throws Exception {
      String[] models = {"sunflower_stage0", "sunflower_stage1", "sunflower_stage2_lower", "sunflower_stage2_upper", "sunflower_stage3_lower", "sunflower_stage3_upper"};
      for (String m : models) {
         String raw = new String(Files.readAllBytes(Paths.get("work/stage/assets/sulfurcraft/models/block/" + m + ".json")));
         JsonObject root = JsonParser.parseString(raw).getAsJsonObject();
         JsonArray elements = root.getAsJsonArray("elements");
         minX = 99; minY = 99; minZ = 99; maxX = -99; maxY = -99; maxZ = -99;
         int idx = 0;
         for (var el : elements) {
            JsonObject obj = el.getAsJsonObject();
            double[] from = arr(obj.getAsJsonArray("from"));
            double[] to = arr(obj.getAsJsonArray("to"));
            double[] aabb = toAABB(from, to);
            if (obj.has("rotation")) {
               JsonObject rot = obj.getAsJsonObject("rotation");
               double[] origin = arr(rot.getAsJsonArray("origin"));
               double angle = rot.get("angle").getAsDouble();
               String axis = rot.get("axis").getAsString();
               aabb = rotateAABB(aabb, origin, angle, axis);
            }
            minX = Math.min(minX, aabb[0]); minY = Math.min(minY, aabb[1]); minZ = Math.min(minZ, aabb[2]);
            maxX = Math.max(maxX, aabb[3]); maxY = Math.max(maxY, aabb[4]); maxZ = Math.max(maxZ, aabb[5]);
            System.out.println("  el" + idx + ": [" + round(aabb[0]) + "," + round(aabb[1]) + "," + round(aabb[2]) + " -> " + round(aabb[3]) + "," + round(aabb[4]) + "," + round(aabb[5]) + "]");
            ++idx;
         }
         System.out.println("=== " + m + " UNION: [" + round(minX) + "," + round(minY) + "," + round(minZ) + " -> " + round(maxX) + "," + round(maxY) + "," + round(maxZ) + "]");
      }
   }

   static double[] arr(JsonArray a) {
      double[] r = new double[a.size()];
      for (int i = 0; i < a.size(); ++i) r[i] = a.get(i).getAsDouble();
      return r;
   }

   static double[] toAABB(double[] from, double[] to) {
      return new double[]{Math.min(from[0], to[0]), Math.min(from[1], to[1]), Math.min(from[2], to[2]), Math.max(from[0], to[0]), Math.max(from[1], to[1]), Math.max(from[2], to[2])};
   }

   static double round(double d) {
      return Math.round(d * 100.0D) / 100.0D;
   }

   static double[] rotateAABB(double[] aabb, double[] origin, double angle, String axis) {
      double rad = angle * Math.PI / 180.0D;
      double sin = Math.sin(rad);
      double cos = Math.cos(rad);
      double minX = 99, minY = 99, minZ = 99, maxX = -99, maxY = -99, maxZ = -99;
      for (int i = 0; i < 8; ++i) {
         double x = (i & 1) == 0 ? aabb[0] : aabb[3];
         double y = ((i >> 1) & 1) == 0 ? aabb[1] : aabb[4];
         double z = ((i >> 2) & 1) == 0 ? aabb[2] : aabb[5];
         double nx = x, ny = y, nz = z;
         if (axis.equals("x")) {
            ny = origin[1] + (y - origin[1]) * cos - (z - origin[2]) * sin;
            nz = origin[2] + (y - origin[1]) * sin + (z - origin[2]) * cos;
         } else if (axis.equals("y")) {
            nx = origin[0] + (x - origin[0]) * cos + (z - origin[2]) * sin;
            nz = origin[2] - (x - origin[0]) * sin + (z - origin[2]) * cos;
         } else {
            nx = origin[0] + (x - origin[0]) * cos - (y - origin[1]) * sin;
            ny = origin[1] + (x - origin[0]) * sin + (y - origin[1]) * cos;
         }
         minX = Math.min(minX, nx); minY = Math.min(minY, ny); minZ = Math.min(minZ, nz);
         maxX = Math.max(maxX, nx); maxY = Math.max(maxY, ny); maxZ = Math.max(maxZ, nz);
      }
      return new double[]{minX, minY, minZ, maxX, maxY, maxZ};
   }
}
