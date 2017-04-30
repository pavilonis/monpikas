package lt.pavilonis.cmm.key.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.key.domain.Key;
import lt.pavilonis.cmm.key.repository.KeyRestRepository;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
public class KeyListController extends AbstractListController<Key, Void, KeyListFilter> {

   @Autowired
   private KeyRestRepository repository;

   @Override
   protected ListGrid<Key> createGrid() {
      return new KeyListGrid();
   }

   @Override
   protected FilterPanel<KeyListFilter> createFilterPanel() {
      return new KeyListFilterPanel();
   }

   @Override
   protected EntityRepository<Key, Void, KeyListFilter> getEntityRepository() {
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

   @Override
   public String getViewName() {
      return "keys";
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.KEY;
   }

   @Override
   public String getViewRole() {
      return "KEYS";
   }

   @Override
   public String getViewGroupName() {
      return "school";
   }
}
