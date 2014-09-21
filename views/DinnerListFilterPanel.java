package lt.pavilonis.monpikas.server.views;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import static com.vaadin.ui.Button.ClickListener;

public class DinnerListFilterPanel extends HorizontalLayout {

   private TextField text = new TextField();
   private Button filterButton = new Button("Filtruoti");

   public DinnerListFilterPanel() {
      Label lbl = new Label("Filtras");
      addComponents(lbl, text, filterButton);
      setSpacing(true);
      setMargin(true);
      setComponentAlignment(lbl, Alignment.MIDDLE_RIGHT);
      setComponentAlignment(filterButton, Alignment.MIDDLE_RIGHT);
      text.focus();
   }

   public void addFilterButtonListener(ClickListener listener) {
      filterButton.addClickListener(listener);
   }

   public DinnerFilter getFilter() {
      return new DinnerFilter(text.getValue());
   }
}