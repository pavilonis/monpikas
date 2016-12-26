package lt.pavilonis.cmm.ui.user;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.domain.UserRepresentation;
import lt.pavilonis.cmm.repository.UserRestRepository;
import lt.pavilonis.cmm.ui.user.form.UserEditWindow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.ListContainer;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
public class UserListFilterPanel extends MHorizontalLayout {

   @Autowired
   public UserListFilterPanel(UserTable table, UserRestRepository userRepository,
                              UserEditWindow editPopup, MessageSourceAdapter messages) {

      TextField textField = new MTextField(messages.get(this, "firstLastName"));

      ListContainer container = (ListContainer) table.getContainerDataSource();
      @SuppressWarnings("unchecked")
      List<UserRepresentation> userData = (List<UserRepresentation>) container.getItemIds();

//      MButton deleteButton = new MButton(
//            FontAwesome.REMOVE,
//            messages.get(this, "delete"),
//            event -> {
//               if (table.getValue() != null) {
//                  userRepository.delete(table.getValue().getCardCode());
//                  Notification.show(messages.get(this, "deleted"), Notification.Type.TRAY_NOTIFICATION);
//               }
//            }
//      );
//      deleteButton.setEnabled(false);
//      MHorizontalLayout controlsLayout = new MHorizontalLayout(deleteButton)
//            .alignAll(Alignment.BOTTOM_RIGHT);

      ComboBox groupCombo = propertyListCombo(
            messages.get(this, "group"),
            userData,
            UserRepresentation::getGroup
      );
      ComboBox roleCombo = propertyListCombo(
            messages.get(this, "role"),
            userData,
            UserRepresentation::getRole
      );

      Runnable containerUpdate = () -> {
         List<UserRepresentation> beans = userRepository
               .loadAll(textField.getValue(), (String) roleCombo.getValue(), (String) groupCombo.getValue());
         table.setBeans(beans);
         table.sort();
      };

      Runnable resetFields = () -> {
         roleCombo.setValue(null);
         groupCombo.setValue(null);
         textField.setValue(null);
         containerUpdate.run();
      };

      addComponents(
            textField,
            roleCombo,
            groupCombo,
            new MButton(
                  FontAwesome.FILTER,
                  messages.get(this, "filter"),
                  event -> containerUpdate.run()
            ).withClickShortcut(KeyCode.ENTER),
            new MButton(
                  FontAwesome.REFRESH,
                  messages.get(this, "reset"),
                  event -> resetFields.run()
            ).withClickShortcut(KeyCode.ESCAPE)
      );
      alignAll(Alignment.BOTTOM_LEFT);
      editPopup.addSaveOrUpdateListener(containerUpdate);
   }

   private ComboBox propertyListCombo(String caption, List<UserRepresentation> userData,
                                      Function<UserRepresentation, String> propertyExtractor) {
      Set<String> items = userData
            .stream()
            .map(propertyExtractor)
            .filter(StringUtils::isNotBlank)
            .map(StringUtils::strip)
            .map(String::toLowerCase)
            .map(StringUtils::capitalize)
            .sorted()
            .collect(Collectors.toSet());

      return new ComboBox(caption, items);
   }
}
