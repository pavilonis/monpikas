package lt.pavilonis.cmm.canteen.views.component;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.ComboBox;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;

import java.util.Arrays;
import java.util.List;

public class EnumComboBox<T extends Enum<T>> extends ComboBox {

   private final MessageSourceAdapter messages = App.context.getBean(MessageSourceAdapter.class);

   public EnumComboBox(Class<T> clazz) {
      List<T> enums = Arrays.asList(clazz.getEnumConstants());
      setContainerDataSource(new BeanItemContainer<>(clazz, enums));
      setValue(enums.get(0));
      setCaption(messages.get(clazz, "name"));
   }

   @Override
   public String getItemCaption(Object itemId) {
      return messages.get(itemId, itemId.toString());
   }

   @Override
   public boolean isNullSelectionAllowed() {
      return false;
   }

}
