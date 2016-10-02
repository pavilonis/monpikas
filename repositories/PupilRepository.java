package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.Pupil;
import lt.pavilonis.monpikas.server.domain.UserRepresentation;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
//TODO think of new name
public class PupilRepository {

   @Autowired
   private JdbcTemplate jdbc;

   public Optional<Pupil> findByCardCode(String cardCode) {
      throw new NotImplementedException("Not needed yet");
   }

   @Query("select distinct ppl from Pupil ppl join ppl.meals m where m.id = :id")
   public List<Pupil> findPortionUsers(@Param("id") long id);

   @Query("select p from Pupil p where p.meals is not empty")
   public List<Pupil> findWithPortions();


   @Value(("${api.path.users}"))
   private String apiPath;

   @Autowired
   private RestTemplate rest;

   public List<Pupil> getAllAdbPupils(List<Pupil> pupilData) {
      return innerQuery(pupilData, null);
   }

   public Optional<Pupil> getAdbPupil(String cardCode, Optional<Pupil> pupilData) {
      List<Pupil> mappingData = pupilData.isPresent()
            ? Collections.singletonList(pupilData.get())
            : Collections.emptyList();

      List<Pupil> result = innerQuery(mappingData, cardCode);

      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   private List<Pupil> innerQuery(List<Pupil> localPupilData, String cardCode) {

      try {
         if (cardCode == null) {
            UserRepresentation[] response = rest.getForObject(uri(), UserRepresentation[].class);
         } else {
            UserRepresentation response = rest.getForObject(uri(cardCode), UserRepresentation.class);
         }
      } catch (Exception e) {

      }


   }

   private URI uri(String... segments) {
      return UriComponentsBuilder.fromUriString(apiPath)
            .pathSegment(segments)
            .build()
            .toUri();
   }

   public List<Pupil> loadAll() {
      throw new NotImplementedException("TODO");
   }

   public Pupil saveOrUpdate(Pupil pupil) {
      //TODO check if must be inserted or updated
   }
}