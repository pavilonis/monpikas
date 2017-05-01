package lt.pavilonis.cmm.warehouse.product;

import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.ui.filter.IdNameFilter;
import lt.pavilonis.cmm.common.ui.filter.NameFilterPanel;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class ProductListController extends AbstractListController<Product, Long, IdNameFilter> {

   @Autowired
   private ProductRepository repository;

   @Autowired
   private ProductGroupRepository productGroupRepo;

   @Override
   protected ListGrid<Product> createGrid() {
      return new ListGrid<Product>(Product.class) {
         @Override
         protected Map<String, ValueProvider<Product, ?>> getCustomColumns() {
            return Collections.singletonMap("productGroup", value -> value.getProductGroup().getName());
         }

         @Override
         protected List<String> columnOrder() {
            return Arrays.asList("name", "unitWeight", "measureUnit", "productGroup");
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
         protected FieldLayout<Product> createFieldLayout() {
            return new ProductForm(productGroupRepo.load(new IdNameFilter()));
         }
      };
   }

   @Override
   protected FilterPanel<IdNameFilter> createFilterPanel() {
      return new NameFilterPanel();
   }

   @Override
   protected EntityRepository<Product, Long, IdNameFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<Product> getEntityClass() {
      return Product.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.PACKAGE;
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
