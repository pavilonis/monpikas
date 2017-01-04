package lt.pavilonis.cmm.canteen.views.settings;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import static com.vaadin.ui.Button.ClickListener;

public class TableControlPanel extends MHorizontalLayout {

   private Button addButton = new Button("Pridėti", FontAwesome.PLUS);
   private Button deleteButton = new Button("Šalinti", FontAwesome.WARNING);

   public TableControlPanel() {
      deleteButton.addStyleName("redicon");
      addComponents(addButton, deleteButton);
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