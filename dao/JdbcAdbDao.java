package lt.pavilonis.monpikas.server.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Repository
public class JdbcAdbDao implements AdbDao {

   @Autowired
   private JdbcTemplate jdbcTemplate;

   @Override
   public List<PupilDto> getAllAdbPupils() {
      return jdbcTemplate.query("SELECT card, fname, lname, gdata FROM gs_ecard_mok_users", new UserDtoMapper());
   }

   public List<PupilDto> getAdbPupilsByCardIds(Set<Long> ids){ //TODO potestit eto, potom peredelat chtoby neskkolkimi partijami po 50 id zaprosy shli
      MapSqlParameterSource parameters = new MapSqlParameterSource();
      parameters.addValue("ids", ids);
      return jdbcTemplate.query("SELECT card, fname, lname, gdata FROM gs_ecard_mok_users WHERE card IN (:ids)",
            new UserDtoMapper(), parameters);
   }

   @Override
   public PupilDto getAdbPupil(long cardId) {
      return jdbcTemplate.queryForObject(
            "SELECT card, fname, lname, gdata FROM gs_ecard_mok_users WHERE card = ?",
            new Object[]{String.valueOf(cardId)}, new UserDtoMapper());
   }

   private static final class UserDtoMapper implements RowMapper<PupilDto> {
      @Override
      public PupilDto mapRow(ResultSet rs, int rowNum) throws SQLException {
         PupilDto user = new PupilDto();
         user.setCardId(Integer.valueOf(rs.getString("card")));
         user.setFirstName(rs.getString("fname"));
         user.setLastName(rs.getString("lname"));
         Date d = (rs.getDate("gdata"));
         user.setBirthDate(d == null ? null : d.toLocalDate());
         return user;
      }
   }
}
