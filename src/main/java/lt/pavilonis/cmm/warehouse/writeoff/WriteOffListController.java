package lt.pavilonis.cmm.warehouse.writeoff;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.warehouse.product.ProductRepository;
import lt.pavilonis.cmm.warehouse.supplier.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class WriteOffListController extends AbstractListController<WriteOff, Long, WriteOffFilter> {

   @Autowired
   private WriteOffRepository repository;

   @Autowired
   private ProductRepository productRepository;

   @Override
   protected ListGrid<WriteOff> createGrid() {
      return new ListGrid<WriteOff>(WriteOff.class) {
         @Override
         protected List<String> columnOrder() {
            return Arrays.asList("periodStart", "periodEnd", "confirmed");
         }
      };
   }

   @Override
   protected FilterPanel<WriteOffFilter> createFilterPanel() {
      return new WriteOffFilterPanel();
   }

   @Override
   protected AbstractFormController<WriteOff, Long> getFormController() {
      return new AbstractFormController<WriteOff, Long>(WriteOff.class) {
         @Override
         protected EntityRepository<WriteOff, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<WriteOff> createFieldLayout() {
            return new WriteOffFields();
         }

         @Override
         protected void customizeWindow(Window window) {
            window.setWidth(920, Sizeable.Unit.PIXELS);
//            window.setHeight(570, Sizeable.Unit.PIXELS);
         }
      };
   }

   @Override
   protected EntityRepository<WriteOff, Long, WriteOffFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<WriteOff> getEntityClass() {
      return WriteOff.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.FILE_REMOVE;
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
      return "writeOff";
   }
}
