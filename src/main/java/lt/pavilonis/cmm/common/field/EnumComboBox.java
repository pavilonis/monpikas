package lt.pavilonis.cmm.common.field;

import com.vaadin.ui.ComboBox;
import lt.pavilonis.cmm.App;

import java.util.Arrays;
import java.util.List;

public class EnumComboBox<T extends Enum> extends ComboBox<T> {

   public EnumComboBox(Class<T> clazz) {
      super();
      List<T> enums = Arrays.asList(clazz.getEnumConstants());

      setItems(enums);
      setValue(enums.get(0));
      setCaption(App.translate(clazz, "name"));
      setItemCaptionGenerator(item -> App.translate(item, item.toString()));
      setEmptySelectionAllowed(false);
   }

   public EnumComboBox<T> withRequired(boolean value) {
      this.setRequiredIndicatorVisible(value);
      return this;
   }
}
