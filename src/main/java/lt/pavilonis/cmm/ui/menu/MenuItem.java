package lt.pavilonis.cmm.ui.menu;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

public abstract class MenuItem extends MButton {

   public MenuItem() {
      setWidth("200px");
   }

   abstract public void collectLayoutElements(MVerticalLayout layout);
}