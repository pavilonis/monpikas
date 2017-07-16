package lt.pavilonis.cmm.warehouse.techcardsettype;

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
public class TechCardSetTypeListController extends AbstractListController<TechCardSetType, Long, IdTextFilter> {

   @Autowired
   private TechCardSetTypeRepository repository;

   @Override
   protected ListGrid<TechCardSetType> createGrid() {
      return new ListGrid<>(TechCardSetType.class);
   }

   @Override
   protected AbstractFormController<TechCardSetType, Long> getFormController() {
      return new AbstractFormController<TechCardSetType, Long>(TechCardSetType.class) {
         @Override
         protected EntityRepository<TechCardSetType, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<TechCardSetType> createFieldLayout() {
            return new TechCardSetTypeFields();
         }
      };
   }

   @Override
   protected FilterPanel<IdTextFilter> createFilterPanel() {
      return new NameFilterPanel();
   }

   @Override
   protected EntityRepository<TechCardSetType, Long, IdTextFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<TechCardSetType> getEntityClass() {
      return TechCardSetType.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.CLOCK;
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
      return "meal-type";
   }
}
