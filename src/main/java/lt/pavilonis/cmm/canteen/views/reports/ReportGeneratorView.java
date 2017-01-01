package lt.pavilonis.cmm.canteen.views.reports;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.reports.ReportService;
import lt.pavilonis.cmm.util.DateUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.vaadin.shared.ui.label.ContentMode.HTML;
import static com.vaadin.ui.Alignment.MIDDLE_CENTER;
import static java.util.Arrays.asList;
import static lt.pavilonis.cmm.util.Messages.label;

public class ReportGeneratorView extends VerticalLayout {

   private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
   private final StreamResource streamResource;
   private final ComboBox pupilTypeCombo;
   private final DateField from;
   private final DateField to;

   public ReportGeneratorView(ReportService service) {

      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
      cal.set(Calendar.DAY_OF_MONTH, 1);
      Date firstDayOfLastMonth = cal.getTime();

      cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
      Date lastDayOfLastMonth = cal.getTime();

      from = new DateField("Nuo", firstDayOfLastMonth);
      from.setDateFormat("yyyy-MM-dd");
      from.setImmediate(true);
      to = new DateField("Iki", lastDayOfLastMonth);
      to.setDateFormat("yyyy-MM-dd");
      to.setImmediate(true);

      setMargin(true);
      setDefaultComponentAlignment(MIDDLE_CENTER);
      setSpacing(true);
      HorizontalLayout hl = new HorizontalLayout(from, to);
      hl.setSpacing(true);
      hl.setMargin(new MarginInfo(true, true, true, true));

      pupilTypeCombo = new ComboBox("Tipas", asList(PupilType.values())) {
         @Override
         public String getItemCaption(Object itemId) {
            return label("PupilType." + itemId.toString());
         }
      };
      pupilTypeCombo.setNullSelectionAllowed(false);
      pupilTypeCombo.setImmediate(true);
      pupilTypeCombo.setValue(PupilType.SOCIAL);

      Button generateButton = new Button("Sukurti");
      addComponents(
            new Label(label("ReportGeneratorView.title"), HTML),
            hl,
            pupilTypeCombo,
            generateButton
      );

      streamResource = getStream(service);
      FileDownloader fileDownloader = new FileDownloader(streamResource);
      fileDownloader.extend(generateButton);
   }

   private StreamResource getStream(ReportService service) {

      StreamResource.StreamSource source = () -> {
         PupilType type = (PupilType) pupilTypeCombo.getValue();
         Date periodStart = DateUtils.startOfDay(from.getValue());
         Date periodEnd = DateUtils.endOfDay(to.getValue());

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
}
