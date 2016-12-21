package lt.pavilonis.cmm.repository;

import lt.pavilonis.TimeUtils;
import lt.pavilonis.cmm.domain.KeyRepresentation;
import lt.pavilonis.cmm.util.UriUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;

@Repository
public class KeyRestRepository {

   private static final Logger LOG = LoggerFactory.getLogger(KeyRestRepository.class);
   private static final String SEGMENT_KEYS = "users";

   @Value("${api.path}")
   private String apiPath;

   @Autowired
   private RestTemplate restTemplate;

   public List<KeyRepresentation> load(Long scannerId, String keyNumber) {
      LocalDateTime opStart = LocalDateTime.now();

      MultiValueMap<String, String> params = new LinkedMultiValueMap<>(1);
      if (StringUtils.isNotBlank(keyNumber)) {
         params.add("keyNumber", keyNumber);
      }

      URI uri = isNull(scannerId)
            ? UriUtils.uri(apiPath, params, SEGMENT_KEYS)
            : UriUtils.uri(apiPath, params, SEGMENT_KEYS, scannerId);

      KeyRepresentation[] response = restTemplate.getForObject(uri, KeyRepresentation[].class);

      LOG.info("Keys loaded [number={}, duration={}]", response.length, TimeUtils.duration(opStart));
      return Arrays.asList(response);
   }

   public List<KeyRepresentation> loadAll() {
      return load(null, null);
   }
}
