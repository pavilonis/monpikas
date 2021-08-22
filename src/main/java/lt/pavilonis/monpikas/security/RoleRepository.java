package lt.pavilonis.monpikas.security;

import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.common.ui.filter.IdTextFilter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoleRepository implements EntityRepository<Role, Long, IdTextFilter> {

   private final JdbcTemplate jdbc;

   public RoleRepository(JdbcTemplate jdbc) {
      this.jdbc = jdbc;
   }

   @Override
   public Role saveOrUpdate(Role entity) {
      throw new IllegalStateException("Not implemented - not needed");
   }

   @Override
   public List<Role> load() {
      return load(IdTextFilter.empty());
   }

   @Override
   public List<Role> load(IdTextFilter ignored) {
      return jdbc.query("SELECT id, name FROM Role", (rs, i) -> new Role(rs.getLong(1), rs.getString(2)));
   }

   @Override
   public Optional<Role> find(Long aLong) {
      throw new IllegalStateException("Not implemented - not needed");
   }

   @Override
   public void delete(Long aLong) {
      throw new IllegalStateException("Not implemented - not needed");
   }

   @Override
   public Class<Role> entityClass() {
      return Role.class;
   }
}
