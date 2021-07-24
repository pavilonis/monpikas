package lt.pavilonis.monpikas.common;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;

public class ViewLayout extends VerticalLayout implements View {

   public ViewLayout() {
      setMargin(true);
      setSizeFull();
   }

   @Override
   public void enter(ViewChangeListener.ViewChangeEvent event) {
   }
}
