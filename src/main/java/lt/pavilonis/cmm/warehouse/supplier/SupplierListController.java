package lt.pavilonis.cmm.warehouse.supplier;

import com.vaadin.icons.VaadinIcons;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.ui.filter.IdNameFilter;
import lt.pavilonis.cmm.common.ui.filter.NameFilterPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SupplierListController extends AbstractListController<Supplier, Long, IdNameFilter> {

   @Autowired
   private SupplierRepository repository;

   @Override
   protected ListGrid<Supplier> createGrid() {
      return new ListGrid<>(Supplier.class);
   }

   @Override
   protected FilterPanel<IdNameFilter> createFilterPanel() {
      return new NameFilterPanel();
   }

   @Override
   protected EntityRepository<Supplier, Long, IdNameFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<Supplier> getEntityClass() {
      return Supplier.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.TRUCK;
   }

   @Override
   public String getViewRole() {
      return "WAREHOUSE";
   }

   @Override
   public String getViewGroupName() {
      return "warehouse";
   }

   @Override
   public String getViewName() {
      return "supplier";
   }
}
