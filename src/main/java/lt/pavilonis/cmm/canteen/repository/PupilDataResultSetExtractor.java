package lt.pavilonis.cmm.canteen.repository;

import lt.pavilonis.cmm.canteen.domain.Eating;
import lt.pavilonis.cmm.canteen.domain.EatingData;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PupilDataResultSetExtractor implements ResultSetExtractor<Map<String, EatingData>> {

   private final int ANY = 100500;
   private final EatingMapper EATING_MAPPER = new EatingMapper();

   @Override
   public Map<String, EatingData> extractData(ResultSet rs) throws SQLException, DataAccessException {
      Map<String, EatingData> result = new HashMap<>();
      while (rs.next()) {
         String cardCode = rs.getString("p.cardCode");
         EatingData pupilData = result.get(cardCode);
         if (pupilData == null) {
            pupilData = new EatingData(
                  cardCode,
                  PupilType.valueOf(rs.getString("p.type")),
                  rs.getString("p.comment"),
                  new HashSet<>()
            );
            result.put(cardCode, pupilData);
         }

         Long eatingId = (Long) rs.getObject("e.id");
         if (eatingId != null) {
            Eating eating = EATING_MAPPER.mapRow(rs, ANY);
            pupilData.getEatings().add(eating);
         }
      }
      return result;
   }
}
