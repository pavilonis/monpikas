package lt.pavilonis.monpikas.scanner;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.monpikas.common.AbstractFormController;
import lt.pavilonis.monpikas.common.AbstractListController;
import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.common.FieldLayout;
import lt.pavilonis.monpikas.common.ListGrid;
import lt.pavilonis.monpikas.common.ui.filter.FilterPanel;
import lt.pavilonis.monpikas.common.ui.filter.IdTextFilter;
import lt.pavilonis.monpikas.common.ui.filter.NameFilterPanel;

@UIScope
@SpringComponent
public class ScannerListController extends AbstractListController<Scanner, Long, IdTextFilter> {

   private final ScannerRepository repository;

   public ScannerListController(ScannerRepository repository) {
      this.repository = repository;
   }

   @Override
   protected ListGrid<Scanner> createGrid() {
      return new ScannerListGrid();
   }

   @Override
   protected FilterPanel<IdTextFilter> createFilterPanel() {
      return new NameFilterPanel();
   }

   @Override
   protected EntityRepository<Scanner, Long, IdTextFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<Scanner> getEntityClass() {
      return Scanner.class;
   }

   @Override
   protected AbstractFormController<Scanner, Long> getFormController() {
      return new AbstractFormController<>(Scanner.class) {
         @Override
         protected EntityRepository<Scanner, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<Scanner> createFieldLayout(Scanner model) {
            return new ScannerFormView();
         }
      };
   }

   @Override
   public String getViewName() {
      return "scanners";
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.BUILDING;
   }

   @Override
   public String getViewRole() {
      return "SCANNER";
   }

   @Override
   public String getViewGroupName() {
      return "school";
   }
}
