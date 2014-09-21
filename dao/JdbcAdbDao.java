package lt.pavilonis.monpikas.server.dao;

import lt.pavilonis.monpikas.server.domain.AdbPupilDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class JdbcAdbDao implements AdbDao {

   @Autowired
   private JdbcTemplate jdbcTemplate;

   @Override
   public List<AdbPupilDto> getAllAdbPupils() {
      return jdbcTemplate.query("SELECT card, fname, lname, gdata FROM gs_ecard_mok_users", newRowMapper());
   }

   @Override
   public AdbPupilDto getAdbPupil(long cardId) {
      List<AdbPupilDto> pupilList = jdbcTemplate.query(
            "SELECT card, fname, lname, gdata FROM gs_ecard_mok_users WHERE card = " + getString(cardId), newRowMapper()
      );
      return pupilList.isEmpty()
            ? null
            : pupilList.get(0);
   }

   private RowMapper<AdbPupilDto> newRowMapper() {
      return (rs, rowNum) -> {
         AdbPupilDto pupil = new AdbPupilDto();
         pupil.setCardId(Integer.valueOf(rs.getString("card")));
         pupil.setFirstName(rs.getString("fname"));
         pupil.setLastName(rs.getString("lname"));
         Date d = (rs.getDate("gdata"));
         pupil.setBirthDate(d == null ? null : d.toLocalDate());
         pupil.setComment("");
         return pupil;
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
