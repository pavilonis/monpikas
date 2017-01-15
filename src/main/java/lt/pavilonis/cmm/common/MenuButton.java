package lt.pavilonis.cmm.common;

import com.vaadin.server.Resource;
import org.vaadin.viritin.button.MButton;

public class MenuButton extends MButton {

   Class<? extends AbstractViewController> controllerClass;

   public MenuButton(Class<? extends AbstractViewController> controllerClass, Resource icon) {
      setIcon(icon);
      this.controllerClass = controllerClass;
      setWidth("200px");
      addStyleName("text-align-left");
   }

   public Class<? extends AbstractViewController> getControllerClass() {
      return controllerClass;
   }
}
