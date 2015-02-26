package lt.pavilonis.monpikas.server.dao;

import lt.pavilonis.monpikas.server.dto.AdbPupilDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Repository
public class JdbcAdbDao implements AdbDao {

   @Autowired
   private JdbcTemplate jdbcTemplate;

   @Override
   public List<AdbPupilDto> getAllAdbPupils() {
      return jdbcTemplate.query("SELECT id, card, fname, lname, gdata FROM gs_ecard_mok_users", newRowMapper());
   }

   @Override
   public Optional<AdbPupilDto> getAdbPupil(long cardId) {
      List<AdbPupilDto> pupilList = jdbcTemplate.query(
            "SELECT id, card, fname, lname, gdata FROM gs_ecard_mok_users WHERE card = " + getString(cardId), newRowMapper()
      );
      return pupilList.isEmpty()
            ? empty()
            : ofNullable(pupilList.get(0));
   }

   private RowMapper<AdbPupilDto> newRowMapper() {
      return (rs, rowNum) -> {
         AdbPupilDto dto = new AdbPupilDto();
         dto.setAdbId(rs.getLong("id"));
         dto.setCardId(rs.getInt("card"));
         dto.setFirstName(rs.getString("fname"));
         dto.setLastName(rs.getString("lname"));
         Optional<Date> date = ofNullable(rs.getDate("gdata"));
         dto.setBirthDate(date.isPresent() ? of(date.get().toLocalDate()) : empty());
         return dto;
      };
   }

   private String getString(Long id) {
      String s = String.valueOf(id);
      while (s.length() < 4) {
         s = 0 + s;
      }
      return s;
   }
}
