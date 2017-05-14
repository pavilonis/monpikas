package lt.pavilonis.cmm.warehouse.menurequirement;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdPeriodFilter;
import lt.pavilonis.cmm.warehouse.meal.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

   @Transactional
   @Override
   public MenuRequirement saveOrUpdate(MenuRequirement entity) {

      return entity.getId() == null
            ? create(entity)
            : update(entity);
   }

   private MenuRequirement update(MenuRequirement menuRequirement) {
      long menuReqId = menuRequirement.getId();
      ImmutableMap<String, Object> args =
            ImmutableMap.of("id", menuReqId, "date", menuRequirement.getDate());

      jdbc.update("UPDATE MenuRequirement SET date = :date WHERE id = :id", args);
      jdbc.update("DELETE FROM Meal WHERE menuRequirement_id = :id", args);

      saveMenuRequirementMeals(menuRequirement.getMeals(), menuReqId);

      return find(menuReqId)
            .orElseThrow(IllegalStateException::new);
   }

   private void saveMenuRequirementMeals(List<Meal> meals, long id) {
      meals.forEach(meal -> {

         long insertedMealId = insertNewMeal(id, meal.getType().getId());

         meal.getTechnologicalCards()
               .forEach(card -> addTechnologicalCardToMeal(insertedMealId, card.getId()));
      });
   }

   private void addTechnologicalCardToMeal(long insertedMealId, long technologicalCardId) {
      jdbc.update(
            "INSERT INTO MealTechnologicalCard (meal_id, technologicalCard_id) VALUES (:mealId, :cardId)",
            ImmutableMap.of("mealId", insertedMealId, "cardId", technologicalCardId)
      );
   }

   private long insertNewMeal(long menuRequirementId, long mealTypeId) {
      KeyHolder keyHolder = new GeneratedKeyHolder();

      jdbc.update(
            "INSERT INTO Meal (menuRequirement_id, mealType_id) VALUES (:menuRequirementId, :mealTypeId)",
            new MapSqlParameterSource(
                  ImmutableMap.of("menuRequirementId", menuRequirementId, "mealTypeId", mealTypeId)
            ),
            keyHolder
      );
      return keyHolder.getKey().longValue();
   }

   private MenuRequirement create(MenuRequirement entity) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbc.update(
            "INSERT INTO MenuRequirement (date) VALUE (:date)",
            new MapSqlParameterSource(Collections.singletonMap("date", entity.getDate())),
            keyHolder
      );

      saveMenuRequirementMeals(entity.getMeals(), entity.getId());

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
                  "SELECT mr.id, mr.date ," +
                  "  m.id, " +
                  "  mt.id, mt.name, " +
                  "  tc.id, tc.name, " +
                  "  tcg.id, tcg.name " +
                  "FROM MenuRequirement mr " +
                  "  JOIN Meal m ON m.menuRequirement_id = mr.id " +
                  "  JOIN MealType mt ON mt.id = m.mealType_id " +
                  "  JOIN MealTechnologicalCard mtc ON mtc.meal_id = m.id " +
                  "  JOIN TechnologicalCard tc ON tc.id = mtc.technologicalCard_id " +
                  "  JOIN TechnologicalCardGroup tcg ON tcg.id = tc.technologicalCardGroup_id " +
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
