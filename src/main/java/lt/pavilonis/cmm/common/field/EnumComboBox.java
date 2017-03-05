package lt.pavilonis.cmm.common.field;

import com.vaadin.ui.ComboBox;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;

import java.util.Arrays;
import java.util.List;

public class EnumComboBox<T extends Enum<T>> extends ComboBox<T> {

   private final MessageSourceAdapter messages = App.context.getBean(MessageSourceAdapter.class);

   public EnumComboBox(Class<T> clazz) {
      super();
      List<T> enums = Arrays.asList(clazz.getEnumConstants());

      setItems(enums);
      setValue(enums.get(0));
      setCaption(messages.get(clazz, "name"));
      setItemCaptionGenerator(item -> messages.get(item, item.toString()));
      setEmptySelectionAllowed(false);
   }

   public EnumComboBox<T> withRequired(boolean value) {
      this.setRequiredIndicatorVisible(value);
      return this;
   }
}
