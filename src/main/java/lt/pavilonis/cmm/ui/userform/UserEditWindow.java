package lt.pavilonis.cmm.ui.userform;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import lt.pavilonis.cmm.UserRestRepository;
import lt.pavilonis.cmm.representation.UserRepresentation;
import lt.pavilonis.cmm.ui.VaadinUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.function.Consumer;

@SpringComponent
@UIScope
public class UserEditWindow extends Window {

   private final Button saveButton = new Button("Save", FontAwesome.CHECK);
   private final TabSheet sheet = new TabSheet();
   private Runnable postSaveUpdateAction = () -> {/**/};

   @Autowired
   private UserRestRepository userRepository;

   public UserEditWindow() {
      setCaption("User settings");
      setResizable(false);
      setWidth("650px");
      setHeight("640px");

//      sheet.setHeight(100.0f, Unit.PERCENTAGE);
      sheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
      sheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

      setModal(true);
      setContent(new MVerticalLayout(
            sheet,
            new MHorizontalLayout(
                  saveButton,
                  new MButton(FontAwesome.REMOVE, "Close", (click) -> close())
            ).withSpacing(true)
      ));
   }

   public void edit(UserRepresentation user) {

      boolean persistent = user.getCardCode() != null;
      sheet.removeAllComponents();
      if (persistent) {
         // Edit existing
         Consumer<UserRepresentation> updateAction = model -> {
            userRepository.update(model);
            close();
            postSaveUpdateAction.run();
         };
         user = userRepository.load(user.getCardCode());
         sheet.addTab(new UserEditWindowWorkTimeTabTable(userRepository, user.getCardCode()), "Work Hours");
         sheet.addTab(new UserEditWindowDetailsTab(user, updateAction, saveButton), "Edit Details");
      } else {
         // Create new
         Consumer<UserRepresentation> saveAction = model -> {
            userRepository.save(model);
            close();
            postSaveUpdateAction.run();
         };
         UserEditWindowDetailsTab editForm = new UserEditWindowDetailsTab(user, saveAction, saveButton);
         sheet.addTab(editForm);
      }
      VaadinUI.getCurrent().addWindow(this);
   }

   public void addSaveOrUpdateListener(Runnable runnable) {
      postSaveUpdateAction = runnable;
   }
}
