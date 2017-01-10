package lt.pavilonis.cmm.canteen.views.setting;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.common.AbstractViewController;

@SpringComponent
@UIScope
public class SettingsViewController extends AbstractViewController {
   @Override
   protected Class<? extends Component> getMainAreaClass() {
      return MealListView.class;
   }

   @Override
   protected FontAwesome getMenuIcon() {
      return FontAwesome.WRENCH;
   }
}
