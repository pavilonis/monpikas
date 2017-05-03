package lt.pavilonis.cmm.warehouse.techcardgroup;

import com.vaadin.icons.VaadinIcons;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.ui.filter.IdNameFilter;
import lt.pavilonis.cmm.common.ui.filter.NameFilterPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TechnologicalCardGroupListController extends AbstractListController<TechnologicalCardGroup, Long, IdNameFilter> {

   @Autowired
   private TechnologicalCardRepository repository;

   @Override
   protected ListGrid<TechnologicalCardGroup> createGrid() {
      return new ListGrid<>(TechnologicalCardGroup.class);
   }

   @Override
   protected AbstractFormController<TechnologicalCardGroup, Long> getFormController() {
      return new AbstractFormController<TechnologicalCardGroup, Long>(TechnologicalCardGroup.class) {
         @Override
         protected EntityRepository<TechnologicalCardGroup, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<TechnologicalCardGroup> createFieldLayout() {
            return new TechnologicalCardGroupForm();
         }
      };
   }

   @Override
   protected FilterPanel<IdNameFilter> createFilterPanel() {
      return new NameFilterPanel();
   }

   @Override
   protected EntityRepository<TechnologicalCardGroup, Long, IdNameFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<TechnologicalCardGroup> getEntityClass() {
      return TechnologicalCardGroup.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.SPOON;
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
      return "dish-group";
   }
}
