package lt.pavilonis.cmm.canteen.service;

import lt.pavilonis.cmm.canteen.domain.SecurityUser;
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

      Map<String, SecurityUser> result = new HashMap<>();

      while (rs.next()) {
         String username = rs.getString("u.username");
         SecurityUser user = result.get(username);
         if (user == null) {
            user = new SecurityUser(
                  rs.getString("u.name"),
                  username,
                  rs.getString("u.password"),
                  rs.getString("u.email"),
                  rs.getBoolean("u.enabled"),
                  new ArrayList<>()
            );
            result.put(username, user);
         }

         String role = rs.getString("r.name");
         if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
         }

      }

      return new ArrayList<>(result.values());
   }
}