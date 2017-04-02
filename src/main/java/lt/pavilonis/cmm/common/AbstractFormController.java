package lt.pavilonis.cmm.common;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.Validator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.common.field.AButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

public abstract class AbstractFormController<T extends Identifiable<ID>, ID> {

   private final Logger LOG = LoggerFactory.getLogger(AbstractFormController.class);
   private final Class<T> clazz;
   private Binder<T> binder;
   protected T model;
   private Window window;

   @Autowired
   protected MessageSourceAdapter messages;

   public AbstractFormController(Class<T> clazz) {
      this.clazz = clazz;
   }

   protected T actionSave() {
      beforeSave(model);

      EntityRepository<T, ID, ?> entityRepository = getEntityRepository();
      try {
         binder.writeBean(model);
         return entityRepository.saveOrUpdate(model);
      } catch (ValidationException e) {
         e.printStackTrace();
         //TODO not visible!
         window.setComponentError(new UserError("Invalid field values"));
//         Notification.show("Invalid field values", Notification.Type.WARNING_MESSAGE);
         return null;
      }
   }

   protected void beforeSave(T model) {/*hook*/}

   protected void actionClose() {
      window.close();
   }

   private Component createControlLayout(Consumer<T> persistedItemConsumer) {
      Button.ClickListener buttonSaveListener = click -> {
         T entity = actionSave();
         persistedItemConsumer.accept(entity);
         actionClose();
         Notification.show(
               App.translate(AbstractFormController.class, "saved"),
               Type.TRAY_NOTIFICATION
         );
      };

      AButton buttonSave = new AButton(AbstractFormController.class.getSimpleName() + ".buttonSave")
            .withIcon(VaadinIcons.CHECK)
            .withClickListener(buttonSaveListener);

      AButton buttonCancel = new AButton(AbstractFormController.class.getSimpleName() + ".buttonClose")
            .withIcon(VaadinIcons.FILE_REMOVE)
            .withClickListener(click -> actionClose());

      return new HorizontalLayout(buttonSave, buttonCancel);
   }

   protected void edit(T itemToEdit, ListGrid<T> listGrid) {

      model = itemToEdit.getId() == null
            ? itemToEdit
            : loadExisting(itemToEdit);

      Consumer<T> persistedItemConsumer = updatedItem -> listGrid.addOrUpdate(itemToEdit, updatedItem);

      binder = new Binder<>(clazz);
      binder.setBean(model);

      FormView<T> formView = createFormView();
      Component controlLayout = createControlLayout(persistedItemConsumer);
      VerticalLayout layout = new VerticalLayout(formView, controlLayout);

      window = new Window(formView.getFormCaption(), layout);
      try {
         binder.bindInstanceFields(formView);
      } catch (IllegalStateException e) {
         LOG.warn(e.getMessage());
      }

      formView.manualBinding(binder);
      formView.initCustomFieldValues(model);

      customizeWindow(window);

//      getValidators()
//            .forEach(binder::addValidator);

      window.center();
      window.setModal(true);
      UI.getCurrent().addWindow(window);
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

   protected abstract FormView<T> createFormView();
}
