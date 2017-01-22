package lt.pavilonis.cmm.ui.key;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.domain.KeyRepresentation;
import lt.pavilonis.cmm.repository.KeyRestRepository;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
public class KeyListController extends AbstractListController<KeyRepresentation, String, KeyFilter> {

   @Autowired
   private KeyListFilterPanel keyListFilterPanel;

   @Autowired
   private KeyTable keyTable;

   @Autowired
   private KeyRestRepository repository;

   @Override
   protected ListTable<KeyRepresentation> getTable() {
      return keyTable;
   }

   @Override
   protected FilterPanel<KeyFilter> getFilterPanel() {
      return keyListFilterPanel;
   }

   @Override
   protected EntityRepository<KeyRepresentation, String, KeyFilter> getEntityRepository() {
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
