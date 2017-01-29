package lt.pavilonis.cmm.canteen.repository;


import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.views.event.MealEventFilter;
import lt.pavilonis.cmm.common.EntityRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MealEventLogRepository implements EntityRepository<MealEventLog, Long, MealEventFilter> {

   private final RowMapper<MealEventLog> MAPPER = (rs, i) -> new MealEventLog(
         rs.getLong("id"),
         rs.getString("cardCode"),
         rs.getString("name"),
         rs.getString("grade"),
         rs.getTimestamp("date"),
         rs.getBigDecimal("price"),
         MealType.valueOf(rs.getString("mealType")),
         PupilType.valueOf(rs.getString("pupilType"))
   );

   @Autowired
   private JdbcTemplate jdbc;

   @Autowired
   private NamedParameterJdbcTemplate namedJdbc;

   public int numOfMealEvents(String cardCode, Date periodStart, Date periodEnd, MealType mealType) {
      return jdbc.queryForObject("" +
                  "SELECT count(*) " +
                  "FROM MealEventLog " +
                  "WHERE `date` BETWEEN ? AND ? " +
                  "  AND cardCode = ? " +
                  "  AND mealType = ?",
            Integer.class,
            periodStart, periodEnd, cardCode, mealType.name()
      );
   }

   public List<MealEventLog> load(PupilType pupilType, Date periodStart, Date periodEnd) {
      return jdbc.query("" +
                  "SELECT * " +
                  "FROM MealEventLog " +
                  "WHERE pupilType = ? " +
                  "  AND `date` BETWEEN ? AND ?",
            MAPPER,
            pupilType.name(), periodStart, periodEnd
      );
   }

   @Override
   public List<MealEventLog> loadAll(MealEventFilter filter) {
      Map<String, Object> params = new HashMap<>();
      params.put("periodStart", filter.getPeriodStart());
      params.put("periodEnd", filter.getPeriodEnd());
      params.put("text", StringUtils.isBlank(filter.getText()) ? null : "%" + filter.getText() + "%");

      return namedJdbc.query("" +
                  "SELECT * " +
                  "FROM MealEventLog " +
                  "WHERE " +
                  "  (:periodStart IS NULL OR :periodStart <= date) " +
                  "  AND (:periodEnd IS NULL OR :periodEnd >= date) " +
                  "  AND (:text IS NULL OR name LIKE :text)",
            params,
            MAPPER
      );
   }

   @Override
   public MealEventLog saveOrUpdate(MealEventLog entity) {
      Map<String, Object> args = new HashMap<>();
      args.put("cardCode", entity.getCardCode());
      args.put("name", entity.getName());
      args.put("price", entity.getPrice());
      args.put("mealType", entity.getMealType().name());
      args.put("grade", entity.getGrade());
      args.put("pupilType", entity.getPupilType().name());
      args.put("date", entity.getDate());

      KeyHolder keyHolder = new GeneratedKeyHolder();

      namedJdbc.update(
            "INSERT INTO MealEventLog (cardCode, `name`, price, mealType, grade, pupilType, date) " +
                  "VALUES (:cardCode, :name, :price, :mealType, :grade, :pupilType, :date)",
            new MapSqlParameterSource(args),
            keyHolder
      );

      return load(keyHolder.getKey().longValue())
            .orElseThrow(() -> new RuntimeException("could not saved mealEventLog"));
   }

   @Override
   public Optional<MealEventLog> load(Long id) {
      MealEventLog result = jdbc.queryForObject("SELECT * FROM MealEventLog WHERE id = ?", MAPPER, id);
      return Optional.of(result);
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM MealEventLog WHERE id = ?", id);
   }

   @Override
   public Class<MealEventLog> getEntityClass() {
      return MealEventLog.class;
   }
}