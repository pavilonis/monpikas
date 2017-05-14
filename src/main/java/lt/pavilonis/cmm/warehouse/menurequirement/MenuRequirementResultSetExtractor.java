package lt.pavilonis.cmm.warehouse.menurequirement;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.meal.Meal;
import lt.pavilonis.cmm.warehouse.meal.MealMapper;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroupMapper;
import lt.pavilonis.cmm.warehouse.techcard.TechCard;
import lt.pavilonis.cmm.warehouse.techcard.TechCardRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MenuRequirementResultSetExtractor implements ResultSetExtractor<List<MenuRequirement>> {

   private static final MealMapper MEAL_MAPPER = new MealMapper();
   private static final TechCardRowMapper TECH_CARD_MAPPER = new TechCardRowMapper();
   private static final ProductGroupMapper PRODUCT_GROUP_MAPPER = new ProductGroupMapper();

   @Override
   public List<MenuRequirement> extractData(ResultSet rs) throws SQLException, DataAccessException {

      LinkedHashMap<Long, MenuRequirement> result = new LinkedHashMap<>();

      while (rs.next()) {
         long id = rs.getLong("mr.id");
         MenuRequirement menu = result.get(id);
         if (menu == null) {
            menu = new MenuRequirement(id, rs.getDate("mr.date").toLocalDate(), new ArrayList<>());
            result.put(id, menu);
         }

         List<Meal> meals = menu.getMeals();
         Meal meal = findById(meals, rs.getLong("m.id")).orElseGet(() -> {
            Meal newMeal = MEAL_MAPPER.mapRow(rs);
            meals.add(newMeal);
            return newMeal;
         });

         List<TechCard> cards = meal.getTechCards();

         TechCard techCard = findById(cards, rs.getLong("tc.id"))
               .orElseGet(() -> mapTechCard(rs, cards));

         addOutputWeight(rs, techCard.getProductGroupOutputWeight());
      }
      return new ArrayList<>(result.values());
   }

   protected TechCard mapTechCard(ResultSet rs, List<TechCard> cards) {
      TechCard techCard = TECH_CARD_MAPPER.mapRow(rs);
      cards.add(techCard);
      return techCard;
   }

   private void addOutputWeight(ResultSet rs, Map<ProductGroup, BigDecimal> weightMap) throws SQLException {
      BigDecimal outputWeight = rs.getBigDecimal("tcp.outputWeight");
      if (outputWeight != null) {
         ProductGroup productGroup = PRODUCT_GROUP_MAPPER.mapRow(rs);
         weightMap.putIfAbsent(productGroup, outputWeight);
      }
   }

   protected <T extends Identified<Long>> Optional<T> findById(List<T> entities, long checkId) {
      return entities.stream()
            .filter(e -> e.getId().equals(checkId))
            .findFirst();
   }
}
