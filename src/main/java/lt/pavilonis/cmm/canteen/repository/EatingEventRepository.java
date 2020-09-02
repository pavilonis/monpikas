package lt.pavilonis.cmm.canteen.repository;

import lt.pavilonis.cmm.canteen.domain.EatingEvent;
import lt.pavilonis.cmm.canteen.domain.EatingType;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.ui.event.EatingEventFilter;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.util.QueryUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static lt.pavilonis.cmm.common.util.TimeUtils.duration;

@Repository
public class EatingEventRepository implements EntityRepository<EatingEvent, Long, EatingEventFilter> {

   private static final Logger LOG = LoggerFactory.getLogger(EatingEventRepository.class.getSimpleName());
   private static final RowMapper<EatingEvent> MAPPER = (rs, i) -> new EatingEvent(
         rs.getLong("id"),
         rs.getString("cardCode"),
         rs.getString("name"),
         rs.getString("grade"),
         rs.getTimestamp("date"),
         rs.getBigDecimal("price"),
         EatingType.valueOf(rs.getString("eatingType")),
         PupilType.valueOf(rs.getString("pupilType"))
   );
   private final JdbcTemplate jdbc;
   private final NamedParameterJdbcTemplate jdbcNamed;

   public EatingEventRepository(JdbcTemplate jdbc, NamedParameterJdbcTemplate jdbcNamed) {
      this.jdbc = jdbc;
      this.jdbcNamed = jdbcNamed;
   }

   public int numOfEatingEvents(String cardCode, Date periodStart,
                                Date periodEnd, EatingType eatingType) {
      return jdbc.queryForObject("" +
                  "SELECT count(*) " +
                  "FROM EatingEvent " +
                  "WHERE `date` BETWEEN ? AND ? " +
                  "  AND cardCode = ? " +
                  "  AND eatingType = ?",
            Integer.class,
            periodStart, periodEnd, cardCode, eatingType.name()
      );
   }

   @Override
   public List<EatingEvent> load() {
      throw new NotImplementedException("Not needed yet");
   }

   @Override
   public List<EatingEvent> load(EatingEventFilter filter) {
      LocalDateTime opStart = LocalDateTime.now();

      List<EatingEvent> result = jdbcNamed.query("" +
                  "SELECT * " +
                  "FROM EatingEvent " +
                  "WHERE " +
                  "  (:periodStart IS NULL OR :periodStart <= date) " +
                  "  AND (:periodEnd IS NULL OR :periodEnd >= date) " +
                  "  AND (:text IS NULL OR name LIKE :text)" +
                  "  AND (:type IS NULL OR pupilType = :type)",
            createSearchArgs(filter),
            MAPPER
      );
      LOG.info("Loaded events [number={}, t={}]", result.size(), duration(opStart));
      return result;
   }

   private Map<String, Object> createSearchArgs(EatingEventFilter filter) {
      Map<String, Object> params = new HashMap<>();

      LocalDate start = filter.getPeriodStart();
      LocalDate end = filter.getPeriodEnd();

      params.put("periodStart", start == null ? null : start.atTime(LocalTime.MIN));
      params.put("periodEnd", end == null ? null : end.atTime(LocalTime.MAX));

      params.put("text", QueryUtils.likeArg(filter.getText()));
      params.put("type", filter.getType() == null ? null : filter.getType().name());
      return params;
   }

   @Override
   public EatingEvent saveOrUpdate(EatingEvent entity) {
      Map<String, Object> args = new HashMap<>();
      args.put("cardCode", entity.getCardCode());
      args.put("name", entity.getName());
      args.put("price", entity.getPrice());
      args.put("eatingType", entity.getEatingType().name());
      args.put("grade", entity.getGrade());
      args.put("pupilType", entity.getPupilType().name());
      args.put("date", entity.getDate());

      KeyHolder keyHolder = new GeneratedKeyHolder();

      jdbcNamed.update(
            "INSERT INTO EatingEvent (cardCode, `name`, price, eatingType, grade, pupilType, date) " +
                  "VALUES (:cardCode, :name, :price, :eatingType, :grade, :pupilType, :date)",
            new MapSqlParameterSource(args),
            keyHolder
      );

      return find(keyHolder.getKey().longValue())
            .orElseThrow(() -> new RuntimeException("could not save " + EatingEvent.class.getSimpleName()));
   }

   @Override
   public Optional<EatingEvent> find(Long id) {
      EatingEvent result = jdbc.queryForObject("SELECT * FROM EatingEvent WHERE id = ?", MAPPER, id);
      return Optional.of(result);
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM EatingEvent WHERE id = ?", id);
   }

   @Override
   public Class<EatingEvent> entityClass() {
      return EatingEvent.class;
   }
}