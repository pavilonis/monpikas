package lt.pavilonis.cmm;

import lt.pavilonis.TimeUtils;
import lt.pavilonis.cmm.representation.UserRepresentation;
import lt.pavilonis.cmm.representation.WorkTimeRepresentation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Repository
public class UserRestRepository {

   private static final Logger LOG = LoggerFactory.getLogger(UserRestRepository.class);
   private static final String SEGMENT_USERS = "users";
   private static final String SEGMENT_WORKTIME = "worktime";

   @Value("${api.path}")
   private String apiPath;

   @Autowired
   private RestTemplate restTemplate;

   public List<UserRepresentation> loadAll() {
      return loadAll(null, null, null);
   }

   public List<UserRepresentation> loadAll(String name, String role, String group) {
      LocalDateTime opStart = LocalDateTime.now();
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>(3);

      addParam(params, "name", name);
      addParam(params, "role", role);
      addParam(params, "group", group);

      UserRepresentation[] response = restTemplate.getForObject(uri(params, SEGMENT_USERS), UserRepresentation[].class);

      LOG.info("All users loaded [number={}, duration={}]", response.length, TimeUtils.duration(opStart));
      return Arrays.asList(response);
   }

   private void addParam(MultiValueMap<String, String> params, String paramName, String paramValue) {
      if (StringUtils.isNoneBlank(paramValue)) {
         params.set(paramName, paramValue);
      }
   }

   public UserRepresentation load(String cardCode) {

      LocalDateTime opStart = LocalDateTime.now();

      UserRepresentation result = restTemplate.getForObject(uri(SEGMENT_USERS, cardCode), UserRepresentation.class);
      LOG.info("User loaded by cardCode [duration={}]", TimeUtils.duration(opStart));

      return result;
   }

   public UserRepresentation save(UserRepresentation userRepresentation) {
      LocalDateTime opStart = LocalDateTime.now();

      UserRepresentation response = restTemplate
            .postForObject(uri(SEGMENT_USERS), userRepresentation, UserRepresentation.class);

      LOG.info("User saved [duration={}]", TimeUtils.duration(opStart));
      return response;
   }

   public void delete(String cardCode) {
      restTemplate.delete(uri(SEGMENT_USERS, cardCode));
   }

   public UserRepresentation update(UserRepresentation userRepresentation) {
      ResponseEntity<UserRepresentation> response = restTemplate.exchange(
            uri(SEGMENT_USERS),
            HttpMethod.PUT,
            new HttpEntity<>(userRepresentation),
            UserRepresentation.class
      );
      return response.getBody();
   }

   private URI uri(String... segments) {
      return uri(new LinkedMultiValueMap<>(0), segments);
   }

   private URI uri(MultiValueMap<String, String> params, String... segments) {
      return UriComponentsBuilder
            .fromUriString(apiPath)
            .pathSegment(segments)
            .queryParams(params)
            .build()
            .toUri();
   }

   public List<WorkTimeRepresentation> loadWorkTime(String cardCode) {
      WorkTimeRepresentation[] response = restTemplate
            .getForObject(uri(SEGMENT_WORKTIME, cardCode), WorkTimeRepresentation[].class);
      return Arrays.asList(response);
   }
}
