package lt.pavilonis.cmm.ui.security;

import lt.pavilonis.cmm.common.EntityRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorityRepository implements EntityRepository<GrantedAuthority, String, Void> {

   @Autowired
   private JdbcTemplate jdbcApi;

   @Override
   public GrantedAuthority saveOrUpdate(GrantedAuthority entity) {
      throw new NotImplementedException("not needed");
   }

   @Override
   public List<GrantedAuthority> loadAll(Void aVoid) {
      return jdbcApi.query(
            "SELECT DISTINCT name FROM UserRole ORDER BY name",
            (rs, i) -> new SimpleGrantedAuthority(rs.getString(1))
      );
   }

   @Override
   public Optional<GrantedAuthority> load(String id) {
      throw new NotImplementedException("not needed");
   }

   @Override
   public void delete(String s) {
      throw new NotImplementedException("not needed");
   }

   @Override
   public Class<GrantedAuthority> getEntityClass() {
      return GrantedAuthority.class;
   }
}
