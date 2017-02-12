package lt.pavilonis.cmm.ui.user.form;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.domain.UserRepresentation;
import lt.pavilonis.cmm.repository.UserRestRepository;
import lt.pavilonis.cmm.ui.VaadinUI;
import lt.pavilonis.cmm.users.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.function.Consumer;

@SpringComponent
@UIScope
public class UserEditWindow extends MWindow {

   private final Button saveButton;
   private final TabSheet sheet = new TabSheet();
   private Runnable postSaveUpdateAction = () -> {/**/};
   private MessageSourceAdapter messages;

   @Autowired
   private UserRestRepository userRepository;

   @Autowired
   private ImageService imageService;

   @Autowired
   public UserEditWindow(MessageSourceAdapter messages) {
      this.messages = messages;
      setCaption(messages.get(this, "title"));
      saveButton = new Button(messages.get(this, "save"), FontAwesome.CHECK);
      setResizable(false);

      withSize(MSize.size(650, Unit.PIXELS, 590, Unit.PIXELS));

      sheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
      sheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

      setModal(true);
      setContent(new MVerticalLayout(
            sheet,
            new MHorizontalLayout(
                  saveButton,
                  new MButton(FontAwesome.REMOVE,
                        messages.get(this, "close"),
                        (click) -> close())
            ).withSpacing(true)
      ));
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

      sheet.addTab(
            new UserEditWindowPresenceTimeTabTable(userRepository, user.getCardCode()),
            messages.get(this, "hoursOfPresence")
      );

      sheet.addTab(
            new UserEditWindowDetailsTab(
                  user,
                  imageService.imageResource(user.getBase16photo()),
                  updateAction,
                  saveButton,
                  messages
            ),
            messages.get(this, "editDetails")
      );
      VaadinUI.getCurrent().addWindow(this);
   }
}
