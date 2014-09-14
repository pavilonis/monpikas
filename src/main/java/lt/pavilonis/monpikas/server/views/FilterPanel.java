package lt.pavilonis.monpikas.server.views;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

import static com.vaadin.ui.Button.ClickListener;

public class FilterPanel extends Panel {

   private TextField text = new TextField();
   private CheckBox dinnerPermission = new CheckBox("Valgantys");
   private CheckBox hadDinnerToday = new CheckBox("Valgė šiandien");
   private Button searchButton = new Button("Filtruoti");

   public FilterPanel() {
      Label lbl = new Label("Raktinis žodis");

      HorizontalLayout hl = new HorizontalLayout(lbl, text, dinnerPermission, hadDinnerToday, searchButton);
      hl.setSpacing(true);
      //hl.setMargin(true);
      hl.setComponentAlignment(lbl, Alignment.MIDDLE_RIGHT);
      hl.setComponentAlignment(dinnerPermission, Alignment.MIDDLE_CENTER);
      hl.setComponentAlignment(hadDinnerToday, Alignment.MIDDLE_CENTER);
      hl.setComponentAlignment(searchButton, Alignment.MIDDLE_RIGHT);
      setContent(hl);
      text.focus();
   }

   public void addSearchListener(ClickListener listener) {
      searchButton.addClickListener(listener);
   }

//   public PupilFilter getFilter() {
//      return new PupilFilter(text.getValue(), dinnerPermission.getValue());
//   }
}

