package lt.pavilonis.cmm.warehouse.techcardgroup;

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
public class TechCardGroupListController extends AbstractListController<TechCardGroup, Long, IdTextFilter> {

   @Autowired
   private TechCardGroupRepository repository;

   @Override
   protected ListGrid<TechCardGroup> createGrid() {
      return new ListGrid<>(TechCardGroup.class);
   }

   @Override
   protected AbstractFormController<TechCardGroup, Long> getFormController() {
      return new AbstractFormController<TechCardGroup, Long>(TechCardGroup.class) {
         @Override
         protected EntityRepository<TechCardGroup, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<TechCardGroup> createFieldLayout(TechCardGroup model) {
            return new TechCardGroupForm();
         }
      };
   }

   @Override
   protected FilterPanel<IdTextFilter> createFilterPanel() {
      return new NameFilterPanel();
   }

   @Override
   protected EntityRepository<TechCardGroup, Long, IdTextFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<TechCardGroup> getEntityClass() {
      return TechCardGroup.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.RECORDS;
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
      return "tech-card-group";
   }
}
