package lt.pavilonis.cmm.canteen.service;

import lt.pavilonis.cmm.canteen.domain.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
                  rs.getBoolean("u.enabled")
            );
            result.put(username, user);
         }

         maybeAddAuthority(
               user,
               rs.getString("r.name")
         );
      }

      return new ArrayList<>(result.values());
   }

   protected void maybeAddAuthority(SecurityUser user, String roleName) {
      if (StringUtils.isNoneBlank(roleName)) {
         GrantedAuthority authority = new SimpleGrantedAuthority(roleName);
         if (!user.getAuthorities().contains(authority)) {
            user.getAuthorities().add(authority);
         }
      }
   }
}