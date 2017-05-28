package lt.pavilonis.cmm.canteen.repository;

import lt.pavilonis.cmm.canteen.domain.EatingData;
import lt.pavilonis.cmm.canteen.domain.EatingType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Repository
public class PupilDataRepository {

   private static final PupilDataResultSetExtractor PUPIL_DATA_EXTRACTOR = new PupilDataResultSetExtractor();

   @Autowired
   private JdbcTemplate jdbc;

   @Autowired
   private NamedParameterJdbcTemplate jdbcNamed;

   public Collection<EatingData> loadAll(boolean withEatingsAssignedOnly, EatingType eatingType) {
      return query(null, eatingType, withEatingsAssignedOnly).values();
   }

   public Optional<EatingData> load(String cardCode) {
      if (StringUtils.isBlank(cardCode)) {
         throw new IllegalArgumentException("Got empty argument!");
      }
      Map<String, EatingData> result = query(cardCode, null, false);
      return result.isEmpty()
            ? Optional.<EatingData>empty()
            : Optional.of(result.values().iterator().next());
   }

   private Map<String, EatingData> query(String cardCode, EatingType eatingType, boolean withEatingsAssigned) {
      Map<String, Object> args = new HashMap<>();
      args.put("cardCode", cardCode);
      args.put("withEatingsAssigned", withEatingsAssigned);
      args.put("eatingType", eatingType == null ? null : eatingType.name());
      return jdbcNamed.query("" +
                  "SELECT " +
                  "  p.cardCode, p.comment, p.type, " +
                  "  e.id, e.name, e.type, e.price, e.startTime, e.endTime " +
                  "FROM Pupil p" +
                  "  LEFT JOIN PupilEating pm ON pm.pupil_cardCode = p.cardCode " +
                  "  LEFT JOIN Eating e ON e.id = pm.eating_id " +
                  "WHERE (:cardCode IS NULL OR :cardCode = p.cardCode)" +
                  "  AND (:eatingType IS NULL OR e.type = :eatingType)" +
                  "  AND (:withEatingsAssigned IS FALSE OR e.id IS NOT NULL)",
            args,
            PUPIL_DATA_EXTRACTOR
      );
   }

   @Transactional
   public EatingData saveOrUpdate(EatingData pupil) {
      Map<String, Object> args = new HashMap<>();
      args.put("cardCode", pupil.getCardCode());
      args.put("type", pupil.getType().name());
      args.put("comment", pupil.getComment());

      jdbcNamed.update("" +
                  "INSERT INTO Pupil (cardCode, type, comment) " +
                  "VALUES (:cardCode, :type, :comment) " +
                  "ON DUPLICATE KEY UPDATE cardCode = :cardCode, type = :type, comment = :comment",
            args
      );

      jdbc.update("DELETE FROM PupilEating WHERE pupil_cardCode = ?", pupil.getCardCode());

      List<Object[]> batchArgs = pupil.getEatings().stream()
            .map(eating -> new Object[]{pupil.getCardCode(), eating.getId()})
            .collect(toList());

      if (!batchArgs.isEmpty()) {
         jdbc.batchUpdate("INSERT INTO PupilEating (pupil_cardCode, eating_id) VALUES (?, ?)", batchArgs);
      }

      return load(pupil.getCardCode()).get();
   }
}