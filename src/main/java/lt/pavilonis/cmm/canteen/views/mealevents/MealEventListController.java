package lt.pavilonis.cmm.canteen.views.mealevents;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.common.AbstractViewController;

@UIScope
@SpringComponent
public class MealEventListController extends AbstractViewController {

   @Override
   protected Class<? extends Component> getMainLayoutClass() {
      return MealEventListView.class;
   }

   @Override
   protected FontAwesome getMenuIcon() {
      return FontAwesome.CUTLERY;
   }
}
