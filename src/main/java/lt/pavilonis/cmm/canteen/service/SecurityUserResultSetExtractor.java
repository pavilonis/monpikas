package lt.pavilonis.cmm.canteen.service;

import lt.pavilonis.cmm.canteen.domain.SecurityUser;
import lt.pavilonis.cmm.ui.security.Role;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class SecurityUserResultSetExtractor implements ResultSetExtractor<List<SecurityUser>> {
   @Override
   public List<SecurityUser> extractData(ResultSet rs) throws SQLException, DataAccessException {

      Map<Long, SecurityUser> result = new HashMap<>();

      while (rs.next()) {
         Long id = rs.getLong("u.id");
         SecurityUser user = result.get(id);
         if (user == null) {
            user = new SecurityUser(
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

   protected void maybeAddRole(SecurityUser user, Long id, String roleName) {
      boolean isNewRole = user.getAuthorities().stream()
            .map(Role::getId)
            .noneMatch(id::equals);

      if (isNewRole) {
         user.getAuthorities().add(new Role(id, roleName));
      }
   }
}