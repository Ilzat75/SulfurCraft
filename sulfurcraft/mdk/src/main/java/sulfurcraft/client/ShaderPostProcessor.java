package sulfurcraft.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import sulfurcraft.ConsumeHandler;
import sulfurcraft.SulfurCraft;

@EventBusSubscriber(
   modid = "sulfurcraft",
   value = {Dist.CLIENT}
)
public class ShaderPostProcessor {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final String VSH = "#version 150\nin vec2 Position;\nin vec2 UV;\nout vec2 texCoord;\nvoid main() {\n    gl_Position = vec4(Position, 0.0, 1.0);\n    texCoord = UV;\n}\n";
   private static final String FSH = "#version 150\nuniform sampler2D DiffuseSampler;\nuniform float Strength;\nuniform float Mode;\nuniform float Time;\nuniform float Fade;\nin vec2 texCoord;\nout vec4 fragColor;\nvec3 hsv2rgb(vec3 c) {\n    vec4 K = vec4(1.0, 2.0/3.0, 1.0/3.0, 3.0);\n    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\n    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\nvec3 rgb2hsv(vec3 c) {\n    vec4 K = vec4(0.0, -1.0/3.0, 2.0/3.0, -1.0);\n    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\n    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\n    float d = q.x - min(q.w, q.y);\n    float e = 1.0e-10;\n    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\nvoid main() {\n    vec4 color = texture(DiffuseSampler, texCoord);\n    vec3 luma = vec3(dot(color.rgb, vec3(0.299, 0.587, 0.114)));\n    vec3 processed;\n    if (Mode <= 0.5) {\n        // doses 1-2: saturation boost\n        processed = mix(luma, color.rgb, min(Strength, 3.0));\n    } else if (Mode <= 1.5) {\n        // dose 3: rainbow hue rotation on the original picture\n        float hueShift = fract(Time * 0.06 + texCoord.x * 0.03 + texCoord.y * 0.03);\n        vec3 hsv = rgb2hsv(color.rgb);\n        hsv.x = fract(hsv.x + hueShift);\n        processed = hsv2rgb(hsv);\n    } else if (Mode <= 2.5) {\n        // dose 4: grayscale\n        float c = smoothstep(0.35, 0.65, luma.x);\n        processed = vec3(c * 0.92 + 0.04);\n    } else {\n        // dose 5: grayscale high contrast\n        float s = min(Strength, 1.0);\n        float lo = 0.45 - 0.15 * s;\n        float hi = 0.55 + 0.15 * s;\n        float c = smoothstep(lo, hi, luma.x);\n        float bl = 0.02 * (1.0 - s);\n        float wh = 1.0 - 0.02 * (1.0 - s);\n        processed = mix(vec3(bl), vec3(wh), c);\n    }\n    vec3 result = mix(color.rgb, processed, Fade);\n    fragColor = vec4(clamp(result, 0.0, 1.0), color.a);\n}\n";
   private static int program = -1;
   private static int vao = -1;
   private static int vbo = -1;
   private static int tex = -1;
   private static int uStrength = -1;
   private static int uMode = -1;
   private static int uTime = -1;
   private static int uFade = -1;
   private static int uSampler = -1;
   private static int lastW = -1;
   private static int lastH = -1;
   private static int lastRemaining = -1;
   private static int fadeTicks = -1;

