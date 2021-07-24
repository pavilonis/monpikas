package lt.pavilonis.monpikas.user;

import lt.pavilonis.monpikas.common.util.QueryUtils;
import lt.pavilonis.monpikas.common.util.SimpleRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper extends SimpleRowMapper<User> {

   @Override
   public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new User(
            rs.getLong("id"),
            rs.getString("cardCode"),
            rs.getString("name"),
            rs.getString("organizationGroup"),
            rs.getString("organizationRole"),
            rs.getString("photo"),
            QueryUtils.getLocalDate(rs, "birthDate"),
            mapSupervisor(rs)
      );
   }

   private User mapSupervisor(ResultSet rs) throws SQLException {
      Long supervisorId = (Long) rs.getObject("supervisorId");
      return supervisorId == null
            ? null
            : new User(supervisorId, rs.getString("supervisorName"));
   }
}
