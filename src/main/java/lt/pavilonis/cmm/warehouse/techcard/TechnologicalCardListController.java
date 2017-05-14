package lt.pavilonis.cmm.warehouse.techcard;

import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.warehouse.techcardgroup.TechnologicalCardGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class TechnologicalCardListController extends AbstractListController<TechnologicalCard, Long, TechnologicalCardFilter> {

   @Autowired
   private TechnologicalCardRepository repository;

   @Override
   protected ListGrid<TechnologicalCard> createGrid() {
      return new ListGrid<TechnologicalCard>(TechnologicalCard.class) {
         @Override
         protected Map<String, ValueProvider<TechnologicalCard, ?>> getCustomColumns() {
            return Collections.singletonMap("technologicalCardGroup",
                  value -> value.getTechnologicalCardGroup().getName());
         }

         @Override
         protected List<String> columnOrder() {
            return Arrays.asList("name", "technologicalCardGroup");
         }
      };
   }

   @Override
   protected AbstractFormController<TechnologicalCard, Long> getFormController() {
      return new AbstractFormController<TechnologicalCard, Long>(TechnologicalCard.class) {
         @Override
         protected EntityRepository<TechnologicalCard, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<TechnologicalCard> createFieldLayout() {
            return null;
//            return new TechnologicalCardForm(dishGroupRepo.load(new IdNameFilter()));
         }
      };
   }

   @Override
   protected FilterPanel<TechnologicalCardFilter> createFilterPanel() {
      return null;
   }

   @Override
   protected EntityRepository<TechnologicalCard, Long, TechnologicalCardFilter> getEntityRepository() {
      return null;
   }

   @Override
   protected Class<TechnologicalCard> getEntityClass() {
      return TechnologicalCard.class;
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
      return "technological-card";
   }
}
