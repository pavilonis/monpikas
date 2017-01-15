package lt.pavilonis.cmm.repository;

import lt.pavilonis.util.TimeUtils;
import lt.pavilonis.cmm.domain.ScannerRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static lt.pavilonis.cmm.util.UriUtils.uri;

@Repository
public class ScannerRestRepository {

   private static final Logger LOG = LoggerFactory.getLogger(ScannerRestRepository.class);
   private static final String SEGMENT_SCANNERS = "scanner";

   @Value("${api.path}")
   private String apiPath;

   @Autowired
   private RestTemplate restTemplate;

   public List<ScannerRepresentation> loadAll() {
      LocalDateTime opStart = LocalDateTime.now();

      ScannerRepresentation[] response =
            restTemplate.getForObject(uri(apiPath, SEGMENT_SCANNERS), ScannerRepresentation[].class);

      LOG.info("Scanners loaded [number={}, duration={}]",
            response.length, TimeUtils.duration(opStart));
      return Arrays.asList(response);
   }
}
