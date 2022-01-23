package lt.pavilonis.monpikas.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.common.util.QueryUtils;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Repository
public class LoginEventRepository implements EntityRepository<LoginEvent, Long, LoginEventFilter> {

   private final NamedParameterJdbcTemplate jdbc;

   @Override
   public LoginEvent saveOrUpdate(LoginEvent entity) {
      var sql = "INSERT INTO LoginEvent (name, address, success, logout) VALUE (:name, :address, :success, :logout)";
      var params = new HashMap<String, Object>();
      params.put("name", entity.getName());
      params.put("address", entity.getAddress());
      params.put("success", entity.isSuccess());
      params.put("logout", entity.isLogout());

      jdbc.update(sql, params);
      return null;
   }

   @Override
   public List<LoginEvent> load(LoginEventFilter filter) {

      var sql = "SELECT * FROM LoginEvent " +
            "WHERE (:periodStart IS NULL OR created >= :periodStart)" +
            "  AND (:periodEnd IS NULL OR created <= :periodEnd) " +
            "  AND (:text IS NULL OR name LIKE :text OR address LIKE :text) " +
            "  AND logout = :logout " +
            "  AND (:logout OR success = :success) " +
            "ORDER BY created DESC";

      LocalDateTime periodEnd = filter.getPeriodEnd() == null
            ? null
            : filter.getPeriodEnd().atTime(LocalTime.MAX);

      var params = new HashMap<String, Object>();
      params.put("text", QueryUtils.likeArg(filter.getText()));
      params.put("periodStart", filter.getPeriodStart());
      params.put("periodEnd", periodEnd);
      params.put("success", filter.isSuccess());
      params.put("logout", filter.isLogout());

      return jdbc.query(sql, params, (rs, i) -> LoginEvent.builder()
            .id(rs.getLong("id"))
            .created(rs.getTimestamp("created").toLocalDateTime())
            .name(rs.getString("name"))
            .address(rs.getString("address"))
            .success(rs.getBoolean("success"))
            .logout(rs.getBoolean("logout"))
            .build()
      );
   }

   @Override
   public List<LoginEvent> load() {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public Optional<LoginEvent> find(Long id) {
      return Optional.empty();
   }

   @Override
   public void delete(Long aLong) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public Class<LoginEvent> entityClass() {
      return LoginEvent.class;
   }
}
