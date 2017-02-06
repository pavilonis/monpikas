package lt.pavilonis.cmm.common;

import com.vaadin.server.Resource;
import org.vaadin.viritin.button.MButton;

public class MenuButton extends MButton {

   private final Class<? extends AbstractViewController> controllerClass;
   private final String roleName;

   public MenuButton(Class<? extends AbstractViewController> controllerClass, String roleName, Resource icon) {
      this.controllerClass = controllerClass;
      this.roleName = roleName;
      setIcon(icon);
      setWidth("200px");
      addStyleName("text-align-left");
   }

   public Class<? extends AbstractViewController> getControllerClass() {
      return controllerClass;
   }

   public String getRoleName() {
      return roleName;
   }
}
