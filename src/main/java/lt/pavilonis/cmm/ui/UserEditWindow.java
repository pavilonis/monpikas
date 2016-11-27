package lt.pavilonis.cmm.ui;

import com.google.common.io.BaseEncoding;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import lt.pavilonis.cmm.UserRestRepository;
import lt.pavilonis.cmm.representation.UserRepresentation;
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
      setWidth("600px");
      setHeight("585px");

      sheet.setHeight(100.0f, Unit.PERCENTAGE);
      sheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
      sheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

      setModal(true);
      setContent(new MVerticalLayout(
            sheet,
            new MHorizontalLayout(
                  saveButton,
                  new MButton(FontAwesome.REMOVE, "Close", (click) -> close())
            ).withSpacing(true).withMargin(new MarginInfo(true, false, false, false))
      ));
   }

   public void edit(UserRepresentation user) {

      boolean persistent = user.getCardCode() != null;
      sheet.removeAllComponents();
      if (persistent) {
         // Edit existing
         Consumer<UserRepresentation> updateAction = model -> {
            userRepository.update(model);
            postSaveUpdateAction.run();
         };
         user = userRepository.load(user.getCardCode());
         sheet.addTab(new UserWorkTimeTable(userRepository, user.getCardCode()), "Work Hours");
         sheet.addTab(new UserDetailsForm(user, updateAction), "Edit Details");
      } else {
         // Create new
         UserDetailsForm editForm = new UserDetailsForm(user, saveAction);
         saveButton.addClickListener((click) -> {
            //TODO Move to separate method to reuse in update
            if (binding.isValid()) {
               byte[] bytes = uploadReceiver.getScaledImageBytes();
               if (bytes != null && bytes.length > 0) {
                  this.model.setBase16photo(BaseEncoding.base16().encode(bytes));
               }
               saveAction.accept(this.model);
               closeWindow.run();
               Notification.show("Saved", Notification.Type.HUMANIZED_MESSAGE);
            } else {
               Notification.show("Incorrectly filled fields", Notification.Type.WARNING_MESSAGE);
            }
         });
         Consumer<UserRepresentation> saveAction = model -> {
            userRepository.save(model);
            postSaveUpdateAction.run();
         };
         sheet.addTab(editForm);
      }

      VaadinUI.getCurrent().addWindow(this);
   }

   public void addSaveOrUpdateListener(Runnable runnable) {
      postSaveUpdateAction = runnable;
   }
}
