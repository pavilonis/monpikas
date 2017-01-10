package lt.pavilonis.cmm.common;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;


public class AbstractFormController extends Window implements FormController {

   @Autowired
   protected MessageSourceAdapter messageSourceAdapter;

   @Override
   public void actionSave() {

   }

   @Override
   public void actionClose() {
      close();
   }

   public Component composeLayout() {
      return new MVerticalLayout(
            createFields(),
            createControls()
      );
   }

   private Component createControls() {
      return new MHorizontalLayout(
            new MButton(FontAwesome.CHECK, messageSourceAdapter.get(this, "buttonSave"), click -> actionSave()),
            new MButton(FontAwesome.REMOVE, messageSourceAdapter.get(this, "buttonClose"), click -> actionClose())
      );
   }

   protected Component createFields() {
      return new MVerticalLayout();
   }
}
