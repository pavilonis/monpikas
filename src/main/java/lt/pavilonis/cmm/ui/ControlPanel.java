package lt.pavilonis.cmm.ui;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.UserRestRepository;
import lt.pavilonis.cmm.representation.UserRepresentation;
import lt.pavilonis.cmm.ui.userform.UserEditWindow;
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
public class ControlPanel extends MHorizontalLayout {

   @Autowired
   public ControlPanel(UserTable table, UserRestRepository userRepository, UserEditWindow editPopup) {

      TextField textField = new MTextField("First / Last Name");

      ListContainer container = (ListContainer) table.getContainerDataSource();
      @SuppressWarnings("unchecked")
      List<UserRepresentation> userData = (List<UserRepresentation>) container.getItemIds();

      MHorizontalLayout controlsLayout = new MHorizontalLayout(
            new MButton(FontAwesome.PLUS, "Add", event -> editPopup.edit(new UserRepresentation())),
            new MButton(FontAwesome.REMOVE, "Delete", event -> {
               if (table.getValue() != null) {
                  userRepository.delete(table.getValue().getCardCode());
                  Notification.show("Deleted", Notification.Type.TRAY_NOTIFICATION);
               }
            })
      ).alignAll(Alignment.BOTTOM_RIGHT);

      ComboBox groupCombo = propertyListCombo("Group", userData, UserRepresentation::getGroup);
      ComboBox roleCombo = propertyListCombo("Role", userData, UserRepresentation::getRole);

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

      MHorizontalLayout filterLayout = new MHorizontalLayout(
            textField,
            roleCombo,
            groupCombo,
            new MButton(FontAwesome.FILTER, "Filter", event -> containerUpdate.run()).withClickShortcut(KeyCode.ENTER),
            new MButton(FontAwesome.REFRESH, "Reset", event -> resetFields.run()).withClickShortcut(KeyCode.ESCAPE)
      ).alignAll(Alignment.BOTTOM_LEFT);

      addComponents(filterLayout, controlsLayout);

      withAlign(controlsLayout, Alignment.BOTTOM_RIGHT);
      withFullWidth();

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
