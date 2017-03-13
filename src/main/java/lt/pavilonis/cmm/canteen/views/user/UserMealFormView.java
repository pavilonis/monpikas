package lt.pavilonis.cmm.canteen.views.user;

import com.vaadin.data.Binder;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.repository.MealRepository;
import lt.pavilonis.cmm.canteen.views.setting.MealFilter;
import lt.pavilonis.cmm.common.FormView;
import lt.pavilonis.cmm.common.component.GridControlPanel;
import lt.pavilonis.cmm.common.field.ATextArea;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.field.EnumComboBox;
import lt.pavilonis.cmm.users.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

//TODO add validator to ensure that user has no more than one meal for each type
@UIScope
@SpringComponent
public class UserMealFormView extends FormView<UserMeal> {

   private final ATextField name = new ATextField(this.getClass(), "name");
   private final ATextField birthDate = new ATextField(this.getClass(), "birthDate");
   private final CssLayout photoLayout = new CssLayout();
   private final TextArea comment = new ATextArea(this.getClass() + ".comment");
   private final EnumComboBox<PupilType> type = new EnumComboBox<>(PupilType.class).withRequired(true);
   private final MealGrid mealTable;

   @Autowired
   private ImageService imageService;

   @Autowired
   public UserMealFormView(MealRepository mealRepository) {

      //TODO make this as many to many custom field
      mealTable = new MealGrid(Collections.<Meal>emptyList());

      setWidth(774, Unit.PIXELS);
      setHeight(460, Unit.PIXELS);
      mealTable.setWidth(512, Unit.PIXELS);
      mealTable.setHeight(250, Unit.PIXELS);

      Stream.of(name, birthDate, type).forEach(field -> field.setWidth("250px"));
      Stream.of(name, birthDate).forEach(field -> field.setEnabled(false));

      Consumer<Set<Meal>> mealSelectionConsumer = meals -> meals.stream()
            .filter(meal -> !mealTable.hasItem(meal))
            .forEach(mealTable::addItem);

      GridControlPanel controls = new GridControlPanel(
            click -> new UserMealSelectionPopup(
                  mealRepository.loadAll(new MealFilter()),
                  mealSelectionConsumer),
            click -> {
               Set<Meal> selected = mealTable.getSelectedItems();
               selected.forEach(mealTable::removeItem);
            }
      );

      GridLayout grid = new GridLayout(3, 3);

      grid.addComponents(name, birthDate, type);
      grid.addComponent(new VerticalLayout(mealTable, controls), 0, 1, 1, 1);
      grid.addComponent(photoLayout, 2, 1);
      grid.addComponent(comment, 0, 2, 2, 2);
      grid.setDefaultComponentAlignment(Alignment.TOP_LEFT);

      addComponent(grid);
   }

   @Override
   public void manualBinding(Binder<UserMeal> binding) {
      binding.bind(type, "mealData.type");
      binding.bind(comment, "mealData.comment");
   }

   @Override
   public void initCustomFieldValues(UserMeal entity) {
      name.setValue(entity.getUser().getName());
      birthDate.setValue(entity.getUser().getBirthDate());
      mealTable.setItems(entity.getMealData().getMeals());
      mealTable.collapseColumns();
      updateImage(entity.getUser().getBase16photo());
   }

   private void updateImage(String base16String) {
      Resource imageResource = imageService.imageResource(base16String);
      Image image = new Image(null, imageResource);
      image.addStyleName("user-photo");

      photoLayout.removeAllComponents();
      photoLayout.addComponent(image);
   }

   @SuppressWarnings("unchecked")
   List<Meal> getMealTableValue() {
      return (List<Meal>) mealTable.getSelectedItems();
   }
}
