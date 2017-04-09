package lt.pavilonis.cmm.common.service;

import com.google.common.io.BaseEncoding;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class ImageService {

   public Resource imageResource(String base16photo) {
      return StringUtils.isNoneBlank(base16photo) && BaseEncoding.base16().canDecode(base16photo)
            ? new StreamResource(() -> new ByteArrayInputStream(BaseEncoding.base16().decode(base16photo)), "img.png")
            : new ThemeResource("user_yellow_256.png");
   }

}
