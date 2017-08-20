package lt.pavilonis.cmm.warehouse.product;

import com.vaadin.icons.VaadinIcons;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

@Controller
public class ProductListController extends AbstractListController<Product, Long, ProductFilter> {

   @Autowired
   private ProductRepository repository;

   @Autowired
   private ProductGroupRepository productGroupRepo;

   @Override
   protected ListGrid<Product> createGrid() {
      return new ListGrid<Product>(Product.class) {
         @Override
         protected List<String> columnOrder() {
            return Arrays.asList("productGroup", "name", "unitWeight", "measureUnit");
         }
      };
   }

   @Override
   protected AbstractFormController<Product, Long> getFormController() {
      return new AbstractFormController<Product, Long>(Product.class) {
         @Override
         protected EntityRepository<Product, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<Product> createFieldLayout(Product model) {
            return new ProductForm(productGroupRepo.load(new IdTextFilter()));
         }
      };
   }

   @Override
   protected FilterPanel<ProductFilter> createFilterPanel() {
      return new ProductFilterPanel(productGroupRepo.load());
   }

   @Override
   protected EntityRepository<Product, Long, ProductFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<Product> getEntityClass() {
      return Product.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.CUBE;
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
      return "product";
   }
}
