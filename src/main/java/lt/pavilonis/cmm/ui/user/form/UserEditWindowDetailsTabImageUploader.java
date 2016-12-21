package lt.pavilonis.cmm.ui.user.form;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Upload;
import lt.pavilonis.ImageUtils;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

public class UserEditWindowDetailsTabImageUploader implements Upload.Receiver, Upload.SucceededListener {

   Consumer<Resource> imageResourceConsumer;
   private ByteArrayOutputStream baos;
   private byte[] scaledImageBytes;

   public UserEditWindowDetailsTabImageUploader(Consumer<Resource> imageResourceConsumer) {
      this.imageResourceConsumer = imageResourceConsumer;
   }

   @Override
   public OutputStream receiveUpload(String filename, String mimeType) {
      return baos = new ByteArrayOutputStream();
   }

   @Override
   public void uploadSucceeded(Upload.SucceededEvent event) {
      byte[] bytes = baos.toByteArray();
      scaledImageBytes = ImageUtils.scale(bytes, 600, 600);

      StreamResource imageResource = new StreamResource(() -> new ByteArrayInputStream(scaledImageBytes), "img.png");
      imageResourceConsumer.accept(imageResource);
      IOUtils.closeQuietly(baos);
   }

   public byte[] getScaledImageBytes() {
      return scaledImageBytes;
   }
}
