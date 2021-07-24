package lt.pavilonis.monpikas.key.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.monpikas.key.Key;
import lt.pavilonis.monpikas.key.KeyListRepository;
import lt.pavilonis.monpikas.scanlog.ScannerRepository;
import lt.pavilonis.monpikas.common.AbstractListController;
import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.common.ListGrid;
import lt.pavilonis.monpikas.common.ui.filter.FilterPanel;

import java.util.Optional;

@UIScope
@SpringComponent
public class KeyListController extends AbstractListController<Key, Integer, KeyListFilter> {

   private final KeyListRepository repository;
   private final ScannerRepository scannerRepository;

   public KeyListController(KeyListRepository repository, ScannerRepository scannerRepository) {
      this.repository = repository;
      this.scannerRepository = scannerRepository;
   }

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
