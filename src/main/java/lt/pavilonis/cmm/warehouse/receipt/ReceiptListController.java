package lt.pavilonis.cmm.warehouse.receipt;

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
import lt.pavilonis.cmm.common.ui.filter.IdPeriodTextFilter;
import lt.pavilonis.cmm.common.ui.filter.PeriodTextFilterPanel;
import lt.pavilonis.cmm.warehouse.product.ProductRepository;
import lt.pavilonis.cmm.warehouse.supplier.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class ReceiptListController extends AbstractListController<Receipt, Long, ReceiptFilter> {

   @Autowired
   private ReceiptRepository repository;

   @Autowired
   private SupplierRepository supplierRepository;

   @Autowired
   private ProductRepository productRepository;

   @Override
   protected ListGrid<Receipt> createGrid() {
      return new ListGrid<Receipt>(Receipt.class) {
         @Override
         protected List<String> columnOrder() {
            return Arrays.asList("number", "supplier", "dateTime", "sum");
         }
      };
   }

   @Override
   protected FilterPanel<ReceiptFilter> createFilterPanel() {
      return new ReceiptFilterPanel(supplierRepository.load());
   }

   @Override
   protected AbstractFormController<Receipt, Long> getFormController() {
      return new AbstractFormController<Receipt, Long>(Receipt.class) {
         @Override
         protected EntityRepository<Receipt, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<Receipt> createFieldLayout(Receipt model) {
            return new ReceiptFields(supplierRepository.load(), productRepository.load());
         }
      };
   }

   @Override
   protected EntityRepository<Receipt, Long, ReceiptFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<Receipt> getEntityClass() {
      return Receipt.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.FILE_ADD;
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
      return "receipt";
   }
}
