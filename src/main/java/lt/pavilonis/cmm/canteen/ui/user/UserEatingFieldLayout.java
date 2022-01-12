package lt.pavilonis.cmm.canteen.ui.user;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.Binder;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
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
import java.util.Optional;

//TODO add validator to ensure that user has no more than one eating for each type
public class UserEatingFieldLayout extends FieldLayout<UserEating> {
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

   public UserEatingFieldLayout(ImageService imageService) {
      this.imageService = imageService;
      eatingsField.setTableWidth(710, Unit.PIXELS);
      comment.setWidth(250, Unit.PIXELS);
      comment.setHeight(179, Unit.PIXELS);
      name.setWidth(286, Unit.PIXELS);

      birthDate.setWidth(200, Unit.PIXELS);
      typeField.setWidth(200, Unit.PIXELS);
      name.setEnabled(false);
      birthDate.setEnabled(false);

      VerticalLayout rightSide = new VerticalLayout(photoLayout, comment);
      rightSide.setComponentAlignment(photoLayout, Alignment.MIDDLE_CENTER);
      rightSide.setMargin(false);

      VerticalLayout leftSide = new VerticalLayout(
            new HorizontalLayout(name, birthDate, typeField),
            eatingsField
      );
      leftSide.setMargin(false);

      HorizontalLayout layout = new HorizontalLayout(leftSide, rightSide);
      layout.setMargin(false);

      addComponents(layout);
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
      Optional.ofNullable(entity.getUser().getName())
            .ifPresent(name::setValue);

      Optional.ofNullable(entity.getUser().getBirthDate())
            .ifPresent(birthDate::setValue);

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
