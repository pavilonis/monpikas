package lt.pavilonis.cmm.canteen.repository;

import lt.pavilonis.cmm.user.domain.User;
import org.apache.commons.lang3.StringUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

@Repository
public class UserRepository {

   private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class.getSimpleName());
   private static final String SEGMENT_USERS = "users";
   private static final String ROLE_PUPIL = "Mokinys";
   private static final String ARG_ROLE = "role";
   private static final String ARG_NAME = "name";

   @Value("${api.path}")
   private String apiUsersPath;

   @Autowired
   private RestTemplate rest;

   public Optional<User> load(String cardCode) {
      try {
         User response = rest.getForObject(uri(cardCode), User.class);
         return Optional.of(response);
      } catch (HttpClientErrorException e) {
         LOG.error(e.getMessage());
         if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Optional.empty();
         } else {
            e.printStackTrace();
            throw e;
         }
      } catch (Exception e) {
         e.printStackTrace();
         LOG.error(e.getMessage());
         throw e;
      }
   }

   public List<User> loadAll() {
      try {
         User[] response = rest.getForObject(uri(), User[].class);
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

   public List<User> loadAllPupils(String name) {
      Map<String, Object> args = new HashMap<>();
      args.put(ARG_ROLE, ROLE_PUPIL);
      if (StringUtils.isNoneBlank(name)) {
         args.put(ARG_NAME, name);
      }
      try {
         User[] response = rest
               .getForObject(uri(args), User[].class);

         return newArrayList(response);

      } catch (Exception e) {
         e.printStackTrace();
         LOG.error(e.getMessage());
         throw e;
      }
   }
}
