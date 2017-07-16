package lt.pavilonis.cmm.canteen.repository;

import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import lt.pavilonis.cmm.security.Role;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoleRepository implements EntityRepository<Role, Long, IdTextFilter> {

   @Autowired
   private JdbcTemplate jdbc;

   @Override
   public Role saveOrUpdate(Role entity) {
      throw new NotImplementedException("not needed");
   }

   @Override
   public List<Role> load() {
      return load(IdTextFilter.empty());
   }

   @Override
   public List<Role> load(IdTextFilter ignored) {
      return jdbc.query(
            "SELECT id, name FROM Role",
            (rs, num) -> new Role(rs.getLong(1), rs.getString(2))
      );
   }

   @Override
   public Optional<Role> find(Long aLong) {
      throw new NotImplementedException("not needed");
   }

   @Override
   public void delete(Long aLong) {
      throw new NotImplementedException("not needed");
   }

   @Override
   public Class<Role> entityClass() {
      return Role.class;
   }
}
