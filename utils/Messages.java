package lt.pavilonis.monpikas.server.utils;

import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

public class Messages {

   private static final Properties PROPERTIES = new Properties();
   private static final Logger LOG = getLogger(Messages.class.getSimpleName());

   static {
      Resource resource = new ClassPathResource("messages.properties");
      InputStream inputStream = null;
      Reader reader = null;

      try {
         inputStream = new FileInputStream(resource.getFile());
         reader = new InputStreamReader(inputStream, "UTF-8");
         PROPERTIES.load(reader);
      } catch (IOException e) {
         LOG.error("Could not initialize messages: " + e);
      } finally {
         try {
            if (inputStream != null) inputStream.close();
            if (reader != null) reader.close();
         } catch (IOException e) {
            LOG.error("Could not close stream or reader");
         }
      }

   }

   public static String label(String key) {
      return PROPERTIES.getProperty(key);
   }
}
