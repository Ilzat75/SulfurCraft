import com.google.gson.JsonParser;
import java.nio.file.*;
import java.util.stream.*;

public class GsonCheck {
   public static void main(String[] args) throws Exception {
      int[] bad = {0};
      try (var walk = Files.walk(Paths.get("C:/Users/User/Desktop/SulfurCraft/sulfurcraft/mdk/src/main/resources"))) {
         for (var f : walk.filter(p -> p.toString().endsWith(".json")).toArray(Path[]::new)) {
            try {
               JsonParser.parseString(new String(Files.readAllBytes(f), java.nio.charset.StandardCharsets.UTF_8));
               System.out.println("OK   " + f.getFileName());
            } catch (Exception e) {
               bad[0]++;
               System.out.println("BAD  " + f + " -> " + e.getMessage());
            }
         }
      }
      System.out.println("bad=" + bad[0]);
   }
}
