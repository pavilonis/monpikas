package lt.pavilonis.monpikas.server.views.pupils;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.monpikas.server.domain.Portion;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import lt.pavilonis.monpikas.server.dto.AdbPupilDto;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.vaadin.shared.ui.label.ContentMode.HTML;
import static com.vaadin.ui.Alignment.BOTTOM_CENTER;
import static com.vaadin.ui.Button.ClickListener;
import static java.util.stream.Collectors.toList;
import static lt.pavilonis.monpikas.server.domain.enumeration.PortionType.BREAKFAST;
import static lt.pavilonis.monpikas.server.domain.enumeration.PortionType.DINNER;

public class PupilEditWindow extends Window {

   private final static Format DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

   @PropertyId("comment")
   Button save = new Button("Saugoti");
   Button close = new Button("Uždaryti");
   BeanFieldGroup<PupilInfo> group = new BeanFieldGroup<>(PupilInfo.class);
   ComboBox breakfastPortionCombo;
   ComboBox dinnerPortionCombo;

   public PupilEditWindow(PupilInfo info, AdbPupilDto dto, Image image, Optional<Date> lastMealDate, List<Portion> portions) {

      List<Portion> breakfasts = portions.stream() //get only breakfasts and filter out current value
            .filter(p -> p.getType() == BREAKFAST && (info.getBreakfastPortion() == null || !p.getId().equals(info.getBreakfastPortion().getId())))
            .collect(toList());

      List<Portion> dinners = portions.stream()
            .filter(p -> p.getType() == DINNER && (info.getDinnerPortion() == null || !p.getId().equals(info.getDinnerPortion().getId())))
            .collect(toList());

      if (info.getBreakfastPortion() != null) {    //add current value (this way vaadin combobox displays current value)
         breakfasts.add(info.getBreakfastPortion());
      }
      if (info.getDinnerPortion() != null) {
         dinners.add(info.getDinnerPortion());
      }

      breakfastPortionCombo = new ComboBox("Pusryčiai", breakfasts);
      dinnerPortionCombo = new ComboBox("Pietus", dinners);

      group.bind(breakfastPortionCombo, "breakfastPortion");
      group.bind(dinnerPortionCombo, "dinnerPortion");
      group.setItemDataSource(info);
      group.setBuffered(true);
      breakfastPortionCombo.select(info.getBreakfastPortion());

      setCaption("Mokinio nustatymai");
      setResizable(false);
      setWidth("510px");
      setHeight("580px");

      String birthDate = dto.getBirthDate().isPresent() ? dto.getBirthDate().get().toString() : "nenurodyta";
      String mealDate = lastMealDate.isPresent() ? DATE_FORMAT.format(lastMealDate.get()) : "nėra duomenų";

      Label last = new Label("<b>Paskutinis<br/>maitinimasis:</b> " + mealDate, HTML);
      Label card = new Label("<b>Kortelės #:</b> " + dto.getCardId(), HTML);
      Label name = new Label("<b>Vardas:</b> " + dto.getFirstName() + " " + dto.getLastName(), HTML);
      Label date = new Label("<b>Gimimo data:</b> " + birthDate, HTML);

      VerticalLayout vl1 = new VerticalLayout(card, name, date, last);
      vl1.setSpacing(true);
      vl1.setMargin(true);

      TextField grade = group.buildAndBind("Klasė", "grade", TextField.class);
      grade.setNullRepresentation("");

      TextArea comment = group.buildAndBind("Komentaras", "comment", TextArea.class);
      comment.setNullRepresentation("");

      vl1.addComponents(
            breakfastPortionCombo,
            dinnerPortionCombo,
            grade
      );

      HorizontalLayout buttons = new HorizontalLayout(save, close);
      buttons.setSpacing(true);
      buttons.setMargin(new MarginInfo(true, false, false, false));
      vl1.addComponent(buttons);
      vl1.setComponentAlignment(buttons, BOTTOM_CENTER);

      GridLayout gl = new GridLayout(2, 1);
      image.setWidth("170px");
      image.setHeight("211px");

      VerticalLayout vl2 = new VerticalLayout(image, comment);
      vl2.setComponentAlignment(image, BOTTOM_CENTER);
      vl2.setSpacing(true);

      breakfastPortionCombo.setWidth("220px");
      dinnerPortionCombo.setWidth("220px");
      grade.setWidth("220px");
      name.setWidth("220px");

      gl.addComponents(vl1, vl2);
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
         group.commit();
      } catch (FieldGroup.CommitException e) {
         e.printStackTrace();
      }
   }

   public boolean isValid() {
      return group.isValid();
   }
}
