package lt.pavilonis.monpikas.user.form;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Upload;
import lt.pavilonis.monpikas.common.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.function.BiConsumer;

public class UserFormViewImageUploader implements Upload.Receiver, Upload.SucceededListener {

   private static final Logger LOGGER = LoggerFactory.getLogger(UserFormViewImageUploader.class);
   private final BiConsumer<Resource, String> imageResourceConsumer;
   private ByteArrayOutputStream baos;

   public UserFormViewImageUploader(BiConsumer<Resource, String> imageResourceConsumer) {
      this.imageResourceConsumer = imageResourceConsumer;
   }

   @Override
   public OutputStream receiveUpload(String filename, String mimeType) {
      return baos = new ByteArrayOutputStream();
   }

   @Override
   public void uploadSucceeded(Upload.SucceededEvent event) {
      byte[] bytes = baos.toByteArray();
      byte[] scaledImageBytes = ImageUtils.scale(bytes, 500, 500);

      var imageResource = new StreamResource(() -> new ByteArrayInputStream(scaledImageBytes), "img.png");
      String base64ImageString = Base64.getEncoder().encodeToString(bytes);

      imageResourceConsumer.accept(imageResource, base64ImageString);
      close(baos);
   }

   private void close(Closeable closeable) {
      if (closeable != null) {
         try {
            closeable.close();
         } catch (final IOException e) {
            LOGGER.error("Could not close stream", e);
         }
      }
   }
}
