package lt.pavilonis.cmm.canteen.views.report;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.report.ReportService;
import lt.pavilonis.cmm.canteen.views.component.EnumComboBox;
import lt.pavilonis.cmm.common.AbstractViewController;
import lt.pavilonis.cmm.common.components.ADateField;
import lt.pavilonis.cmm.util.DateUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.vaadin.shared.ui.label.ContentMode.HTML;
import static com.vaadin.ui.Alignment.TOP_CENTER;

@SpringComponent
@UIScope
public class CanteenReportViewController extends AbstractViewController {

   private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
   private final ComboBox pupilTypeCombo = pupilTypeCombo();
   private final DateField periodStartField = new ADateField(this.getClass(), "periodStart")
         .withValue(LocalDateTime.now().withDayOfMonth(1).withTime(0, 0, 0, 0).toDate())
         .withRequired()
         .withImmediate();

   private final DateField periodEndField = new ADateField(this.getClass(), "periodEnd")
         .withValue(LocalDateTime.now().dayOfMonth().withMaximumValue().withTime(23, 59, 59, 999).toDate())
         .withRequired()
         .withImmediate();

   private StreamResource streamResource;

   @Autowired
   private ReportService service;

   private StreamResource getStream(ReportService service) {

      StreamResource.StreamSource source = () -> {
         PupilType type = (PupilType) pupilTypeCombo.getValue();
         Date periodStart = DateUtils.startOfDay(periodStartField.getValue());
         Date periodEnd = DateUtils.endOfDay(periodEndField.getValue());

         ByteArrayOutputStream stream = service.generate(periodStart, periodEnd, type);
         streamResource.setFilename(
               String.join("_",
                     "ataskaita",
                     type.toString().toLowerCase(),
                     DATE_FORMAT.format(periodStart),
                     DATE_FORMAT.format(periodEnd) + ".xls"
               )
         );

         return new ByteArrayInputStream(stream.toByteArray());
      };

      return new StreamResource(source, "ataskaita.xls");
   }

   @Override
   protected Component getMainArea() {

      Button generateButton = new Button(messages.get(this, "buttonGenerate"));

      MVerticalLayout layout = new MVerticalLayout(
            new Label(messages.get(this, "title"), HTML),
            new MHorizontalLayout(periodStartField, periodEndField),
            pupilTypeCombo,
            generateButton
      )
            .withHeight("350px")
            .alignAll(TOP_CENTER);

      streamResource = getStream(service);
      FileDownloader fileDownloader = new FileDownloader(streamResource);
      fileDownloader.extend(generateButton);

      return layout;
   }

   private ComboBox pupilTypeCombo() {
      ComboBox combo = new EnumComboBox<>(PupilType.class);
      combo.setNullSelectionAllowed(false);
      combo.setImmediate(true);
      combo.setValue(PupilType.SOCIAL);
      return combo;
   }
}
