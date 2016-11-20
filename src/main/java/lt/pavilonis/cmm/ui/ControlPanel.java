package lt.pavilonis.cmm.ui;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.UserRestRepository;
import lt.pavilonis.cmm.representation.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MMarginInfo;

@SpringComponent
@UIScope
public class ControlPanel extends MHorizontalLayout {

   @Autowired
   public ControlPanel(UserTable table, UserRestRepository userRepository, UserEditPopup editPopup) {

      TextField textField = new MTextField();

      MHorizontalLayout filterLayout = new MHorizontalLayout(
            new MButton(FontAwesome.PLUS, "Add", event -> editPopup.edit(new UserRepresentation())),
            new MButton(FontAwesome.REMOVE, "Delete", event -> {
               if (table.getValue() != null) {
                  userRepository.delete(table.getValue().getCardCode());
                  Notification.show("Deleted", Notification.Type.TRAY_NOTIFICATION);
               }
            })
      ).withMargin(new MMarginInfo(false, false, false, true));

      addComponents(
            new MHorizontalLayout(
                  textField,
                  new MButton(FontAwesome.FILTER, "Filter", event -> table.setBeans(userRepository.loadAll()))
            ),
            filterLayout
      );

      withAlign(filterLayout, Alignment.MIDDLE_RIGHT);
      withFullWidth();
   }
}
