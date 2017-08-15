package lt.pavilonis.cmm.common;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.field.AButton;
import lt.pavilonis.cmm.common.service.MessageSourceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractFormController<T extends Identified<ID>, ID> {

   private final Logger LOG = LoggerFactory.getLogger(AbstractFormController.class.getSimpleName());
   private final Class<T> clazz;
   private Binder<T> binder;
   protected T model;
   private Window window;

   @Autowired
   protected MessageSourceAdapter messages;

   public AbstractFormController(Class<T> clazz) {
      this.clazz = clazz;
   }

   protected Optional<T> actionSave(FieldLayout<T> fieldLayout) {

      EntityRepository<T, ID, ?> entityRepository = getEntityRepository();

      try {

         binder.writeBean(model);

//         beforeSave(model);

         T persistedItem = entityRepository.saveOrUpdate(model);
         return Optional.of(persistedItem);

      } catch (ValidationException e) {

         String errorMessage = e.getValidationErrors().stream()
               .map(ValidationResult::getErrorMessage)
               .findFirst()
               .orElse("Unknown error");

         Notification.show(errorMessage, Type.WARNING_MESSAGE);
//         fieldLayout.displayErrorMessage(errorMessage);
         return Optional.empty();
      }
   }

//   protected void beforeSave(T model) {/*hook*/}

   protected void actionClose() {
      window.close();
   }

   private Component createControlLayout(Consumer<T> persistedItemConsumer, FieldLayout<T> fieldLayout, boolean readOnly) {

      AButton buttonSave = new AButton(AbstractFormController.class, "buttonSave");

      if (readOnly) {
         buttonSave.setEnabled(false);

      } else {
         buttonSave = buttonSave
               .withIcon(VaadinIcons.CHECK)
               .withClickListener(click -> {
                  Optional<T> entity = actionSave(fieldLayout);
                  entity.ifPresent(persistedItem -> {
                     persistedItemConsumer.accept(persistedItem);
                     actionClose();
                     Notification.show(
                           App.translate(AbstractFormController.class, "saved"),
                           Type.TRAY_NOTIFICATION
                     );
                  });
               });
      }

      AButton buttonCancel = new AButton(AbstractFormController.class, "buttonClose")
            .withIcon(VaadinIcons.CLOSE)
            .withClickListener(click -> actionClose());

      return new HorizontalLayout(buttonSave, buttonCancel);
   }

   protected void edit(T itemToEdit, ListGrid<T> listGrid, boolean readOnly) {

      model = itemToEdit.getId() == null
            ? itemToEdit
            : loadExisting(itemToEdit);

      Consumer<T> persistedItemConsumer = updatedItem -> listGrid.addOrUpdate(itemToEdit, updatedItem);

      binder = new BeanValidationBinder<>(clazz);

      FieldLayout<T> fieldLayout = createFieldLayout();

      Component controlLayout = createControlLayout(persistedItemConsumer, fieldLayout, readOnly);

      window = createWindow(fieldLayout, controlLayout);

      fieldLayout.manualBinding(binder);
      try {
         binder.bindInstanceFields(fieldLayout);
      } catch (IllegalStateException e) {
         LOG.warn(e.getMessage());
      }

      binder.readBean(model);
      fieldLayout.initCustomFieldValues(model);

      getValidators().forEach(binder::withValidator);

      customizeWindow(window);

      window.center();
      window.setModal(true);
      UI.getCurrent().addWindow(window);
   }

   protected Window createWindow(FieldLayout<T> fieldLayout, Component controlLayout) {
      String formCaption = fieldLayout.getFormCaption(clazz);
      VerticalLayout layout = new VerticalLayout(fieldLayout, controlLayout);
      return new Window(formCaption, layout);
   }

   private T loadExisting(T itemToEdit) {
      return getEntityRepository().find(itemToEdit.getId())
            .orElseThrow(() -> new RuntimeException("Could not load entity selected for edition: "
                  + itemToEdit.getClass().getSimpleName() + ". id: " + itemToEdit.getId()));
   }

   protected Collection<Validator<T>> getValidators() {
      return Collections.emptyList();
   }

   protected void customizeWindow(Window window) {/*hook*/}

   protected abstract EntityRepository<T, ID, ?> getEntityRepository();

   protected abstract FieldLayout<T> createFieldLayout();
}
