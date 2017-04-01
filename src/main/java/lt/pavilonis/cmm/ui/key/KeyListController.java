package lt.pavilonis.cmm.ui.key;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.domain.KeyRepresentation;
import lt.pavilonis.cmm.repository.KeyRestRepository;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
public class KeyListController extends AbstractListController<KeyRepresentation, String, KeyListFilter> {

   @Autowired
   private KeyListFilterPanel keyListFilterPanel;

   @Autowired
   private KeyListGrid keyTable;

   @Autowired
   private KeyRestRepository repository;

   @Override
   protected ListGrid<KeyRepresentation> createGrid() {
      return keyTable;
   }

   @Override
   protected FilterPanel<KeyListFilter> createFilterPanel() {
      return keyListFilterPanel;
   }

   @Override
   protected EntityRepository<KeyRepresentation, String, KeyListFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<KeyRepresentation> getEntityClass() {
      return KeyRepresentation.class;
   }

   @Override
   protected Component getControlPanel() {
      return null;
   }
}
