package lt.pavilonis.monpikas.server.dao;

import lt.pavilonis.monpikas.server.dto.PupilDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Repository
public class JdbcAdbDao implements AdbDao {

   private static final String BASE_QUERY =
         "SELECT u.id, u.card, u.fname, u.lname, u.gdata, g.text AS grade " +
               "FROM gs_ecard_mok_users u " +
               "JOIN gs_ecard_mok_static g ON u.group_id = g.id";

   private static final RowMapper<PupilDto> ROW_MAPPER = (rs, rowNum) -> {
      PupilDto dto = new PupilDto();
      dto.setAdbId(rs.getLong("id"));
      dto.setCardId(rs.getInt("card"));
      dto.setFirstName(rs.getString("fname"));
      dto.setLastName(rs.getString("lname"));
      dto.setGrade(rs.getString("grade"));
      dto.setBirthDate(ofNullable(rs.getDate("gdata")));
      return dto;
   };

   @Autowired
   private JdbcTemplate jdbc;

   @Override
   public List<PupilDto> getAllAdbPupils() {
      return jdbc.query(BASE_QUERY, ROW_MAPPER);
   }

   @Override
   public Optional<PupilDto> getAdbPupil(long cardId) {
      PupilDto dto = jdbc.queryForObject(BASE_QUERY + " WHERE card = " + getString(cardId), ROW_MAPPER);
      return ofNullable(dto);
   }

   private String getString(Long id) {
      String s = String.valueOf(id);
      while (s.length() < 4) {
         s = 0 + s;
      }
      return s;
   }
}
