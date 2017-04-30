package lt.pavilonis.cmm.canteen.ui.setting;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.canteen.domain.Eating;
import lt.pavilonis.cmm.canteen.repository.EatingRepository;
import lt.pavilonis.cmm.canteen.ui.user.EatingGrid;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.HiddenFilterPanel;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.ui.filter.IdNameFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

@SpringComponent
@UIScope
public class EatingListController extends AbstractListController<Eating, Long, IdNameFilter> {

   @Autowired
   private EatingRepository repository;

   @Override
   protected AbstractFormController<Eating, Long> getFormController() {
      return new AbstractFormController<Eating, Long>(Eating.class) {

         @Override
         protected EntityRepository<Eating, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<Eating> createFieldLayout() {
            return new EatingFormView();
         }
      };
   }

   @Override
   protected ListGrid<Eating> createGrid() {
      return new EatingGrid(Collections.emptyList());
   }

   @Override
   protected FilterPanel<IdNameFilter> createFilterPanel() {
      return new HiddenFilterPanel<>();
   }

   @Override
   protected EntityRepository<Eating, Long, IdNameFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<Eating> getEntityClass() {
      return Eating.class;
   }

   @Override
   public String getViewName() {
      return "eatings";
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.WRENCH;
   }

   @Override
   public String getViewRole() {
      return "EATING_CONFIG";
   }

   @Override
   public String getViewGroupName() {
      return "canteen";
   }
}
