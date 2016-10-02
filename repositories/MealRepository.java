package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.Meal;
import lt.pavilonis.monpikas.server.domain.MealType;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singletonMap;

@Repository
public class MealRepository {

   private static final RowMapper<Meal> ROW_MAPPER = (rs, i) -> new Meal(
         rs.getLong("id"),
         rs.getString("name"),
         MealType.valueOf(rs.getString("type")),
         rs.getBigDecimal("price"),
         rs.getTimestamp("startTime").toLocalDateTime().toLocalTime(),
         rs.getTimestamp("endTime").toLocalDateTime().toLocalTime()
   );

   @Autowired
   private JdbcTemplate jdbc;

   @Autowired
   private NamedParameterJdbcTemplate namedJdbc;

   public Collection<Meal> deleteByIds(Set<Long> ids) {
      throw new NotImplementedException("TODO");
   }

   public List<Meal> loadAll() {
      return jdbc.query("SELECT * FROM Meal", ROW_MAPPER);
   }

   public void delete(long mealId) {
      jdbc.update("DELETE FROM Meal WHERE id = ?", mealId);
   }

   public List<Meal> load(Set<Long> ids) {
      return namedJdbc.query("SELECT * FROM Meal WHERE id IN (:ids)", singletonMap("ids", ids), ROW_MAPPER);
   }

   public Meal saveOrUpdate(Meal meal) {
      //TODO check if must be inserted or updated
   }
}
