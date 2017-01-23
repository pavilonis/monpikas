package lt.pavilonis.cmm.canteen.views.user;

import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.repository.MealRepository;
import lt.pavilonis.cmm.canteen.views.component.EnumComboBox;
import lt.pavilonis.cmm.canteen.views.setting.MealFilter;
import lt.pavilonis.cmm.canteen.views.setting.TableControlPanel;
import lt.pavilonis.cmm.common.FormView;
import lt.pavilonis.cmm.users.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MGridLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@UIScope
@SpringComponent
public class UserMealFormView extends FormView<UserMeal> {

   private final MTextField name = new MTextField();
   private final MTextField birthDate = new MTextField();
   private final MCssLayout photoLayout = new MCssLayout();
   private final MTextArea comment = new MTextArea().withNullRepresentation("").withRows(2);
   private final EnumComboBox<PupilType> type = new EnumComboBox<>(PupilType.class).withRequired(true);
   private final MealTable mealTable;

   @Autowired
   private ImageService imageService;

   @Autowired
   public UserMealFormView(MealRepository mealRepository, MessageSourceAdapter messages) {

      name.setCaption(messages.get(this, "name"));
      birthDate.setCaption(messages.get(this, "birthDate"));

      //TODO make this as many to many custom field
      mealTable = new MealTable(messages);
      comment.setCaption(messages.get(this, "comment"));

      withSize(MSize.size("774px", "460px"));
      mealTable.withSize(MSize.size("512px", "250px"));

      Stream.of(name, birthDate, type)
            .forEach(field -> field.setWidth("250px"));

      Stream.of(name, birthDate)
            .forEach(field -> field.setEnabled(false));

      Consumer<Meal> mealSelectionConsumer = meal -> {
         if (!mealTable.containsId(meal)) {
            mealTable.addBeans(meal);
         }
      };

      TableControlPanel controls = new TableControlPanel(
            click -> new UserMealSelectionPopup(
                  mealRepository.loadAll(new MealFilter()),
                  mealSelectionConsumer,
                  messages
            ),
            click -> {
               Meal selected = mealTable.getValue();
               mealTable.getContainerDataSource().removeItem(selected);
            }
      );

      MGridLayout grid = new MGridLayout(3, 3)
            .withMargin(false)
            .withDefaultComponentAlignment(Alignment.TOP_LEFT);
      grid.add(name, birthDate, type);
      grid.addComponent(new MVerticalLayout(mealTable, controls).withMargin(false), 0, 1, 1, 1);
      grid.addComponent(photoLayout, 2, 1);
      grid.addComponent(comment, 0, 2, 2, 2);

      add(grid);
   }

   @Override
   public void manualBinding(MBeanFieldGroup<UserMeal> binding) {
      binding.bind(type, "mealData.type");
      binding.bind(comment, "mealData.comment");
   }

   @Override
   public void initCustomFieldValues(UserMeal entity) {
      name.setValue(entity.getUser().getName());
      birthDate.setValue(entity.getUser().getBirthDate());
      mealTable.removeAllItems();
      mealTable.addBeans(entity.getMealData().getMeals());
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
      return (List<Meal>) mealTable.getItemIds();
   }
}
