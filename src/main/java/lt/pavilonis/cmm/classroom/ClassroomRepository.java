package lt.pavilonis.cmm.classroom;

import com.google.common.collect.Lists;
import lt.pavilonis.cmm.common.EntityRepository;
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

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class ClassroomRepository implements EntityRepository<ClassroomOccupancy, Void, ClassroomFilter> {

   private static final Logger LOG = LoggerFactory.getLogger(ClassroomRepository.class);
   private static final String SEGMENT_CLASSROOMS = "classrooms";
   private static final String SEGMENT_LOG = "log";

   @Value("${api.path}")
   private String apiPath;

   @Autowired
   private RestTemplate restTemplate;

   @Override
   public List<ClassroomOccupancy> load(ClassroomFilter filter) {
      LocalDateTime opStart = LocalDateTime.now();

      List<Object> segments = Lists.newArrayList(SEGMENT_CLASSROOMS);
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

      if (StringUtils.isNotBlank(filter.getText())) {
         params.add("classroomNumber", filter.getText());
      }

      if (filter.isLogMode()) {
         segments.add(SEGMENT_LOG);
         params.add("periodStart", DateTimeFormatter.ISO_DATE.format(filter.getPeriodStart()));
         params.add("periodEnd", DateTimeFormatter.ISO_DATE.format(filter.getPeriodEnd()));
      }

      URI uri = UriUtils.uri(apiPath, params, segments.toArray());
      ClassroomOccupancy[] response = restTemplate.getForObject(uri, ClassroomOccupancy[].class);

      LOG.info("Class occupancy entries loaded [number={}, duration={}]",
            response.length, TimeUtils.duration(opStart));
      return Arrays.asList(response);
   }

   @Override
   public ClassroomOccupancy saveOrUpdate(ClassroomOccupancy entity) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public Optional<ClassroomOccupancy> find(Void s) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public void delete(Void s) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public Class<ClassroomOccupancy> getEntityClass() {
      return ClassroomOccupancy.class;
   }
}
