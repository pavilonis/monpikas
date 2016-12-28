package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.UserRepresentation;
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
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

@Repository
public class UserRepository {

   private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class.getSimpleName());

   @Value(("${api.path.users}"))
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

   private URI uri(String... segments) {
      return UriComponentsBuilder.fromUriString(apiUsersPath)
            .pathSegment(segments)
            .build()
            .toUri();
   }
}
