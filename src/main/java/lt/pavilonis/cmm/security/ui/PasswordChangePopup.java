package lt.pavilonis.cmm.security.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.field.AButton;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

public class PasswordChangePopup extends Window {

   private PasswordField passwordField = new PasswordField(App.translate(getClass(), "newPassword"));

   public PasswordChangePopup(Consumer<String> newPasswordConsumer) {
      super(App.translate(PasswordChangePopup.class, "passwordChange"));

      AButton buttonAccept = new AButton(getClass(), "accept")
            .withClickListener(click -> {
               String value = passwordField.getValue();
               if (StringUtils.isBlank(value)) {
                  Notification.show(App.translate(getClass(), "passwordMayNotBeEmpty"));
               } else {
                  newPasswordConsumer.accept(value);
                  close();
                  Notification.show(App.translate("saved"), Notification.Type.TRAY_NOTIFICATION);
               }
            })
            .withIcon(VaadinIcons.CHECK);

      AButton buttonCancel = new AButton(getClass(), "cancel")
            .withClickListener(click -> close())
            .withIcon(VaadinIcons.CLOSE);

      setContent(new VerticalLayout(passwordField, new HorizontalLayout(buttonAccept, buttonCancel)));
      setWidth(300, Unit.PIXELS);

      UI.getCurrent().addWindow(this);
      this.center();
      passwordField.focus();
   }
}