   @SubscribeEvent
   public static void onGui(RenderGuiEvent.Pre event) {
      Minecraft mc = Minecraft.m_91087_();
      if (mc.f_91074_ != null && mc.f_91073_ != null) {
         MobEffectInstance marker = mc.f_91074_.m_21124_((MobEffect)SulfurCraft.KAIF.get());
         if (marker == null) {
            lastRemaining = -1;
            fadeTicks = -1;
         } else {
            RenderTarget target = mc.m_91385_();
            int w = target.f_83915_;
            int h = target.f_83916_;
            if (w > 0 && h > 0) {
               if (program < 0) {
                  init(w, h);
               }

               if (lastW != w || lastH != h) {
                  resize(w, h);
               }

               int remaining = marker.m_19557_();
               if (lastRemaining < 0 || remaining > lastRemaining + 10) {
                  fadeTicks = 10;
               }

               lastRemaining = remaining;
               if (fadeTicks > 0) {
                  --fadeTicks;
               }

               int dose = ConsumeHandler.decodeDose(marker.m_19564_());
               int mode = dose <= 2 ? 0 : (dose == 3 ? 1 : (dose == 4 ? 2 : 3));
               float strength = 1.0F + (float)dose * 0.4F;
               float fadeIn = fadeTicks > 0 ? 1.0F - (float)fadeTicks / 10.0F : 1.0F;
               float fadeOut = Math.max(0.0F, Math.min(1.0F, (float)remaining / 20.0F));
               float fade = Math.min(fadeIn, fadeOut);
               float time = (float)(System.currentTimeMillis() % 100000L) / 1000.0F;
               target.m_83956_();
               GL11.glBindTexture(3553, tex);
               GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, w, h);
               target.m_83947_(true);
               GL11.glDisable(2929);
               GL11.glDisable(2884);
               GL11.glDisable(3042);
               GL20.glUseProgram(program);
               GL30.glBindVertexArray(vao);
               GL13.glActiveTexture(33984);
               GL11.glBindTexture(3553, tex);
               GL20.glUniform1i(uSampler, 0);
               GL20.glUniform1f(uStrength, strength);
               GL20.glUniform1f(uMode, (float)mode);
               GL20.glUniform1f(uTime, time);
               GL20.glUniform1f(uFade, fade);
               GL20.glDrawArrays(5, 0, 4);
               GL30.glBindVertexArray(0);
               GL20.glUseProgram(0);
               GL11.glBindTexture(3553, 0);
               RenderSystem.enableBlend();
               RenderSystem.enableDepthTest();
            }
         }
      }
   }

   private static void init(int w, int h) {
      int vs = compileShader(35633, "#version 150\nin vec2 Position;\nin vec2 UV;\nout vec2 texCoord;\nvoid main() {\n    gl_Position = vec4(Position, 0.0, 1.0);\n    texCoord = UV;\n}\n");
      int fs = compileShader(35632, "#version 150\nuniform sampler2D DiffuseSampler;\nuniform float Strength;\nuniform float Mode;\nuniform float Time;\nuniform float Fade;\nin vec2 texCoord;\nout vec4 fragColor;\nvec3 hsv2rgb(vec3 c) {\n    vec4 K = vec4(1.0, 2.0/3.0, 1.0/3.0, 3.0);\n    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\n    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\nvec3 rgb2hsv(vec3 c) {\n    vec4 K = vec4(0.0, -1.0/3.0, 2.0/3.0, -1.0);\n    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\n    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\n    float d = q.x - min(q.w, q.y);\n    float e = 1.0e-10;\n    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\nvoid main() {\n    vec4 color = texture(DiffuseSampler, texCoord);\n    vec3 luma = vec3(dot(color.rgb, vec3(0.299, 0.587, 0.114)));\n    vec3 processed;\n    if (Mode <= 0.5) {\n        // doses 1-2: saturation boost\n        processed = mix(luma, color.rgb, min(Strength, 3.0));\n    } else if (Mode <= 1.5) {\n        // dose 3: rainbow hue rotation on the original picture\n        float hueShift = fract(Time * 0.06 + texCoord.x * 0.03 + texCoord.y * 0.03);\n        vec3 hsv = rgb2hsv(color.rgb);\n        hsv.x = fract(hsv.x + hueShift);\n        processed = hsv2rgb(hsv);\n    } else if (Mode <= 2.5) {\n        // dose 4: grayscale\n        float c = smoothstep(0.35, 0.65, luma.x);\n        processed = vec3(c * 0.92 + 0.04);\n    } else {\n        // dose 5: grayscale high contrast\n        float s = min(Strength, 1.0);\n        float lo = 0.45 - 0.15 * s;\n        float hi = 0.55 + 0.15 * s;\n        float c = smoothstep(lo, hi, luma.x);\n        float bl = 0.02 * (1.0 - s);\n        float wh = 1.0 - 0.02 * (1.0 - s);\n        processed = mix(vec3(bl), vec3(wh), c);\n    }\n    vec3 result = mix(color.rgb, processed, Fade);\n    fragColor = vec4(clamp(result, 0.0, 1.0), color.a);\n}\n");
      if (vs >= 0 && fs >= 0) {
         program = GL20.glCreateProgram();
         GL20.glAttachShader(program, vs);
         GL20.glAttachShader(program, fs);
         GL20.glLinkProgram(program);
         if (GL20.glGetProgrami(program, 35714) == 0) {
            LOGGER.error("[SulfurCraft] Shader link failed: {}", GL20.glGetProgramInfoLog(program));
         } else {
            GL20.glDeleteShader(vs);
            GL20.glDeleteShader(fs);
            uStrength = GL20.glGetUniformLocation(program, "Strength");
            uMode = GL20.glGetUniformLocation(program, "Mode");
            uTime = GL20.glGetUniformLocation(program, "Time");
            uFade = GL20.glGetUniformLocation(program, "Fade");
            uSampler = GL20.glGetUniformLocation(program, "DiffuseSampler");
            float[] data = new float[]{-1.0F, -1.0F, 0.0F, 0.0F, 1.0F, -1.0F, 1.0F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F};
            FloatBuffer buf = MemoryUtil.memAllocFloat(data.length);
            buf.put(data).flip();
            vao = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vao);
            vbo = GL15.glGenBuffers();
            GL15.glBindBuffer(34962, vbo);
            GL15.glBufferData(34962, buf, 35044);
            MemoryUtil.memFree(buf);
            GL20.glEnableVertexAttribArray(0);
            GL20.glVertexAttribPointer(0, 2, 5126, false, 16, 0L);
            GL20.glEnableVertexAttribArray(1);
            GL20.glVertexAttribPointer(1, 2, 5126, false, 16, 8L);
            GL30.glBindVertexArray(0);
            tex = GL11.glGenTextures();
            resize(w, h);
            lastW = w;
            lastH = h;
            lastRemaining = -1;
            fadeTicks = -1;
            LOGGER.info("[SulfurCraft] Raw-GL shader initialized ({}x{})", w, h);
         }
      } else {
         program = -1;
      }
   }

   private static void resize(int w, int h) {
      if (tex > 0) {
         GL11.glDeleteTextures(tex);
      }

      tex = GL11.glGenTextures();
      GL11.glBindTexture(3553, tex);
      GL11.glTexImage2D(3553, 0, 32856, w, h, 0, 6408, 5121, (ByteBuffer)null);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glTexParameteri(3553, 10242, 33071);
      GL11.glTexParameteri(3553, 10243, 33071);
      GL11.glBindTexture(3553, 0);
      lastW = w;
      lastH = h;
   }

   private static int compileShader(int type, String src) {
      int id = GL20.glCreateShader(type);
      GL20.glShaderSource(id, src);
      GL20.glCompileShader(id);
      if (GL20.glGetShaderi(id, 35713) == 0) {
         LOGGER.error("[SulfurCraft] Shader compile failed ({}): {}", type, GL20.glGetShaderInfoLog(id));
         GL20.glDeleteShader(id);
         return -1;
      } else {
         return id;
      }
   }
}
