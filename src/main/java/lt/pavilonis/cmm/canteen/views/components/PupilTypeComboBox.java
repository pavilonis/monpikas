package lt.pavilonis.monpikas.server.views.components;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.ComboBox;
import lt.pavilonis.monpikas.server.domain.MealType;
import lt.pavilonis.monpikas.server.domain.PupilType;

import java.util.List;

import static java.util.Arrays.asList;
import static lt.pavilonis.monpikas.server.utils.Messages.label;

public class PupilTypeComboBox extends ComboBox {

   private final static List<PupilType> types = asList(PupilType.values());

   public PupilTypeComboBox() {
      setContainerDataSource(new BeanItemContainer<>(PupilType.class, types));
      setValue(types.get(0));
      setCaption(label("PupilTypeComboBox.caption"));
   }

   @Override
   public String getItemCaption(Object itemId) {
      return label("PupilType." + itemId);
   }

   @Override
   public boolean isNullSelectionAllowed() {
      return false;
   }

}
