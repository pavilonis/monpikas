package lt.pavilonis.cmm.warehouse.techcardset;

import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import lt.pavilonis.util.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TechCardSetRepository implements EntityRepository<TechCardSet, Long, IdTextFilter> {

   @Autowired
   private NamedParameterJdbcTemplate jdbcNamed;

   @Override
   public TechCardSet saveOrUpdate(TechCardSet entity) {

      return null;
   }

   @Override
   public List<TechCardSet> load() {
      return load(IdTextFilter.empty());
   }

   @Override
   public List<TechCardSet> load(IdTextFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("text", QueryUtils.likeArg(filter.getText()));
      return jdbcNamed.query("" +
                  "SELECT " +
                  "  tcs.id, tcs.name," +
                  "  tcst.id, tcst.name," +
                  "  tc.id, tc.name, " +
                  "  tcg.id, tcg.name " +
                  "FROM TechCardSet tcs " +
                  "  JOIN TechCardSetType tcst ON tcst.id = tcs.techCardSetType_id " +
                  "  JOIN TechCardSetTechCard tcstc ON tcs.id = tcstc.techCardSet_id " +
                  "  JOIN TechCard tc ON tc.id = tcstc.techCard_id " +
                  "  JOIN TechCardGroup tcg ON tcg.id = tc.techCardGroup_id " +
                  "WHERE :text IS NULL OR tcs.name LIKE :text",
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
