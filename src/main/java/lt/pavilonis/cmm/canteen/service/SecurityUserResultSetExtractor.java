package lt.pavilonis.cmm.canteen.service;

import lt.pavilonis.cmm.canteen.domain.SecurityUser;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
                  rs.getBoolean("u.enabled"),
                  new HashSet<>()
            );
            result.put(username, user);
         }

         user.getRoles().add(rs.getString("r.name"));
      }

      return new ArrayList<>(result.values());
   }
}