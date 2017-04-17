package lt.pavilonis.cmm.canteen.ui.report;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.report.ReportService;
import lt.pavilonis.cmm.common.field.AButton;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.EnumComboBox;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import static com.vaadin.ui.Alignment.TOP_CENTER;

@SpringView(name = MealReportViewController.VIEW_NAME)
public class MealReportViewController extends VerticalLayout implements View {

   static final String VIEW_NAME = "meal-report";

   private final ComboBox pupilTypeCombo = pupilTypeCombo();
   private final DateField periodStartField = new ADateField(this.getClass(), "periodStart")
         .withValue(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()))
         .withRequired();

   private final DateField periodEndField = new ADateField(this.getClass(), "periodEnd")
         .withValue(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()))
         .withRequired();

   private StreamResource streamResource;

   @Autowired
   public MealReportViewController(ReportService service) {
      Button generateButton = new AButton(this.getClass(), "buttonGenerate");
      setDefaultComponentAlignment(TOP_CENTER);
      addComponents(
            new Label(App.translate(this, "title"), ContentMode.HTML),
            new HorizontalLayout(periodStartField, periodEndField),
            pupilTypeCombo,
            generateButton
      );
      setHeight(350, Sizeable.Unit.PIXELS);
      setMargin(false);
      setWidth(100, Sizeable.Unit.PERCENTAGE);

      streamResource = getStream(service);
      FileDownloader fileDownloader = new FileDownloader(streamResource);
      fileDownloader.extend(generateButton);
   }

   private StreamResource getStream(ReportService service) {

      StreamResource.StreamSource source = () -> {
         PupilType type = (PupilType) pupilTypeCombo.getValue();

         ByteArrayOutputStream stream =
               service.generate(periodStartField.getValue(), periodEndField.getValue(), type);

         streamResource.setFilename(
               String.join(
                     "_",
                     "ataskaita",
                     type.toString().toLowerCase(),
                     DateTimeFormatter.ISO_DATE.format(periodStartField.getValue()),
                     DateTimeFormatter.ISO_DATE.format(periodEndField.getValue()) + ".xls"
               )
         );

         return new ByteArrayInputStream(stream.toByteArray());
      };

      return new StreamResource(source, "ataskaita.xls");
   }

   private ComboBox pupilTypeCombo() {
      ComboBox<PupilType> combo = new EnumComboBox<>(PupilType.class);
      combo.setEmptySelectionAllowed(false);
      combo.setValue(PupilType.SOCIAL);
      return combo;
   }

   @Override
   public void enter(ViewChangeListener.ViewChangeEvent event) {
   }
}
