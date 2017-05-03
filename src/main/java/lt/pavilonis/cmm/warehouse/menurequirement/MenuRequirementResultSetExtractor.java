package lt.pavilonis.cmm.warehouse.menurequirement;

import lt.pavilonis.cmm.warehouse.meal.Meal;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuRequirementResultSetExtractor implements ResultSetExtractor<List<MenuRequirement>> {
   @Override
   public List<MenuRequirement> extractData(ResultSet rs) throws SQLException, DataAccessException {

      Map<Long, MenuRequirement> result = new HashMap<>();

      while (rs.next()) {
         long id = rs.getLong("mr.id");
         MenuRequirement menu = result.get(id);
         if (menu == null) {
            menu = new MenuRequirement(id, rs.getDate("mr.date").toLocalDate(), new ArrayList<>());
            result.put(id, menu);
         }

         List<Meal> meals = menu.getMeals();



         LocalDate date = rs.getDate("mr.date").toLocalDate();


      }

      return result;
   }
}
