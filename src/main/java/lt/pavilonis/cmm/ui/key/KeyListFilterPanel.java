package lt.pavilonis.cmm.ui.key;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.converter.LocalDateConverter;
import lt.pavilonis.cmm.domain.KeyRepresentation;
import lt.pavilonis.cmm.domain.ScannerRepresentation;
import lt.pavilonis.cmm.repository.KeyRestRepository;
import lt.pavilonis.cmm.repository.ScannerRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@SpringComponent
@UIScope
class KeyListFilterPanel extends MHorizontalLayout {

   private final TextField textField;
   private final ComboBox scannerCombo;
   private final CheckBox activeKeysCheckBox;
   private final DateField periodStart;
   private final DateField periodEnd;

   @Autowired
   private KeyRestRepository keys;

   @Autowired
   private KeyTable table;

   @Autowired
   public KeyListFilterPanel(ScannerRestRepository scanners, MessageSourceAdapter messages) {

      activeKeysCheckBox = new MCheckBox(messages.get(this, "active"), true);
      activeKeysCheckBox.addValueChangeListener(value -> togglePeriodStartEnd());
      activeKeysCheckBox.setImmediate(true);

      periodStart = new MDateField(messages.get(this, "periodStart"));
      periodStart.setDateFormat("yyyy-MM-dd");
      periodStart.setRequired(true);
      periodStart.setConverter(new LocalDateConverter());

      periodEnd = new MDateField(messages.get(this, "periodEnd"));
      periodEnd.setDateFormat("yyyy-MM-dd");
      periodEnd.setRequired(true);
      periodEnd.setConverter(new LocalDateConverter());
      defaultDateValues();
      togglePeriodStartEnd();

      textField = new MTextField(messages.get(this, "keyNumber"));
      scannerCombo = new ComboBox(messages.get(this, "scanner"), scanners.loadAll()) {
         @Override
         public String getItemCaption(Object itemId) {
            ScannerRepresentation scanner = (ScannerRepresentation) itemId;
            return messages.get(scanner, scanner.getName());
         }
      };

      Runnable resetFields = () -> {
         scannerCombo.setValue(null);
         textField.setValue(null);
         defaultDateValues();
         reload();
      };

      addComponents(
            periodStart,
            periodEnd,
            scannerCombo,
            textField,
            activeKeysCheckBox,
            new MButton(
                  FontAwesome.FILTER,
                  messages.get(this, "filter"),
                  event -> {
                     if (!periodStart.isEmpty() && !periodEnd.isEmpty()) {
                        reload();
                     } else {
                        Notification.show(
                              messages.get(this, "emptyFilterDates"),
                              Notification.Type.WARNING_MESSAGE
                        );
                     }
                  }
            ).withClickShortcut(KeyCode.ENTER),
            new MButton(
                  FontAwesome.REFRESH,
                  messages.get(this, "reset"),
                  event -> resetFields.run()
            ).withClickShortcut(KeyCode.ESCAPE)
      );

      alignAll(Alignment.BOTTOM_LEFT);
   }

   private void togglePeriodStartEnd() {
      boolean mode = isLogMode();
      periodStart.setEnabled(mode);
      periodEnd.setEnabled(mode);
   }

   private void defaultDateValues() {
      periodStart.setConvertedValue(LocalDate.now().minusWeeks(1));
      periodEnd.setValue(new Date());
   }

   void reload() {
      List<KeyRepresentation> beans = keys.load(
            scannerCombo.getValue() == null ? null : ((ScannerRepresentation) scannerCombo.getValue()).getId(),
            textField.getValue(),
            isLogMode(),
            (LocalDate) periodStart.getConvertedValue(),
            (LocalDate) periodEnd.getConvertedValue()
      );
      table.setBeans(beans);
   }

   boolean isLogMode() {
      return activeKeysCheckBox.getValue() != Boolean.TRUE;
   }
}
