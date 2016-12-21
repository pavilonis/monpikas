package lt.pavilonis.cmm.ui.key;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.repository.KeyRestRepository;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.repository.ScannerRestRepository;
import lt.pavilonis.cmm.domain.KeyRepresentation;
import lt.pavilonis.cmm.domain.ScannerRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.List;

@SpringComponent
@UIScope
public class KeyListFilterPanel extends MHorizontalLayout {

   @Autowired
   public KeyListFilterPanel(KeyTable table, KeyRestRepository keyRepository,
                             ScannerRestRepository scanners, MessageSourceAdapter messages) {

      TextField textField = new MTextField(messages.get(this, "keyNumber"));

      ComboBox scannerCombo = new ComboBox(messages.get(this, "scanner"), scanners.loadAll());
      scannerCombo.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
      scannerCombo.setItemCaptionPropertyId("name");

      Runnable containerUpdate = () -> {
         List<KeyRepresentation> beans = keyRepository.load(
               scannerCombo.getValue() == null ? null : ((ScannerRepresentation)scannerCombo.getValue()).getId(),
               textField.getValue()
         );
         table.setBeans(beans);
         table.sort();
      };

      Runnable resetFields = () -> {
         scannerCombo.setValue(null);
         textField.setValue(null);
         containerUpdate.run();
      };

      addComponents(
            textField,
            scannerCombo,
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
   }
}
