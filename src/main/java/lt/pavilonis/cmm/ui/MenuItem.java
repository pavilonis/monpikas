package lt.pavilonis.cmm.ui;

import com.vaadin.server.FontAwesome;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

public abstract class MenuItem extends MButton {

   public MenuItem(MessageSourceAdapter messages) {
      setWidth("170px");
      setCaption(messages.get(this, "caption"));
      addStyleName("text-align-left");
      setIcon(getMenuIcon());
   }

   abstract public void collectLayoutElements(MVerticalLayout layout);

   abstract protected FontAwesome getMenuIcon();
}