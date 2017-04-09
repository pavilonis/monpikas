package lt.pavilonis.cmm.user.form;

import com.google.common.io.BaseEncoding;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Upload;
import lt.pavilonis.util.ImageUtils;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.function.BiConsumer;

public class UserFormViewImageUploader implements Upload.Receiver, Upload.SucceededListener {

   private BiConsumer<Resource, String> imageResourceConsumer;
   private ByteArrayOutputStream baos;
   private byte[] scaledImageBytes;

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
      scaledImageBytes = ImageUtils.scale(bytes, 500, 500);

      StreamResource imageResource =
            new StreamResource(() -> new ByteArrayInputStream(scaledImageBytes), "img.png");
      String base16ImageString = BaseEncoding.base16().encode(bytes);

      imageResourceConsumer.accept(imageResource, base16ImageString);
      IOUtils.closeQuietly(baos);
   }

   public byte[] getScaledImageBytes() {
      return scaledImageBytes;
   }
}
