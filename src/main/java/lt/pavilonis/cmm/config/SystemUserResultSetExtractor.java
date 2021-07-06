package lt.pavilonis.cmm.config;

import lt.pavilonis.cmm.security.SystemUser;
import lt.pavilonis.cmm.security.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SystemUserResultSetExtractor implements ResultSetExtractor<List<SystemUser>> {
   @Override
   public List<SystemUser> extractData(ResultSet rs) throws SQLException, DataAccessException {

      Map<Long, SystemUser> result = new HashMap<>();

      while (rs.next()) {
         Long id = rs.getLong("u.id");
         SystemUser user = result.get(id);
         if (user == null) {
            user = new SystemUser(
                  id,
                  rs.getString("u.name"),
                  rs.getString("u.username"),
                  rs.getString("u.password"),
                  rs.getString("u.email"),
                  rs.getBoolean("u.enabled")
            );
            result.put(id, user);
         }

         maybeAddRole(
               user,
               rs.getLong("r.id"),
               rs.getString("r.name")
         );
      }

      return new ArrayList<>(result.values());
   }

   protected void maybeAddRole(SystemUser user, Long id, String roleName) {
      if (StringUtils.isBlank(roleName)) {
         return;
      }

      boolean isNewRole = user.getAuthorities().stream()
            .map(Role::getId)
            .noneMatch(id::equals);

      if (!isNewRole) {
         return;
      }

      user.getAuthorities().add(new Role(id, roleName));
   }
}