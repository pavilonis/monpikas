package lt.pavilonis.monpikas.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.common.ui.filter.IdPeriodTextFilter;
import lt.pavilonis.monpikas.common.util.QueryUtils;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Repository
public class FailedLoginRepository implements EntityRepository<FailedLogin, Long, IdPeriodTextFilter> {

   private final NamedParameterJdbcTemplate jdbc;

   @Override
   public FailedLogin saveOrUpdate(FailedLogin entity) {
      var params = new HashMap<String, String>();
      params.put("name", entity.getName());
      params.put("address", entity.getAddress());
      jdbc.update("INSERT INTO FailedLogin (name, address) VALUE (:name, :address)", params);
      return null;
   }

   @Override
   public List<FailedLogin> load(IdPeriodTextFilter filter) {

      var sql = "SELECT * FROM FailedLogin " +
            "WHERE (:periodStart IS NULL OR created >= :periodStart)" +
            "  AND (:periodEnd IS NULL OR created <= :periodEnd) " +
            "  AND (:text IS NULL OR name LIKE :text OR address LIKE :text)" +
            "ORDER BY created DESC";

      LocalDateTime periodEnd = filter.getPeriodEnd() == null
            ? null
            : filter.getPeriodEnd().atTime(LocalTime.MAX);

      var params = new HashMap<String, Object>();
      params.put("text", QueryUtils.likeArg(filter.getText()));
      params.put("periodStart", filter.getPeriodStart());
      params.put("periodEnd", periodEnd);

      return jdbc.query(sql, params, (rs, i) -> FailedLogin.builder()
            .id(rs.getLong("id"))
            .created(rs.getTimestamp("created").toLocalDateTime())
            .name(rs.getString("name"))
            .address(rs.getString("address"))
            .build()
      );
   }

   @Override
   public List<FailedLogin> load() {
      LocalDate today = LocalDate.now();
      IdPeriodTextFilter filter = IdPeriodTextFilter.builder()
            .periodStart(today.minusDays(1))
            .periodEnd(today)
            .build();
      return load(filter);
   }

   @Override
   public Optional<FailedLogin> find(Long id) {
      return Optional.empty();
   }

   @Override
   public void delete(Long aLong) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public Class<FailedLogin> entityClass() {
      return FailedLogin.class;
   }
}
