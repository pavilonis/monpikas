package lt.pavilonis.cmm.warehouse.menurequirement;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdPeriodFilter;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class MenuRequirementRepository implements EntityRepository<MenuRequirement, Long, IdPeriodFilter> {

   @Autowired
   private NamedParameterJdbcTemplate jdbcNamed;

   @Transactional
   @Override
   public MenuRequirement saveOrUpdate(MenuRequirement entity) {

      return entity.getId() == null
            ? create(entity)
            : update(entity);
   }

   private MenuRequirement update(MenuRequirement entity) {

      ImmutableMap<String, Object> args =
            ImmutableMap.of("id", entity.getId(), "date", entity.getDate());

      jdbcNamed.update("UPDATE MenuRequirement SET date = :date WHERE id = :id", args);
      jdbcNamed.update("DELETE FROM MenuRequirementTechCardSet WHERE menuRequirement_id = :id", args);

      saveMenuRequirementTechCardSets(entity.getId(), entity.getTechCardSets());

      return find(entity.getId())
            .orElseThrow(IllegalStateException::new);
   }

   private void saveMenuRequirementTechCardSets(long menuRequirementId, Collection<TechCardSetNumber> techCardSetNumbers) {
      @SuppressWarnings("unchecked")
      Map<String, ?>[] batchArgs = techCardSetNumbers.stream()
            .map(entry ->
                        ImmutableMap.of(
                              "menuReqId", menuRequirementId,
                              "setId", entry.getTechCardSet().getId(),
                              "number", entry.getNumber(),
                              "dateCreated", new Date()
                        )
            ).toArray(Map[]::new);

      jdbcNamed.batchUpdate(
            "INSERT INTO MenuRequirementTechCardSet (menuRequirement_id, techCardSet_id, number, dateCreated) " +
                  "VALUES (:menuReqId, :setId, :number, :dateCreated)",
            batchArgs
      );
   }

   private MenuRequirement create(MenuRequirement entity) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcNamed.update(
            "INSERT INTO MenuRequirement (date, dateCreated) VALUE (:date, :dateCreated)",
            new MapSqlParameterSource(ImmutableMap.of(
                  "date", entity.getDate(),
                  "dateCreated", new Date()
            )),
            keyHolder
      );

      saveMenuRequirementTechCardSets(keyHolder.getKey().longValue(), entity.getTechCardSets());

      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }

   @Override
   public List<MenuRequirement> load() {
      throw new NotImplementedException("Not needed yet");
   }

   @Override
   public List<MenuRequirement> load(IdPeriodFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, filter.getId());
      args.put("periodStart", filter.getPeriodStart());
      args.put("periodEnd", filter.getPeriodEnd());
      return jdbcNamed.query("" +
                  "SELECT mr.id, mr.date ," +
                  "  mrtcs.number, " +
                  "  tcs.id, tcs.name, " +
                  "  tcst.id, tcst.name, " +
                  "  tc.id, tc.name, " +
                  "  tcg.id, tcg.name, " +
                  "  tcp.outputWeight, " +
                  "  pg.id, pg.name, pg.kcal100 " +
                  "FROM MenuRequirement mr " +
                  "  JOIN MenuRequirementTechCardSet mrtcs ON mrtcs.menuRequirement_id = mr.id " +
                  "  JOIN TechCardSet tcs ON mrtcs.techCardSet_id = tcs.id " +
                  "  JOIN TechCardSetType tcst ON tcst.id = tcs.techCardSetType_id " +
                  "  JOIN TechCardSetTechCard tcstc ON tcstc.techCardSet_id = tcs.id " +
                  "  JOIN TechCard tc ON tc.id = tcstc.techCard_id " +
                  "  JOIN TechCardGroup tcg ON tcg.id = tc.techCardGroup_id " +
                  "  JOIN TechCardProduct tcp ON tcp.techCard_id = tc.id " +
                  "  JOIN ProductGroup pg ON pg.id = tcp.productGroup_id " +
                  "WHERE (:id IS NULL OR mr.id = :id) " +
                  "  AND (:periodStart IS NULL OR mr.date >= :periodStart)" +
                  "  AND (:periodEnd IS NULL OR mr.date <= :periodEnd) " +
                  "ORDER BY mr.date",
            args,
            new MenuRequirementResultSetExtractor()
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
      jdbcNamed.update("DELETE FROM MenuRequirement WHERE id = :id", Collections.singletonMap("id", id));
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
