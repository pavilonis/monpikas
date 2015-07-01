package lt.pavilonis.monpikas.server.views.pupils;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import lt.pavilonis.monpikas.server.domain.MealType;
import lt.pavilonis.monpikas.server.views.components.MealTypeComboBox;

import static com.vaadin.ui.Button.ClickListener;

public class PupilListFilterPanel extends HorizontalLayout {

   private final TextField text = new TextField();
   private final ComboBox mealType = new MealTypeComboBox();
   private final Button filterButton = new Button("Filtruoti", FontAwesome.FILTER);
   private final Button cancelFilterButton = new Button("Valyti", FontAwesome.REFRESH);

   public PupilListFilterPanel() {
//      mealType.setEnabled(false);
      mealType.setCaption(null);
      mealType.setNullSelectionAllowed(true);
      mealType.setValue(null);
      addComponents(text, mealType, filterButton, cancelFilterButton);
      setSpacing(true);
      setMargin(true);
      setComponentAlignment(filterButton, Alignment.MIDDLE_RIGHT);
      text.focus();
   }

   public void addFilterButtonListener(ClickListener listener) {
      filterButton.addClickListener(listener);
   }

   public PupilFilter getFilter() {
      return new PupilFilter(text.getValue(), (MealType) mealType.getValue());
   }

   public void addCancelFilterButtonListener(ClickListener listener) {
      cancelFilterButton.addClickListener(listener);
   }

   public void cleanFields() {
      text.setValue("");
      mealType.setValue(null);
   }
}

