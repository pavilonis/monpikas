package lt.pavilonis.monpikas.server.views;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.monpikas.server.domain.PupilInfo;

import static com.vaadin.ui.Button.ClickListener;

public class PupilEditWindow extends Window {

   TextArea comment = new TextArea("Komentaras");
   Button save = new Button("Saugoti");
   Button close = new Button("Uždaryti");
   FieldGroup editFields;
   CheckBox dinnerPermission = new CheckBox("Leidimas valgyti");

   public PupilEditWindow(Item item, String caption) {
      editFields = new FieldGroup(item);
      setCaption(caption);
      setResizable(false);
      setWidth("450px");
      setHeight("450px");
      VerticalLayout vl = new VerticalLayout();
      vl.setSpacing(true);
      vl.setMargin(true);

      vl.addComponent(new HorizontalLayout(
            new Label("<b>Kortelės #:</b> " + item.getItemProperty("cardId"), ContentMode.HTML)));
      vl.addComponent(new HorizontalLayout(
            new Label("<b>Vardas:</b> " + item.getItemProperty("firstName") + " " + item.getItemProperty("lastName"), ContentMode.HTML)));

      vl.addComponent(new HorizontalLayout(new Label("<b>Gimimo data</b>: " +
            ((item.getItemProperty("birthDate").getValue() != null) ? item.getItemProperty("birthDate") : "nenurodyta"), ContentMode.HTML)));
      vl.addComponent(dinnerPermission);
      comment.setRows(4);
      vl.addComponent(comment);
      editFields.bind(dinnerPermission, "dinner");
      editFields.bind(comment, "comment");

      HorizontalLayout buttons = new HorizontalLayout(save, close);
      buttons.setSpacing(true);
      buttons.setMargin(new MarginInfo(true, false, false, false));
      vl.addComponent(buttons);
      vl.setComponentAlignment(buttons, Alignment.BOTTOM_CENTER);

      setContent(vl);
      setModal(true);
   }

   public void addSaveButtonListener(ClickListener listener) {
      save.addClickListener(listener);
   }

   public void addCloseButtonListener(ClickListener listener) {
      close.addClickListener(listener);
   }

   public void commit() {
      try {
         editFields.commit();
      } catch (FieldGroup.CommitException e) {
         e.printStackTrace();
      }
   }

   public PupilInfo getModel() {
      return new PupilInfo();
   }
}
