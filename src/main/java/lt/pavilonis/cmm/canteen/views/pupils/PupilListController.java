package lt.pavilonis.cmm.canteen.views.pupils;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.canteen.views.mealevents.MealEventListView;
import lt.pavilonis.cmm.common.AbstractViewController;

@UIScope
@SpringComponent
public class PupilListController extends AbstractViewController {

   @Override
   protected Class<? extends Component> getMainAreaClass() {
      return MealEventListView.class;
   }

   @Override
   protected Class<? extends Component> getHeaderAreaClass() {
      return PupilListFilterPanel.class;
   }

   @Override
   protected FontAwesome getMenuIcon() {
      return FontAwesome.CHILD;
   }
}
