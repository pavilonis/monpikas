package lt.pavilonis.cmm.canteen.views.user;

import com.vaadin.data.Binder;
import com.vaadin.server.Resource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.common.FormView;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.field.EnumComboBox;
import lt.pavilonis.cmm.common.field.OneToManyField;
import lt.pavilonis.cmm.users.service.ImageService;

import java.util.HashSet;
import java.util.stream.Stream;

//TODO add validator to ensure that user has no more than one meal for each type
public class UserMealFormView extends FormView<UserMeal> {
   private final ImageService imageService;
   private final ATextField name = new ATextField(this.getClass(), "name");
   private final ATextField birthDate = new ATextField(this.getClass(), "birthDate");
   private final CssLayout photoLayout = new CssLayout();
   private final TextArea comment = new TextArea(App.translate(this.getClass(), "comment"));
   private final EnumComboBox<PupilType> typeField = new EnumComboBox<>(PupilType.class)
         .withRequired(true);
   private final OneToManyField<Meal> mealsField = new OneToManyField<>(Meal.class);

   public UserMealFormView(ImageService imageService) {
      this.imageService = imageService;
      setWidth(774, Unit.PIXELS);
      setHeight(460, Unit.PIXELS);
      mealsField.setWidth(512, Unit.PIXELS);

      Stream.of(name, birthDate, typeField).forEach(field -> field.setWidth("250px"));
      Stream.of(name, birthDate).forEach(field -> field.setEnabled(false));

      HorizontalLayout row1 = new HorizontalLayout(name, birthDate, typeField);
      VerticalLayout photoAndComment = new VerticalLayout(photoLayout, comment);
      photoAndComment.setMargin(false);
      HorizontalLayout row2 = new HorizontalLayout(mealsField, photoAndComment);

      addComponents(row1, row2);
      setExpandRatio(row2, 1f);
   }

   @Override
   public void manualBinding(Binder<UserMeal> binding) {
      binding.bind(
            typeField,
            userMeal -> userMeal.getMealData().getType(),
            (userMeal, value) -> userMeal.getMealData().setType(value));
      binding.bind(
            comment,
            item -> item.getMealData().getComment(),
            (item, value) -> item.getMealData().setComment(value)
      );
      binding.bind(
            mealsField,
            item -> item.getMealData().getMeals(),
            (item, value) -> item.getMealData().setMeals(new HashSet<>(value))
      );
   }

   @Override
   public void initCustomFieldValues(UserMeal entity) {
      name.setValue(entity.getUser().getName());
      birthDate.setValue(entity.getUser().getBirthDate());
      updateImage(entity.getUser().getBase16photo());
   }

   private void updateImage(String base16String) {
      Resource imageResource = imageService.imageResource(base16String);
      Image image = new Image(null, imageResource);
      image.addStyleName("user-photo");

      photoLayout.removeAllComponents();
      photoLayout.addComponent(image);
   }
}
