package lt.pavilonis.monpikas.server.views.mealevents;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.vaadin.ui.Button.ClickListener;

public class MealEventManualCreateWindow extends Window {

   TextArea comment = new TextArea("Komentaras");
   Button save = new Button("Saugoti");
   Button close = new Button("Uždaryti");
   FieldGroup editFields;
   CheckBox breakfastPermitted = new CheckBox("Pusryčiai");
   CheckBox dinnerPermitted = new CheckBox("Pietus");

   public MealEventManualCreateWindow(Item item, Image image, Date lastDinner) {
      editFields = new FieldGroup(item);
      setCaption("Mokinio nustatymai");
      setResizable(false);
      setWidth("550px");
      setHeight("580px");
      VerticalLayout vl = new VerticalLayout();
      vl.setSpacing(true);
      vl.setMargin(true);

      long cardId = (long) item.getItemProperty("cardId").getValue();
      vl.addComponent(new Label("<b>Kortelės #:</b> " + cardId, ContentMode.HTML));

      String name = item.getItemProperty("firstName").getValue() + " " + item.getItemProperty("lastName").getValue();
      Label nameLbl = new Label("<b>Vardas:</b> " + name, ContentMode.HTML);
      nameLbl.setWidth("250px");
      vl.addComponent(nameLbl);

      String date = String.valueOf((item.getItemProperty("birthDate").getValue() != null)
            ? item.getItemProperty("birthDate").getValue()
            : "nenurodyta");
      vl.addComponent(new Label("<b>Gimimo data:</b> " + date, ContentMode.HTML));

      String lastDinnerString = lastDinner == null
            ? "nėra duomenų"
            : new SimpleDateFormat("yyyy-MM-dd HH:mm").format(lastDinner);
      vl.addComponent(new Label("<b>Paskutinis<br/>maitinimasis:</b> " + lastDinnerString, ContentMode.HTML));

      vl.addComponent(breakfastPermitted);
      vl.addComponent(dinnerPermitted);
      comment.setRows(4);
      vl.addComponent(comment);
      editFields.bind(breakfastPermitted, "breakfastPermitted");
      editFields.bind(dinnerPermitted, "dinnerPermitted");
      editFields.bind(comment, "comment");

      HorizontalLayout buttons = new HorizontalLayout(save, close);
      buttons.setSpacing(true);
      buttons.setMargin(new MarginInfo(true, false, false, false));
      vl.addComponent(buttons);
      vl.setComponentAlignment(buttons, Alignment.BOTTOM_CENTER);

      GridLayout gl = new GridLayout(2, 1);
      image.setWidth("160px");
      image.setHeight("200px");
      gl.addComponents(vl, image);
      setContent(gl);
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
}
