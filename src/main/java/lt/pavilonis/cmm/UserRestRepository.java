package lt.pavilonis.cmm;

import lt.pavilonis.TimeUtils;
import lt.pavilonis.cmm.representation.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
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
//   private static final String SEGMENT_GROUPS = "groups";
//   private static final String SEGMENT_GROUP_TYPES = "/groupTypes";

   @Value("${api.path}")
   private String apiPath;

   @Autowired
   private RestTemplate restTemplate;

   public List<UserRepresentation> loadAll() {
      LocalDateTime opStart = LocalDateTime.now();

      UserRepresentation[] response = restTemplate.getForObject(uri(SEGMENT_USERS), UserRepresentation[].class);

      LOG.info("All users loaded [number={}, duration={}]", response.length, TimeUtils.duration(opStart));
      return Arrays.asList(response);
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

//   public List<GroupRepresentation> loadGroups() {
//      GroupRepresentation[] response = restTemplate.getForObject(uri(SEGMENT_GROUPS), GroupRepresentation[].class);
//      return Arrays.asList(response);
//   }
//
//   public List<GroupTypeRepresentation> loadGroupTypes() {
//      GroupTypeRepresentation[] response =
//            restTemplate.getForObject(uri(SEGMENT_GROUP_TYPES), GroupTypeRepresentation[].class);
//      return Arrays.asList(response);
//   }

   private URI uri(String... segments) {
      return UriComponentsBuilder
            .fromUriString(apiPath)
            .pathSegment(segments)
            .build()
            .toUri();
   }
}
