package lt.pavilonis.cmm.canteen.views.report;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.common.AbstractViewController;

@SpringComponent
@UIScope
public class CanteenReportViewController extends AbstractViewController {
   @Override
   protected Class<? extends Component> getHeaderAreaClass() {
      return null;
   }

   @Override
   protected Class<? extends Component> getMainAreaClass() {
      return ReportGeneratorView.class;
   }

   @Override
   protected FontAwesome getMenuIcon() {
      return FontAwesome.FILE_EXCEL_O;
   }
}
