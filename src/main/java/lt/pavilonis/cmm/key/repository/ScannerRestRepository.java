package lt.pavilonis.cmm.key.repository;

import lt.pavilonis.util.TimeUtils;
import lt.pavilonis.cmm.key.domain.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static lt.pavilonis.cmm.common.util.UriUtils.uri;

@Repository
public class ScannerRestRepository {

   private static final Logger LOG = LoggerFactory.getLogger(ScannerRestRepository.class);
   private static final String SEGMENT_SCANNERS = "scanner";

   @Value("${api.path}")
   private String apiPath;

   @Autowired
   private RestTemplate restTemplate;

   public List<Scanner> loadAll() {
      LocalDateTime opStart = LocalDateTime.now();

      Scanner[] response =
            restTemplate.getForObject(uri(apiPath, SEGMENT_SCANNERS), Scanner[].class);

      LOG.info("Scanners loaded [number={}, duration={}]",
            response.length, TimeUtils.duration(opStart));
      return Arrays.asList(response);
   }
}
