package lt.pavilonis.cmm.key.ui;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.key.domain.Key;
import lt.pavilonis.cmm.key.repository.KeyRestRepository;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
public class KeyListController extends AbstractListController<Key, String, KeyListFilter> {

   @Autowired
   private KeyListFilterPanel keyListFilterPanel;

   @Autowired
   private KeyListGrid keyTable;

   @Autowired
   private KeyRestRepository repository;

   @Override
   protected ListGrid<Key> createGrid() {
      return keyTable;
   }

   @Override
   protected FilterPanel<KeyListFilter> createFilterPanel() {
      return keyListFilterPanel;
   }

   @Override
   protected EntityRepository<Key, String, KeyListFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<Key> getEntityClass() {
      return Key.class;
   }

   @Override
   protected Component getControlPanel() {
      return null;
   }
}
