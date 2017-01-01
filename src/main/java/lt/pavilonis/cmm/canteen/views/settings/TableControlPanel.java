package lt.pavilonis.cmm.canteen.views.settings;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

import static com.vaadin.ui.Button.ClickListener;

public class TableControlPanel extends HorizontalLayout {

   private Button addButton = new Button("Pridėti", FontAwesome.PLUS);
   private Button deleteButton = new Button("Šalinti", FontAwesome.WARNING);

   public TableControlPanel() {
      deleteButton.addStyleName("redicon");
      addComponents(addButton, deleteButton);
      setSpacing(true);
      setMargin(true);//new MarginInfo(false, true, true, true));
      setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
      setComponentAlignment(deleteButton, Alignment.MIDDLE_RIGHT);
   }

   public void addAddListener(ClickListener clickListener) {
      addButton.addClickListener(clickListener);
   }

   public void addDeleteListener(ClickListener clickListener) {
      deleteButton.addClickListener(clickListener);
   }
}