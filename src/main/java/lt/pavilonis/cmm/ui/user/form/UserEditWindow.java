package lt.pavilonis.cmm.ui.user.form;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.field.AButton;
import lt.pavilonis.cmm.domain.UserRepresentation;
import lt.pavilonis.cmm.repository.UserRestRepository;
import lt.pavilonis.cmm.ui.VaadinUI;
import lt.pavilonis.cmm.users.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Consumer;

@SpringComponent
@UIScope
public class UserEditWindow extends Window {

   private final Button saveButton = new AButton(this, "save").withIcon(VaadinIcons.CHECK);
   private final TabSheet sheet = new TabSheet();
   private Runnable postSaveUpdateAction = () -> {/**/};

   @Autowired
   private UserRestRepository userRepository;

   @Autowired
   private ImageService imageService;

   @Autowired
   public UserEditWindow() {
      setCaption(App.translate(this, "title"));
      setResizable(false);

      setWidth(650, Unit.PIXELS);
      setHeight(590, Unit.PIXELS);

      sheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
      sheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

      setModal(true);
      HorizontalLayout controls = new HorizontalLayout(
            saveButton,
            new AButton(this.getClass(), "close")
                  .withIcon(VaadinIcons.FILE_REMOVE)
                  .withClickListener(click -> close())
      );
      controls.setSpacing(true);
      setContent(new VerticalLayout(sheet, controls));
   }

   public void edit(UserRepresentation user) {

      sheet.removeAllComponents();

      Consumer<UserRepresentation> updateAction = model -> {
         userRepository.update(model);
         close();
         postSaveUpdateAction.run();
      };

      user = userRepository.load(user.getCardCode())
            .orElseThrow(() -> new RuntimeException("User not found"));

      UserEditWindowPresenceTimeTabGrid tab1 =
            new UserEditWindowPresenceTimeTabGrid(userRepository, user.getCardCode());
      sheet.addTab(tab1, App.translate(this, "hoursOfPresence"));

      Resource image = imageService.imageResource(user.getBase16photo());
      UserEditWindowDetailsTab tab = new UserEditWindowDetailsTab(user, image, updateAction, saveButton);
      sheet.addTab(tab, App.translate(this, "editDetails"));

      VaadinUI.getCurrent().addWindow(this);
   }
}
