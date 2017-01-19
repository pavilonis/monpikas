package lt.pavilonis.cmm.canteen.repository;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.domain.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

@Repository
public class UserRepository {

   private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class.getSimpleName());
   private static final String SEGMENT_USERS = "users";
   private static final String ROLE_PUPIL = "Mokinys";
   private static final String PARAM_ROLE = "role";

   @Value("${api.path}")
   private String apiUsersPath;

   @Autowired
   private RestTemplate rest;

   public Optional<UserRepresentation> load(String cardCode) {
      try {
         UserRepresentation response = rest.getForObject(uri(cardCode), UserRepresentation.class);
         return Optional.of(response);
      } catch (HttpClientErrorException e) {
         e.printStackTrace();
         LOG.error(e.getMessage());
         if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Optional.empty();
         } else {
            throw e;
         }
      } catch (Exception e) {
         e.printStackTrace();
         LOG.error(e.getMessage());
         throw e;
      }
   }

   public List<UserRepresentation> loadAll() {
      try {
         UserRepresentation[] response = rest.getForObject(uri(), UserRepresentation[].class);
         return newArrayList(response);
      } catch (Exception e) {
         e.printStackTrace();
         LOG.error(e.getMessage());
         throw e;
      }
   }

   private URI uri(Map<String, Object> queryParams, String... segments) {
      UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(apiUsersPath)
            .pathSegment(SEGMENT_USERS)
            .pathSegment(segments);

      queryParams.forEach(uriBuilder::queryParam);

      return uriBuilder
            .build()
            .toUri();
   }

   private URI uri(String... segments) {
      return uri(Collections.emptyMap(), segments);
   }

   public List<UserRepresentation> loadAllPupils() {
      try {
         UserRepresentation[] response = rest
               .getForObject(uri(ImmutableMap.of(PARAM_ROLE, ROLE_PUPIL)), UserRepresentation[].class);

         return newArrayList(response);

      } catch (Exception e) {
         e.printStackTrace();
         LOG.error(e.getMessage());
         throw e;
      }
   }
}
