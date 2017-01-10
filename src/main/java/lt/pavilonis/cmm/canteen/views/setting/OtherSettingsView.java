package lt.pavilonis.cmm.canteen.views.setting;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class OtherSettingsView extends VerticalLayout {

   Button saveButton = new Button("Saugoti");

   public OtherSettingsView() {
      setSpacing(true);
      setMargin(true);

      Label title = new Label("Kiti nustatymai (neveikia, bus realizuota vėliau)");
      ComboBox hour = new ComboBox("Pusryčių ir pietų riba (val.)");
      TextField currencyTextField = new TextField("Valiutos pavadinimas");
      TextField employeePosition = new TextField("Sąskaita, daruotojo pareigos");
      TextField employeeName = new TextField("Sąskaita, daruotojo vardas");

//      addComponents(
//            title,
//            hour,
//            currencyTextField,
//            employeePosition,
//            employeeName
//      );

//      setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
//      setComponentAlignment(title, Alignment.TOP_LEFT);
   }

   public void addSaveButtonClickListener(Button.ClickListener listener) {
      saveButton.addClickListener(listener);
   }
}
