package lt.pavilonis.cmm.warehouse.menurequirement;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.Named;
import lt.pavilonis.cmm.common.converter.CollectionValueProviderAdapter;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.ui.filter.IdPeriodFilter;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import lt.pavilonis.cmm.common.ui.filter.PeriodFilterPanel;
import lt.pavilonis.cmm.warehouse.techcardset.TechCardSet;
import lt.pavilonis.cmm.warehouse.techcardsettype.TechCardSetType;
import lt.pavilonis.cmm.warehouse.techcardsettype.TechCardSetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MenuRequirementListController extends AbstractListController<MenuRequirement, Long, IdPeriodFilter> {

   @Autowired
   private MenuRequirementRepository repository;

   @Autowired
   private TechCardSetTypeRepository techCardSetTypeRepository;

   @Override
   protected ListGrid<MenuRequirement> createGrid() {
      return new ListGrid<MenuRequirement>(MenuRequirement.class) {

         @Override
         protected List<String> columnOrder() {
            return Arrays.asList("date", "meals", "caloricity");
         }

         @Override
         protected Map<String, ValueProvider<MenuRequirement, ?>> getCustomColumns() {
            return ImmutableMap.of(
                  "meals", new CollectionValueProviderAdapter<>(
                        menuRequirement -> menuRequirement.getTechCardSets().stream()
                              .map(TechCardSet::getType)
                              .map(Named::getName)
                              .collect(Collectors.toList())
                  )
            );
         }
      };
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
            List<TechCardSetType> types = techCardSetTypeRepository.load(IdTextFilter.empty());
            return new MenuRequirementForm(types);
         }
      };
   }

   @Override
   protected FilterPanel<IdPeriodFilter> createFilterPanel() {
      return new PeriodFilterPanel<>();
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
