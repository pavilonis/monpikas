package lt.pavilonis.cmm.warehouse.techcardset;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import lt.pavilonis.cmm.warehouse.techcard.TechCard;
import lt.pavilonis.cmm.common.util.QueryUtils;
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
public class TechCardSetRepository implements EntityRepository<TechCardSet, Long, IdTextFilter> {

   @Autowired
   private NamedParameterJdbcTemplate jdbcNamed;

   @Transactional
   @Override
   public TechCardSet saveOrUpdate(TechCardSet entity) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", entity.getId());
      args.put("name", entity.getName());
      args.put("typeId", entity.getType().getId());
      args.put("dateCreated", new Date());

      return entity.getId() == null
            ? save(entity, args)
            : update(entity, args);
   }

   private TechCardSet update(TechCardSet entity, Map<String, Object> args) {
      jdbcNamed.update(
            "UPDATE TechCardSet SET name = :name, techCardSetType_id = :typeId WHERE id = :id",
            args
      );
      jdbcNamed.update("DELETE FROM TechCardSetTechCard WHERE techCardSet_id = :id", args);
      saveTechCards(entity.getTechCards(), entity.getId());

      return find(entity.getId())
            .orElseThrow(() -> new RuntimeException("Could not load updated object"));
   }

   private TechCardSet save(TechCardSet entity, Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcNamed.update("" +
                  "INSERT INTO TechCardSet(name, techCardSetType_id, dateCreated) " +
                  "VALUES (:name, :typeId, :dateCreated)",
            new MapSqlParameterSource(args),
            keyHolder
      );

      long cardSetId = keyHolder.getKey().longValue();

      saveTechCards(entity.getTechCards(), cardSetId);

      return find(cardSetId)
            .orElseThrow(() -> new RuntimeException("Could not load saved object"));
   }

   private void saveTechCards(Collection<TechCard> techCards, long cardSetId) {

      Date now = new Date();

      @SuppressWarnings("unchecked")
      Map<String, ?>[] batchArgs = techCards.stream()
            .map(Identified::getId)
            .map(cardId -> ImmutableMap.of("cardSetId", cardSetId, "cardId", cardId, "dateCreated", now))
            .toArray(Map[]::new);

      jdbcNamed.batchUpdate("" +
                  "INSERT INTO TechCardSetTechCard (techCardSet_id, techCard_id, dateCreated) " +
                  "VALUES (:cardSetId, :cardId, :dateCreated)",
            batchArgs
      );
   }

   @Override
   public List<TechCardSet> load() {
      return load(IdTextFilter.empty());
   }

   @Override
   public List<TechCardSet> load(IdTextFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("text", QueryUtils.likeArg(filter.getText()));
      args.put("id", filter.getId());
      return jdbcNamed.query("" +
                  "SELECT " +
                  "  tcs.id, tcs.name," +
                  "  tcst.id, tcst.name," +
                  "  tc.id, tc.name, " +
                  "  tcg.id, tcg.name, " +
                  "  tcp.outputWeight, " +
                  "  pg.id, pg.name, pg.kcal100 " +
                  "FROM TechCardSet tcs " +
                  "  JOIN TechCardSetType tcst ON tcst.id = tcs.techCardSetType_id " +
                  "  JOIN TechCardSetTechCard tcstc ON tcs.id = tcstc.techCardSet_id " +
                  "  JOIN TechCard tc ON tc.id = tcstc.techCard_id " +
                  "  JOIN TechCardGroup tcg ON tcg.id = tc.techCardGroup_id " +
                  "  LEFT JOIN TechCardProduct tcp ON tcp.techCard_id = tc.id " +
                  "  LEFT JOIN ProductGroup pg ON pg.id = tcp.productGroup_id " +
                  "WHERE (:id IS NULL OR :id = tcs.id) " +
                  "  AND (:text IS NULL OR tcs.name LIKE :text)",
            args,
            new TechCardSetResultSetExtractor()
      );
   }

   @Override
   public Optional<TechCardSet> find(Long id) {
      List<TechCardSet> result = load(new IdTextFilter(id));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbcNamed.update(
            "DELETE FROM TechCardSet WHERE id = :id",
            Collections.singletonMap("id", id)
      );
   }

   @Override
   public Class<TechCardSet> entityClass() {
      return TechCardSet.class;
   }
}
