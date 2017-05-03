package lt.pavilonis.cmm.warehouse.menurequirement;

import com.vaadin.icons.VaadinIcons;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.ui.filter.IdPeriodFilter;
import lt.pavilonis.cmm.common.ui.filter.PeriodFilterPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MenuRequirementListController extends AbstractListController<MenuRequirement, Long, IdPeriodFilter> {

   @Autowired
   private MenuRequirementRepository repository;

   @Override
   protected ListGrid<MenuRequirement> createGrid() {
      return new ListGrid<>(MenuRequirement.class);
   }

   @Override
   protected AbstractFormController<MenuRequirement, Long> getFormController() {
      return new AbstractFormController<MenuRequirement, Long>(MenuRequirement.class) {
         @Override
         protected EntityRepository<MenuRequirement, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<MenuRequirement> createFieldLayout() {
            return new MenuRequirementForm();
         }
      };
   }

   @Override
   protected FilterPanel<IdPeriodFilter> createFilterPanel() {
      return new PeriodFilterPanel();
   }

   @Override
   protected EntityRepository<MenuRequirement, Long, IdPeriodFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<MenuRequirement> getEntityClass() {
      return MenuRequirement.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.LIST_OL;
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
      return "menu-requirement";
   }
}
