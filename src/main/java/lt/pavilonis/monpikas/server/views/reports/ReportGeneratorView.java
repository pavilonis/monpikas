package lt.pavilonis.monpikas.server.views.reports;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.monpikas.server.reports.ReportService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.vaadin.shared.ui.label.ContentMode.HTML;
import static com.vaadin.ui.Alignment.MIDDLE_CENTER;

public class ReportGeneratorView extends VerticalLayout {

   Button generateButton = new Button("Sukurti");
   StreamResource streamResource;
   DateField from;
   DateField to;

   public ReportGeneratorView(ReportService service) {

      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
      cal.add(Calendar.MONTH, -1);
      cal.set(Calendar.DAY_OF_MONTH, 1);
      cal.set(Calendar.HOUR, 1);
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
      addComponents(
            new Label("<center><h3>Ataskaitos sukūrimas pasirinktam laikotarpiui</h3></center>", HTML),
            hl,
            generateButton
      );

      streamResource = getStream(service);
      FileDownloader fileDownloader = new FileDownloader(streamResource);
      fileDownloader.extend(generateButton);
   }

   private StreamResource getStream(ReportService service) {

      StreamResource.StreamSource source = () -> {

         ByteArrayOutputStream stream = service.generate(from.getValue(), to.getValue());

         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         streamResource.setFilename("ataskaita_" + sdf.format(from.getValue()) + "_" + sdf.format(to.getValue()) + ".xls");

         return new ByteArrayInputStream(stream.toByteArray());
      };

      return new StreamResource(source, "ataskaita.xls");
   }
}
