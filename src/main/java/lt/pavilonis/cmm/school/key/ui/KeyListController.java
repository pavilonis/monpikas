package lt.pavilonis.cmm.school.key.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.api.rest.key.Key;
import lt.pavilonis.cmm.api.rest.scanner.ScannerRepository;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.school.key.KeyListRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@UIScope
@SpringComponent
public class KeyListController extends AbstractListController<Key, Integer, KeyListFilter> {

   @Autowired
   private KeyListRepository repository;

   @Autowired
   private ScannerRepository scannerRepository;

   @Override
   protected ListGrid<Key> createGrid() {
      return new KeyListGrid();
   }

   @Override
   protected FilterPanel<KeyListFilter> createFilterPanel() {
      return new KeyListFilterPanel(scannerRepository.loadAll());
   }

   @Override
   protected EntityRepository<Key, Integer, KeyListFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<Key> getEntityClass() {
      return Key.class;
   }

   @Override
   protected Optional<Component> getControlPanel(Component mainArea) {
      return Optional.empty();
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
