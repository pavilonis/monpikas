package lt.pavilonis.cmm.warehouse.menurequirement;

import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.warehouse.techcardset.TechCardSet;
import lt.pavilonis.cmm.warehouse.techcardset.TechCardSetRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TechCardSetNumberRepositoryAdapter implements EntityRepository<TechCardSetNumber, Void, Void> {

   @Autowired
   private TechCardSetRepository techCardSetRepository;

   @Override
   public List<TechCardSetNumber> load() {
      List<TechCardSet> allTechCardSets = techCardSetRepository.load();
      return allTechCardSets.stream()
            .map(tsc -> new TechCardSetNumber(tsc, 1))
            .collect(Collectors.toList());
   }

   @Override
   public Class<TechCardSetNumber> entityClass() {
      return TechCardSetNumber.class;
   }

   @Override
   public TechCardSetNumber saveOrUpdate(TechCardSetNumber entity) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public List<TechCardSetNumber> load(Void aVoid) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public Optional<TechCardSetNumber> find(Void aVoid) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public void delete(Void aVoid) {
      throw new NotImplementedException("Not needed");
   }
}
