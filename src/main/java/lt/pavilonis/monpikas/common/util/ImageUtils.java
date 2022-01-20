package lt.pavilonis.monpikas.common.util;


import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ImageUtils {

   private ImageUtils() {/**/}

   public static byte[] scale(byte[] imageBytes, int width, int height) {

      BufferedImage image = bytesToImage(imageBytes);

      BufferedImage scaledImage = Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, width, height);

      return imageToBytes(scaledImage);
   }

   private static byte[] imageToBytes(BufferedImage scaledImage) {
      try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
         ImageIO.write(scaledImage, "png", baos);
         return baos.toByteArray();

      } catch (IOException e) {
         String message = "Could not write bytes to image";
         log.error(message, e);
         throw new RuntimeException(message, e);
      }
   }

   private static BufferedImage bytesToImage(byte[] imageBytes) {
      try (InputStream in = new ByteArrayInputStream(imageBytes)) {
         return ImageIO.read(in);

      } catch (IOException e) {
         String message = "Could not read image from bytes";
         log.error(message, e);
         throw new RuntimeException(message, e);
      }
   }
}
