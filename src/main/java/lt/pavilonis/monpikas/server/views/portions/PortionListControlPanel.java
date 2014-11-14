package lt.pavilonis.monpikas.server.views.portions;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

import static com.vaadin.ui.Button.ClickListener;

public class PortionListControlPanel extends HorizontalLayout {

   private Button addButton = new Button("Pridėti", FontAwesome.PLUS);
   private Button deleteButton = new Button("Ištrinti", FontAwesome.WARNING);

   public PortionListControlPanel() {
      addComponents(addButton, deleteButton);
      setSpacing(true);
      setMargin(true);
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