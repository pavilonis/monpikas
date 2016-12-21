package lt.pavilonis.cmm.ui.user.form;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.repository.UserRestRepository;
import lt.pavilonis.cmm.domain.UserRepresentation;
import lt.pavilonis.cmm.ui.VaadinUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.function.Consumer;

@SpringComponent
@UIScope
public class UserEditWindow extends Window {

   private final Button saveButton;
   private final TabSheet sheet = new TabSheet();
   private Runnable postSaveUpdateAction = () -> {/**/};
   private MessageSourceAdapter messages;

   @Autowired
   private UserRestRepository userRepository;

   @Autowired
   public UserEditWindow(MessageSourceAdapter messages) {
      this.messages = messages;
      setCaption(messages.get(this, "title"));
      saveButton = new Button(messages.get(this, "save"), FontAwesome.CHECK);
      setResizable(false);
      setWidth("650px");
      setHeight("650px");

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

//      boolean persistent = user.getCardCode() != null;
      sheet.removeAllComponents();
//      if (persistent) {
      // Edit existing
      Consumer<UserRepresentation> updateAction = model -> {
         userRepository.update(model);
         close();
         postSaveUpdateAction.run();
      };
      user = userRepository.load(user.getCardCode());
      sheet.addTab(
            new UserEditWindowPresenceTimeTabTable(userRepository, user.getCardCode(), messages),
            messages.get(this, "hoursOfPresence")
      );
      sheet.addTab(
            new UserEditWindowDetailsTab(user, updateAction, saveButton, messages),
            messages.get(this, "editDetails")
      );
//      } else {
//         // Create new
//         Consumer<UserRepresentation> saveAction = model -> {
//            userRepository.save(model);
//            close();
//            postSaveUpdateAction.run();
//         };
//         UserEditWindowDetailsTab editForm = new UserEditWindowDetailsTab(user, saveAction, saveButton);
//         sheet.addTab(editForm);
//      }
      VaadinUI.getCurrent().addWindow(this);
   }

   public void addSaveOrUpdateListener(Runnable runnable) {
      postSaveUpdateAction = runnable;
   }
}
