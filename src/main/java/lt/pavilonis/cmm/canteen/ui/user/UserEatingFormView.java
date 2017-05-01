package lt.pavilonis.cmm.canteen.ui.user;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.Binder;
import com.vaadin.server.Resource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.canteen.domain.Eating;
import lt.pavilonis.cmm.canteen.domain.EatingType;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.domain.UserEating;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.field.EnumComboBox;
import lt.pavilonis.cmm.common.field.OneToManyField;
import lt.pavilonis.cmm.common.service.ImageService;

import java.util.HashSet;
import java.util.stream.Stream;

//TODO add validator to ensure that user has no more than one eating for each type
public class UserEatingFormView extends FieldLayout<UserEating> {
   private final ImageService imageService;
   private final ATextField name = new ATextField(this.getClass(), "name");
   private final ATextField birthDate = new ATextField(this.getClass(), "birthDate");
   private final CssLayout photoLayout = new CssLayout();
   private final TextArea comment = new TextArea(App.translate(this.getClass(), "comment"));
   private final EnumComboBox<PupilType> typeField = new EnumComboBox<>(PupilType.class)
         .withRequired(true);
   private final OneToManyField<Eating> eatingsField = new OneToManyField<>(
         Eating.class,
         ImmutableMap.of(
               "type",
               eating -> App.translate(EatingType.class, eating.getType().name())
         )
   );

   public UserEatingFormView(ImageService imageService) {
      this.imageService = imageService;
      setWidth(852, Unit.PIXELS);
      setHeight(460, Unit.PIXELS);
      eatingsField.setTableWidth(600, Unit.PIXELS);

      name.setWidth(348, Unit.PIXELS);
      Stream.of(birthDate, typeField).forEach(field -> field.setWidth(240, Unit.PIXELS));
      Stream.of(name, birthDate).forEach(field -> field.setEnabled(false));

      HorizontalLayout row1 = new HorizontalLayout(name, birthDate, typeField);
      VerticalLayout photoAndComment = new VerticalLayout(photoLayout, comment);
      photoAndComment.setMargin(false);
      HorizontalLayout row2 = new HorizontalLayout(eatingsField, photoAndComment);

      addComponents(row1, row2);
      setExpandRatio(row2, 1f);
   }

   @Override
   public void manualBinding(Binder<UserEating> binding) {
      binding.bind(
            typeField,
            userEating -> userEating.getEatingData().getType(),
            (userEating, value) -> userEating.getEatingData().setType(value));
      binding.bind(
            comment,
            item -> item.getEatingData().getComment(),
            (item, value) -> item.getEatingData().setComment(value)
      );
      binding.bind(
            eatingsField,
            item -> item.getEatingData().getEatings(),
            (item, value) -> item.getEatingData().setEatings(new HashSet<>(value))
      );
   }

   @Override
   public void initCustomFieldValues(UserEating entity) {
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
