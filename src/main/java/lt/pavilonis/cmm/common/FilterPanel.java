package lt.pavilonis.cmm.common;

import com.vaadin.data.HasValue;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.common.field.AButton;

import java.util.List;

public abstract class FilterPanel<FILTER> extends HorizontalLayout {

   protected final MessageSourceAdapter messages = App.context.getBean(MessageSourceAdapter.class);
   private final List<HasValue<?>> fields = getFields();

   private final AButton buttonFilter = new AButton(FilterPanel.class.getSimpleName() + ".filter")
         .withIcon(VaadinIcons.FILTER)
         .withClickShortcut(ShortcutAction.KeyCode.ENTER);

   private final AButton buttonReset = new AButton(FilterPanel.class.getSimpleName() + ".reset")
         .withIcon(VaadinIcons.REFRESH)
         .withClickShortcut(ShortcutAction.KeyCode.ESCAPE);

   public FilterPanel() {
      setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);

      Component fields = getFieldLayout();
      HorizontalLayout controlButtons = getButtonLayout();
      addComponents(fields, controlButtons);

      setDefaultValues();
      maybeFocus();
   }

   private void maybeFocus() {
      AbstractField<?> field = getFieldToFocus();
      if (field != null) {
         field.focus();
      }
   }

   protected AbstractField<?> getFieldToFocus() {
      return null;
   }

   protected Component getFieldLayout() {
      HorizontalLayout layout = new HorizontalLayout();
      layout.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);

      fields.forEach(field -> layout.addComponent((Component) field));
      return layout;
   }

   protected abstract List<HasValue<?>> getFields();

   public abstract FILTER getFilter();

   protected HorizontalLayout getButtonLayout() {
      HorizontalLayout layout = new HorizontalLayout(buttonFilter, buttonReset);
      layout.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
      return layout;
   }

   protected void fieldReset() {
      fields.forEach(HasValue::clear);
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
