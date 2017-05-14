package lt.pavilonis.cmm.warehouse.techcard;

import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import lt.pavilonis.cmm.common.ui.filter.NameFilterPanel;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroupRepository;
import lt.pavilonis.cmm.warehouse.techcardgroup.TechCardGroup;
import lt.pavilonis.cmm.warehouse.techcardgroup.TechCardGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class TechCardListController extends AbstractListController<TechCard, Long, IdTextFilter> {

   @Autowired
   private TechCardRepository repository;

   @Autowired
   private TechCardGroupRepository techCardGroupRepository;

   @Autowired
   private ProductGroupRepository productGroupRepository;

   @Override
   protected ListGrid<TechCard> createGrid() {
      return new ListGrid<TechCard>(TechCard.class) {
         @Override
         protected Map<String, ValueProvider<TechCard, ?>> getCustomColumns() {
            return Collections.singletonMap(
                  "techCardGroup",
                  value -> value.getGroup().getName()
            );
         }

         @Override
         protected List<String> columnOrder() {
            return Arrays.asList("name", "techCardGroup");
         }
      };
   }

   @Override
   protected AbstractFormController<TechCard, Long> getFormController() {
      return new AbstractFormController<TechCard, Long>(TechCard.class) {
         @Override
         protected EntityRepository<TechCard, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<TechCard> createFieldLayout() {
            List<TechCardGroup> cardGroups = techCardGroupRepository.load(IdTextFilter.empty());
            List<ProductGroup> productGroups = productGroupRepository.load(IdTextFilter.empty());
            return new TechCardForm(cardGroups, productGroups);
         }
      };
   }

   @Override
   protected FilterPanel<IdTextFilter> createFilterPanel() {
      return new NameFilterPanel();
   }

   @Override
   protected EntityRepository<TechCard, Long, IdTextFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<TechCard> getEntityClass() {
      return TechCard.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.CROSS_CUTLERY;
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
      return "tech-card";
   }
}
