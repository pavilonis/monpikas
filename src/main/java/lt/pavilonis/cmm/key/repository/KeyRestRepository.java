package lt.pavilonis.cmm.key.repository;

import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.key.domain.Key;
import lt.pavilonis.cmm.key.ui.KeyListFilter;
import lt.pavilonis.cmm.common.util.UriUtils;
import lt.pavilonis.util.TimeUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

@Repository
public class KeyRestRepository implements EntityRepository<Key, String, KeyListFilter> {

   private static final Logger LOG = LoggerFactory.getLogger(KeyRestRepository.class);
   private static final String SEGMENT_KEYS = "keys";
   private static final String SEGMENT_LOG = "log";

   @Value("${api.path}")
   private String apiPath;

   @Autowired
   private RestTemplate restTemplate;

   @Override
   public List<Key> load(KeyListFilter filter) {
      LocalDateTime opStart = LocalDateTime.now();

      List<Object> segments = newArrayList(SEGMENT_KEYS);
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      if (filter.getScannerId() != null) {
         params.add("scannerId", filter.getScannerId().toString());
      }
      if (StringUtils.isNotBlank(filter.getText())) {
         params.add("keyNumber", filter.getText());
      }
      if (filter.isLogMode()) {
         segments.add(SEGMENT_LOG);
         params.add("periodStart", DateTimeFormatter.ISO_DATE.format(filter.getPeriodStart()));
         params.add("periodEnd", DateTimeFormatter.ISO_DATE.format(filter.getPeriodEnd()));
      }

      Key[] response = restTemplate.getForObject(
            UriUtils.uri(apiPath, params, segments.toArray()),
            Key[].class
      );

      LOG.info("Keys loaded [number={}, duration={}]", response.length, TimeUtils.duration(opStart));
      return Arrays.asList(response);
   }

   @Override
   public Key saveOrUpdate(Key entity) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public Optional<Key> find(String s) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public void delete(String s) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public Class<Key> getEntityClass() {
      return Key.class;
   }
}
