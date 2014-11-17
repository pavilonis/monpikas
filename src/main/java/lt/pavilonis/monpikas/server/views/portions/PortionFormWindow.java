package lt.pavilonis.monpikas.server.views.portions;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import lt.pavilonis.monpikas.server.domain.Portion;
import lt.pavilonis.monpikas.server.domain.enumeration.PortionType;

import static com.vaadin.ui.Alignment.BOTTOM_CENTER;
import static com.vaadin.ui.Button.ClickListener;
import static java.util.Arrays.asList;

public class PortionFormWindow extends Window {

   BeanFieldGroup<Portion> group = new BeanFieldGroup<>(Portion.class);
   ComboBox type = new ComboBox("Tipas", asList(PortionType.values()));

   Button save = new Button("Saugoti");
   Button close = new Button("Uždaryti");

   public PortionFormWindow(Item item) {
      this();
      group.setItemDataSource(item);
   }

   public PortionFormWindow() {
      group.setItemDataSource(new Portion());
      setCaption("Porcijos tipas");
      setResizable(false);
      setWidth("350px");
      setHeight("280px");

      type.setNullSelectionAllowed(false);
      type.setItemCaption(PortionType.BREAKFAST, "Pusryčiai");
      type.setItemCaption(PortionType.DINNER, "Pietus");
      group.bind(type, "type");

      FormLayout fl = new FormLayout(
            group.buildAndBind("Pavadinimas", "name"),
            type,
            group.buildAndBind("Kaina (Lt.)", "price")
      );
      ((TextField) group.getField("name")).setNullRepresentation("");
      group.setBuffered(true);
      fl.setSpacing(true);
      fl.setMargin(true);

      HorizontalLayout buttons = new HorizontalLayout(save, close);
      buttons.setSpacing(true);
      buttons.setMargin(new MarginInfo(true, false, false, false));
      fl.addComponent(buttons);
      fl.setComponentAlignment(buttons, BOTTOM_CENTER);

      setContent(fl);
      setModal(true);
   }

   public void addSaveButtonListener(ClickListener listener) {
      save.addClickListener(listener);
   }

   public void addCloseButtonListener(ClickListener listener) {
      close.addClickListener(listener);
   }

   public BeanItem<Portion> getItemDateSource() {
      return group.getItemDataSource();
   }

   public void commit() {
      try {
         group.commit();
      } catch (FieldGroup.CommitException e) {
         e.printStackTrace();
      }
   }

   public boolean isValid() {
      return group.isValid();
   }
}
