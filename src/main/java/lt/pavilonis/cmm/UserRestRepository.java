package lt.pavilonis.cmm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

   @Value("${api.users.path}")
   private String usersApiPath;

   @Autowired
   private RestTemplate restTemplate;

   List<UserRepresentation> loadAll() {
      LocalDateTime opStart = LocalDateTime.now();

      UserRepresentation[] response = restTemplate.getForObject(URI.create(usersApiPath), UserRepresentation[].class);

      LOG.info("All users loaded [number={}, duration={}]", response.length, TimeUtils.duration(opStart));
      return Arrays.asList(response);
   }

   UserRepresentation load(String cardCode) {

      LocalDateTime opStart = LocalDateTime.now();

      URI uri = UriComponentsBuilder
            .fromUriString(usersApiPath)
            .pathSegment(cardCode)
            .build()
            .toUri();

      UserRepresentation result = restTemplate.getForObject(uri, UserRepresentation.class);
      LOG.info("User loaded by cardCode [duration={}]", TimeUtils.duration(opStart));

      return result;
   }

   UserRepresentation save(UserRepresentation userRepresentation) {
      LocalDateTime opStart = LocalDateTime.now();

      UserRepresentation response = restTemplate.postForObject(URI.create(usersApiPath), userRepresentation, UserRepresentation.class);

      LOG.info("User saved [duration={}]", TimeUtils.duration(opStart));
      return response;
   }

   public void delete() {

   }
}
