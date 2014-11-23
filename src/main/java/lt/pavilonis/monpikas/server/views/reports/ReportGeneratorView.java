package lt.pavilonis.monpikas.server.views.reports;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

import java.io.InputStream;

public class ReportGeneratorView extends HorizontalLayout {

   Button generateButton = new Button("Generuoti");

   public ReportGeneratorView() {
      setSizeFull();
      setSpacing(true);

//      StreamResource sr = getPDFStream();
//      FileDownloader fileDownloader = new FileDownloader(sr);
//      fileDownloader.extend(generateButton);
//


      addComponent(generateButton);
   }

//   private StreamResource getPDFStream() {
//      StreamResource.StreamSource source = new StreamResource.StreamSource() {
//
//         public InputStream getStream() {
//            // return your file/bytearray as an InputStream
//            return input;
//
//         }
//      };
//      StreamResource resource = new StreamResource ( source, getFileName());
//      return resource;
//   }

   public void addGenerateActionListener(Button.ClickListener listener) {
      generateButton.addClickListener(listener);
   }
}
