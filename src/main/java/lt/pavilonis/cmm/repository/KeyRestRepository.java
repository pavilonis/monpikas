package lt.pavilonis.cmm.repository;

import lt.pavilonis.util.TimeUtils;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Repository
public class KeyRestRepository {

   private static final Logger LOG = LoggerFactory.getLogger(KeyRestRepository.class);
   private static final String SEGMENT_KEYS = "keys";
   private static final String SEGMENT_LOG = "log";

   @Value("${api.path}")
   private String apiPath;

   @Autowired
   private RestTemplate restTemplate;

   public List<KeyRepresentation> load(Long scannerId, String keyNumber,
                                       boolean isLog, LocalDate periodStart, LocalDate periodEnd) {
      LocalDateTime opStart = LocalDateTime.now();

      List<Object> segments = newArrayList(SEGMENT_KEYS);
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      if (scannerId != null) {
         params.add("scannerId", scannerId.toString());
      }
      if (StringUtils.isNotBlank(keyNumber)) {
         params.add("keyNumber", keyNumber);
      }
      if (isLog) {
         segments.add(SEGMENT_LOG);
         params.add("periodStart", DateTimeFormatter.ISO_DATE.format(periodStart));
         params.add("periodEnd", DateTimeFormatter.ISO_DATE.format(periodEnd));
      }


      KeyRepresentation[] response = restTemplate.getForObject(
            UriUtils.uri(apiPath, params, segments.toArray()),
            KeyRepresentation[].class
      );

      LOG.info("Keys loaded [number={}, duration={}]", response.length, TimeUtils.duration(opStart));
      return Arrays.asList(response);
   }
}
