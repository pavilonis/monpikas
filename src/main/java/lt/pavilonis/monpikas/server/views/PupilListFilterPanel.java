package lt.pavilonis.monpikas.server.views;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import static com.vaadin.ui.Button.ClickListener;

public class PupilListFilterPanel extends HorizontalLayout {

   private TextField text = new TextField();
   private CheckBox dinnerPermission = new CheckBox("Gali pietauti");
   private Button filterButton = new Button("Filtruoti");

   public PupilListFilterPanel() {
      Label lbl = new Label("Filtras");
      addComponents(lbl, text, dinnerPermission, filterButton);
      setSpacing(true);
      setMargin(true);
      setComponentAlignment(lbl, Alignment.MIDDLE_RIGHT);
      setComponentAlignment(dinnerPermission, Alignment.MIDDLE_CENTER);
      setComponentAlignment(filterButton, Alignment.MIDDLE_RIGHT);
      text.focus();
   }

   public void addFilterButtonListener(ClickListener listener) {
      filterButton.addClickListener(listener);
   }

   public PupilFilter getFilter() {
      return new PupilFilter(text.getValue(), dinnerPermission.getValue());
   }
}

