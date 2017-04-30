package lt.pavilonis.cmm.user.repository;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.Query;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.user.domain.PresenceTime;
import lt.pavilonis.cmm.user.domain.User;
import lt.pavilonis.cmm.user.ui.UserFilter;
import lt.pavilonis.util.TimeUtils;
import org.apache.commons.lang3.NotImplementedException;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class UserRestRepository implements EntityRepository<User, String, UserFilter> {

   private static final Logger LOG = LoggerFactory.getLogger(UserRestRepository.class);
   private static final String SEGMENT_SIZE = "size";
   private static final String SEGMENT_USERS = "users";
   private static final String SEGMENT_SCANLOG = "scanlog";
   private static final String SCANNER_ID_CANTEEN = "6";
   private static final String SEGMENT_PRESENCE = "presence";

   @Value("${api.path}")
   private String apiPath;

   @Autowired
   private RestTemplate restTemplate;

   @Override
   public List<User> load(UserFilter filter) {
      LocalDateTime opStart = LocalDateTime.now();
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>(3);

      addParam(params, "name", filter.getName());
      addParam(params, "role", filter.getRole());
      addParam(params, "group", filter.getGroup());

      User[] response = restTemplate.getForObject(uri(params, SEGMENT_USERS), User[].class);

      LOG.info("Loaded all users [number={}, duration={}]", response.length, TimeUtils.duration(opStart));
      return Arrays.asList(response);
   }

   private void addParam(MultiValueMap<String, String> params, String paramName, Object paramValue) {
      if (paramValue == null) {
         return;
      }

      String stringValue = String.valueOf(paramValue);
      if (StringUtils.isNoneBlank(stringValue)) {
         params.set(paramName, stringValue);
      }
   }

   @Override
   public Optional<User> find(String cardCode) {

      LocalDateTime opStart = LocalDateTime.now();

      User result = restTemplate
            .getForObject(uri(SEGMENT_USERS, cardCode), User.class);

      LOG.info("User loaded [cardCode={}, duration={}]", cardCode, TimeUtils.duration(opStart));
      return Optional.ofNullable(result);
   }

   public void delete(String cardCode) {
      restTemplate.delete(uri(SEGMENT_USERS, cardCode));
   }

   @Override
   public Class<User> entityClass() {
      return User.class;
   }

   public User update(User userRepresentation) {
      ResponseEntity<User> response = restTemplate.exchange(
            uri(SEGMENT_USERS),
            HttpMethod.PUT,
            new HttpEntity<>(userRepresentation),
            User.class
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

   public List<PresenceTime> loadPresenceTime(String cardCode) {
      PresenceTime[] response = restTemplate
            .getForObject(uri(SEGMENT_PRESENCE, cardCode), PresenceTime[].class);
      return Arrays.asList(response);
   }

   public void logUserScan(String cardCode) {
      LOG.info("Sending scanLog post request");
      try {
         restTemplate.postForObject(uri(SEGMENT_SCANLOG, SCANNER_ID_CANTEEN, cardCode), null, Void.class);
      } catch (HttpClientErrorException e) {
         LOG.error("Error writing log for user with card: " + cardCode + ". Http status: " + e.getStatusCode());
      }
   }

   @Override
   public User saveOrUpdate(User entity) {
      throw new NotImplementedException("Not needed yet");
   }

   @Override
   public Optional<DataProvider<User, UserFilter>> lazyDataProvider() {
      DataProvider<User, UserFilter> provider =
            new AbstractBackEndDataProvider<User, UserFilter>() {
               @Override
               protected Stream<User> fetchFromBackEnd(Query<User, UserFilter> query) {

                  LocalDateTime opStart = LocalDateTime.now();

                  MultiValueMap<String, String> params = collectParams(query);

                  User[] response =
                        restTemplate.getForObject(uri(params, SEGMENT_USERS), User[].class);

                  LOG.info("Loaded users [number={}, offset={}, limit={}, duration={}]",
                        response.length, query.getOffset(), query.getLimit(), TimeUtils.duration(opStart));
                  return Stream.of(response);
               }

               @Override
               protected int sizeInBackEnd(Query<User, UserFilter> query) {
                  LocalDateTime opStart = LocalDateTime.now();

                  MultiValueMap<String, String> params = collectParams(query);

                  int size = restTemplate.getForObject(uri(params, SEGMENT_USERS, SEGMENT_SIZE), Integer.class);
                  LOG.info("Checked number of users [number={}, duration={}]", size, TimeUtils.duration(opStart));
                  return size;
               }
            };
      return Optional.of(provider);
   }

   private MultiValueMap<String, String> collectParams(Query<User, UserFilter> query) {
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      addParam(params, "offset", query.getOffset());
      addParam(params, "limit", query.getLimit());
      query.getFilter().ifPresent(filter -> {
         addParam(params, "name", filter.getName());
         addParam(params, "role", filter.getRole());
         addParam(params, "group", filter.getGroup());
      });
      return params;
   }
}
