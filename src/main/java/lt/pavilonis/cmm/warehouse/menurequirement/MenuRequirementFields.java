package lt.pavilonis.cmm.warehouse.menurequirement;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.Binder;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.Editor;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.NumberField;
import lt.pavilonis.cmm.common.field.OneToManyField;

import java.util.Arrays;
import java.util.List;

public final class MenuRequirementFields extends FieldLayout<MenuRequirement> {

   private final OneToManyField<TechCardSetNumber> techCardSets = new OneToManyField<TechCardSetNumber>(
         TechCardSetNumber.class,
         ImmutableMap.of(
               "name", item -> item.getTechCardSet().getName(),
               "type", item -> item.getTechCardSet().getType().getName(),
               "caloricity", item -> item.getTechCardSet().getCaloricity()
         ),
         "name", "type", "caloricity", "number"
   ) {
      @Override
      protected List<String> getSelectionPopupColumnOrder() {
         return Arrays.asList("name", "type", "caloricity");
      }

      @Override
      protected ListGrid<TechCardSetNumber> createGrid(Class<TechCardSetNumber> type) {
         ListGrid<TechCardSetNumber> grid = super.createGrid(type);

         NumberField number = new NumberField();

         Editor<TechCardSetNumber> editor = grid.getEditor();

         Binder.Binding<TechCardSetNumber, Integer> binding = editor
               .getBinder()
               .bind(number, TechCardSetNumber::getNumber, TechCardSetNumber::setNumber);

         Grid.Column<TechCardSetNumber, Integer> numberColumn =
               (Grid.Column<TechCardSetNumber, Integer>) grid.getColumn("number");

         numberColumn
               .setEditorComponent(number, TechCardSetNumber::setNumber)
               .setEditorBinding(binding)
               .setWidth(120);

         editor.setEnabled(true);
         editor.setSaveCaption(App.translate("save"));
         editor.setCancelCaption(App.translate("cancel"));

         return grid;
      }
   };
   private final DateField date = new ADateField(MenuRequirement.class, "date");

   public MenuRequirementFields() {
      addComponents(date, techCardSets);
   }
}
