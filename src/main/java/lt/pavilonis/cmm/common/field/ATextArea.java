package lt.pavilonis.cmm.common.field;

import com.vaadin.ui.TextArea;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;


public class ATextArea extends TextArea {

   public ATextArea(String labelCode) {
      String caption = App.context.getBean(MessageSourceAdapter.class)
            .get(labelCode);

      this.setCaption(caption);
   }
}
