package lt.pavilonis.cmm.warehouse.productgroup;

import com.vaadin.icons.VaadinIcons;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import lt.pavilonis.cmm.common.ui.filter.NameFilterPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProductGroupListController extends AbstractListController<ProductGroup, Long, IdTextFilter> {

   @Autowired
   private ProductGroupRepository repository;

   @Override
   protected ListGrid<ProductGroup> createGrid() {
      return new ListGrid<>(ProductGroup.class);
   }

   @Override
   protected AbstractFormController<ProductGroup, Long> getFormController() {
      return new AbstractFormController<ProductGroup, Long>(ProductGroup.class) {
         @Override
         protected EntityRepository<ProductGroup, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<ProductGroup> createFieldLayout(ProductGroup model) {
            return new ProductGroupForm();
         }
      };
   }

   @Override
   protected FilterPanel<IdTextFilter> createFilterPanel() {
      return new NameFilterPanel();
   }

   @Override
   protected EntityRepository<ProductGroup, Long, IdTextFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<ProductGroup> getEntityClass() {
      return ProductGroup.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.STOCK;
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
      return "product-group";
   }
}
