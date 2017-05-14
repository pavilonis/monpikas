package lt.pavilonis.cmm.warehouse.menurequirement;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.meal.Meal;
import lt.pavilonis.cmm.warehouse.meal.MealMapper;
import lt.pavilonis.cmm.warehouse.techcard.TechnologicalCard;
import lt.pavilonis.cmm.warehouse.techcard.TechnologicalCardMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public class MenuRequirementResultSetExtractor implements ResultSetExtractor<List<MenuRequirement>> {

   private static final MealMapper MEAL_MAPPER = new MealMapper();
   private static final TechnologicalCardMapper TECH_CARD_MAPPER = new TechnologicalCardMapper();

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

         List<TechnologicalCard> cards = meal.getTechnologicalCards();
         findById(cards, rs.getLong("tc.id")).orElseGet(() -> {
            TechnologicalCard newCard = TECH_CARD_MAPPER.mapRow(rs);
            cards.add(newCard);
            return newCard;
         });
      }
      return new ArrayList<>(result.values());
   }

   protected <T extends Identified<Long>> Optional<T> findById(List<T> entities, long checkId) {
      return entities.stream()
            .filter(e -> e.getId().equals(checkId))
            .findFirst();
   }
}
