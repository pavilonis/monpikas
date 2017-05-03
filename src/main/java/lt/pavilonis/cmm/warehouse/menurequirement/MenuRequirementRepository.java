package lt.pavilonis.cmm.warehouse.menurequirement;

import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdPeriodFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class MenuRequirementRepository implements EntityRepository<MenuRequirement, Long, IdPeriodFilter> {

   private static final ResultSetExtractor<List<MenuRequirement>> RESULT_EXTRACTOR
         = new MenuRequirementResultSetExtractor();

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   @Override
   public MenuRequirement saveOrUpdate(MenuRequirement entity) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, entity.getId());
      args.put("date", entity.getDate());

      return entity.getId() == null
            ? create(args)
            : update(args);
   }

   private MenuRequirement update(Map<String, ?> args) {
      jdbc.update("UPDATE MenuRequirement SET name = :name, date = :date WHERE id = :id", args);
      return find((Long) args.get(ID))
            .orElseThrow(IllegalStateException::new);
   }

   private MenuRequirement create(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbc.update(
            "INSERT INTO MenuRequirement (name, date) VALUES (:name, :date)",
            new MapSqlParameterSource(args),
            keyHolder
      );
      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }

   @Override
   public List<MenuRequirement> load(IdPeriodFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, filter.getId());
      args.put("periodStart", filter.getPeriodStart());
      args.put("periodEnd", filter.getPeriodEnd());
      return jdbc.query("" +
                  "SELECT mr.id, mr.date " +
                  "FROM MenuRequirement mr " +
                  "  JOIN Meal m ON m.menuRequirement_id = mr.id" +
                  "  JOIN MealItem mi ON mi.meal_id = m.id" +
                  "  JOIN TechnologicalCard tc ON tc.id = mi.technologicalCard_id " +
                  "WHERE (:id IS NULL OR mr.id = :id) " +
                  "  AND (:periodStart IS NULL OR mr.date >= :periodStart)" +
                  "  AND (:periodEnd IS NULL OR mr.date <= :periodEnd) " +
                  "ORDER BY mr.date",
            args,
            RESULT_EXTRACTOR
      );
   }

   @Override
   public Optional<MenuRequirement> find(Long id) {
      List<MenuRequirement> result = load(new IdPeriodFilter(id));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM MenuRequirement WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<MenuRequirement> entityClass() {
      return MenuRequirement.class;
   }

//   @Override
//   public List<MealRecord> forMenu(long menuId) {
//      return dsl().select(MEAL.fields())
//            .from(MEAL)
//            .join(MENU).on(MENU.ID.eq(MEAL.MENUID))
//            .where(MENU.ID.eq(menuId))
//            .fetchInto(MEAL);
//   }
//
//   @Override
//   public int mealsNumForMenu(long menuId) {
//      return dsl().selectCount()
//            .from(MEAL)
//            .join(MENU).on(MENU.ID.eq(MEAL.MENUID))
//            .where(MENU.ID.eq(menuId))
//            .fetchOne(0, int.class);
//   }
//
//   @Override
//   public int kcal(long mealId) {
//      return dsl().select(coalesce(sum(PRODUCTGROUP.KCAL100.mul(DISHITEM.OUTPUTWEIGHT).div(100)), 0).coerce(int.class))
//            .from(DISHITEM)
//            .join(DISH).on(DISHITEM.DISHID.eq(DISH.ID))
//            .join(PRODUCTGROUP).on(DISHITEM.PRODUCTGROUPID.eq(PRODUCTGROUP.ID)).and(DISHITEM.DISHID.eq(DISH.ID))
//            .join(MEALITEM).on(MEALITEM.DISHID.eq(DISH.ID))
//            .join(MEAL).on(MEAL.ID.eq(MEALITEM.MEALID))
//            .where(MEAL.ID.eq(mealId))
//            .fetchOne()
//            .value1();
//   }
}
