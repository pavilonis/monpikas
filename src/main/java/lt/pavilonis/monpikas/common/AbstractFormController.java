package lt.pavilonis.monpikas.common;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.monpikas.App;
import lt.pavilonis.monpikas.common.field.AButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractFormController<T extends Identified<ID>, ID> {

   private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFormController.class);
   private final Class<T> clazz;
   private Binder<T> binder;
   private T model;
   private Window window;

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

      AButton buttonSave = new AButton(AbstractFormController.class, "buttonSave")
            .withIcon(VaadinIcons.CHECK);

      if (readOnly) {
         buttonSave.setEnabled(false);

      } else {
         buttonSave = buttonSave.withClickListener(click -> actionSave(fieldLayout)
               .ifPresent(persistedItem -> {
                  persistedItemConsumer.accept(persistedItem);
                  actionClose();
                  Notification.show(App.translate(AbstractFormController.class, "saved"), Type.TRAY_NOTIFICATION);
               }));
      }

      AButton buttonCancel = new AButton(AbstractFormController.class, "buttonClose")
            .withIcon(VaadinIcons.CLOSE)
            .withClickListener(click -> actionClose());

      return new HorizontalLayout(buttonSave, buttonCancel);
   }

   public void edit(T itemToEdit, ListGrid<T> listGrid, boolean readOnly) {

      model = itemToEdit.getId() == null
            ? itemToEdit
            : loadExisting(itemToEdit);

      Consumer<T> persistedItemConsumer = updatedItem -> updateGridOnSaveUpdate(itemToEdit, listGrid, updatedItem);

      binder = new BeanValidationBinder<>(clazz);

      FieldLayout<T> fieldLayout = createFieldLayout(model);

      Component controlLayout = createControlLayout(persistedItemConsumer, fieldLayout, readOnly);

      window = createWindow(fieldLayout, controlLayout);

      fieldLayout.manualBinding(binder);
      try {
         binder.bindInstanceFields(fieldLayout);
      } catch (IllegalStateException e) {
         LOGGER.warn(e.getMessage());
      }

      binder.readBean(model);
      fieldLayout.initCustomFieldValues(model);

      getValidators().forEach(binder::withValidator);

      customizeWindow(window);

      window.center();
      window.setModal(true);
      UI.getCurrent().addWindow(window);
   }

   protected void updateGridOnSaveUpdate(T itemToEdit, ListGrid<T> listGrid, T updatedItem) {
      listGrid.addOrUpdate(itemToEdit, updatedItem);
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

   protected void customizeWindow(Window window) {
      window.setWidth(999, Sizeable.Unit.PIXELS);
   }

   protected abstract EntityRepository<T, ID, ?> getEntityRepository();

   protected abstract FieldLayout<T> createFieldLayout(T model);
}
