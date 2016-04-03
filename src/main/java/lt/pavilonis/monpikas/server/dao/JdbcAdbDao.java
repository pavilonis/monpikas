package lt.pavilonis.monpikas.server.dao;

import lt.pavilonis.monpikas.server.domain.Meal;
import lt.pavilonis.monpikas.server.domain.Pupil;
import lt.pavilonis.monpikas.server.dto.PupilDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

@Repository
public class JdbcAdbDao implements AdbDao {

   private static final String BASE_QUERY = "" +
         "SELECT u.id, u.card, u.fname, u.lname, u.gdata, g.text AS grade " +
         "FROM gs_ecard_mok_users u " +
         "  JOIN gs_ecard_mok_static g ON u.group_id = g.id";

   @Autowired
   private JdbcTemplate jdbc;

   @Override
   public List<PupilDto> getAllAdbPupils(List<Pupil> pupilData) {
      return jdbc.query(BASE_QUERY, mapper(pupilData));
   }

   @Override
   public Optional<PupilDto> getAdbPupil(long cardId, Optional<Pupil> pupilData) {
      List<Pupil> mappingData = pupilData.isPresent()
            ? Collections.singletonList(pupilData.get())
            : Collections.emptyList();

//      newArrayList(pupilData.orElse(null));

      return Optional.ofNullable(
            jdbc.queryForObject(
                  BASE_QUERY + " WHERE card = ?",
                  mapper(mappingData),
                  getString(cardId)
            )
      );
   }

   private String getString(Long id) {
      String s = String.valueOf(id);
      while (s.length() < 4) {
         s = 0 + s;
      }
      return s;
   }

   private RowMapper<PupilDto> mapper(List<Pupil> pupilData) {
      return (rs, rowNum) -> {

         long cardId = rs.getLong("card");

         Optional<Pupil> pupil = pupilData.stream()
               .filter(data -> data.getCardId() == cardId)
               .findAny();

         return new PupilDto(
               rs.getLong("id"),
               cardId,
               rs.getString("fname"),
               rs.getString("lname"),
               rs.getString("grade"),
               Optional.ofNullable(rs.getDate("gdata")),

               pupil.isPresent() ? pupil.get().getType() : null,
               pupil.isPresent() ? pupil.get().getMeals() : Collections.<Meal>emptySet(),
               pupil.isPresent() ? Optional.ofNullable(pupil.get().getComment()) : Optional.empty()
         );
      };
   }
}
