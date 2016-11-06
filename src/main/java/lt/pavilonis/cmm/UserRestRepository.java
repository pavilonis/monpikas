package lt.pavilonis.cmm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Repository
public class UserRestRepository {

   private static final Logger LOG = LoggerFactory.getLogger(UserRestRepository.class);

   @Value("${api.users.path}")
   private String usersApiPath;

   @Autowired
   private RestTemplate restTemplate;

   List<UserRepresentation> loadAll() {

      UserRepresentation[] response = restTemplate.getForObject(URI.create(usersApiPath), UserRepresentation[].class);

      return Arrays.asList(response);
   }

   UserRepresentation load(String cardCode) {
      URI uri = UriComponentsBuilder
            .fromUriString(usersApiPath)
            .pathSegment(cardCode)
            .build()
            .toUri();
      return restTemplate.getForObject(uri, UserRepresentation.class);
   }

   UserRepresentation save(UserRepresentation userRepresentation) {
      return restTemplate.postForObject(URI.create(usersApiPath), userRepresentation, UserRepresentation.class);
   }
}
