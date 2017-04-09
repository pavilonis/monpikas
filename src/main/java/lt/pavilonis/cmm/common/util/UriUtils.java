package lt.pavilonis.cmm.common.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.stream.Stream;

public class UriUtils {

   public static URI uri(String basePath, Object... segments) {
      return uri(basePath, new LinkedMultiValueMap<>(0), segments);
   }

   public static URI uri(String basePath, MultiValueMap<String, String> params, Object... segments) {

      String[] stringSegments = Stream.of(segments)
            .map(String::valueOf)
            .toArray(String[]::new);

      return UriComponentsBuilder
            .fromUriString(basePath)
            .pathSegment(stringSegments)
            .queryParams(params)
            .build()
            .toUri();
   }
}
