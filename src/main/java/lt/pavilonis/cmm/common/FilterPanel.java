package lt.pavilonis.cmm.common;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.List;

public abstract class FilterPanel<FILTER> extends MHorizontalLayout {

   protected final MessageSourceAdapter messages = App.context.getBean(MessageSourceAdapter.class);
   private final List<Field> fields = getFields();

   private final MButton buttonFilter = new MButton(messages.get(FilterPanel.class, "filter"))
         .withIcon(FontAwesome.FILTER)
         .withClickShortcut(ShortcutAction.KeyCode.ENTER);

   private final MButton buttonReset = new MButton(messages.get(FilterPanel.class, "reset"))
         .withIcon(FontAwesome.REFRESH)
         .withClickShortcut(ShortcutAction.KeyCode.ESCAPE);

   public FilterPanel() {
      add(
            getFieldLayout(),
            getButtonLayout()
      );

      alignAll(Alignment.BOTTOM_LEFT);
      setMargin(false);
      setDefaultValues();

      maybeFocus();
   }

   private void maybeFocus() {
      Field field = getFieldToFocus();
      if (field != null) {
         field.focus();
      }
   }

   protected AbstractField<?> getFieldToFocus() {
      return null;
   }

   protected Component getFieldLayout() {
      return new MHorizontalLayout(fields.toArray(new AbstractField[fields.size()]))
            .withMargin(false)
            .alignAll(Alignment.BOTTOM_LEFT);
   }

   protected abstract List<AbstractField<?>> getFields();

   public abstract FILTER getFilter();

   protected MHorizontalLayout getButtonLayout() {
      return new MHorizontalLayout(buttonFilter, buttonReset)
            .withMargin(false);
   }

   protected void fieldReset() {
      fields.forEach(Field::clear);
      setDefaultValues();
   }

   protected void setDefaultValues() {
   }

   public void addSearchClickListener(Button.ClickListener clickListener) {
      buttonFilter.addClickListener(clickListener);
   }

   public void addResetClickListener(Button.ClickListener clickListener) {
      buttonReset.addClickListener(clickListener);
   }
}
