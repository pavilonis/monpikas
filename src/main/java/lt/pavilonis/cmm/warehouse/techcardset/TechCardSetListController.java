package lt.pavilonis.cmm.warehouse.techcardset;

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
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import lt.pavilonis.cmm.common.ui.filter.NameFilterPanel;
import lt.pavilonis.cmm.warehouse.techcardsettype.TechCardSetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class TechCardSetListController extends AbstractListController<TechCardSet, Long, IdTextFilter> {

   @Autowired
   private TechCardSetRepository repository;

   @Autowired
   private TechCardSetTypeRepository mealTypeRepository;

   @Override
   protected ListGrid<TechCardSet> createGrid() {
      return new ListGrid<TechCardSet>(TechCardSet.class) {
         @Override
         protected List<String> columnOrder() {
            return Arrays.asList("name", "type", "techCards", "caloricity");
         }
      };
   }

   @Override
   protected FilterPanel<IdTextFilter> createFilterPanel() {
      return new NameFilterPanel();
   }

   @Override
   protected AbstractFormController<TechCardSet, Long> getFormController() {
      return new AbstractFormController<TechCardSet, Long>(TechCardSet.class) {
         @Override
         protected EntityRepository<TechCardSet, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<TechCardSet> createFieldLayout(TechCardSet model) {
            return new TechCardSetFields(mealTypeRepository.load(IdTextFilter.empty()));
         }

         @Override
         protected void customizeWindow(Window window) {
            super.customizeWindow(window);
            window.setHeight(570, Sizeable.Unit.PIXELS);
         }
      };
   }

   @Override
   protected EntityRepository<TechCardSet, Long, IdTextFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<TechCardSet> getEntityClass() {
      return TechCardSet.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.CUTLERY;
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
      return "techCardSet";
   }
}
