package lt.pavilonis.cmm.common.util;


import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
         e.printStackTrace();
         throw new RuntimeException("Could not write bytes to image", e);
      }
   }

   private static BufferedImage bytesToImage(byte[] imageBytes) {
      try (InputStream in = new ByteArrayInputStream(imageBytes)) {
         return ImageIO.read(in);

      } catch (IOException e) {
         e.printStackTrace();
         throw new RuntimeException("Could not read image from bytes", e);
      }
   }
}
