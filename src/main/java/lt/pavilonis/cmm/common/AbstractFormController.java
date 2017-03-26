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
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.common.field.AButton;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

public abstract class AbstractFormController<T extends Identifiable<ID>, ID> {

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

   private Component createControlLayout(Consumer<T> persistedEntityConsumer) {
      Button.ClickListener buttonSaveListener = click -> {
         T entity = actionSave();
         persistedEntityConsumer.accept(entity);
         actionClose();
         String message = getMessageSource().get(AbstractFormController.class, "saved");
         Notification.show(message, Type.TRAY_NOTIFICATION);
      };

      AButton buttonSave = new AButton(AbstractFormController.class.getSimpleName() + ".buttonSave")
            .withIcon(VaadinIcons.CHECK)
            .withClickListener(buttonSaveListener);

      AButton buttonCancel = new AButton(AbstractFormController.class.getSimpleName() + ".buttonClose")
            .withIcon(VaadinIcons.FILE_REMOVE)
            .withClickListener(click -> actionClose());

      return new HorizontalLayout(buttonSave, buttonCancel);
   }

   protected void edit(T listItem, ListGrid<T> listGrid) {

      ID id = listItem.getId();
      T editEntity;
      if (id == null) {
         editEntity = listItem;
      } else {
         editEntity = getEntityRepository().load(id)
               .orElseThrow(() -> new RuntimeException("Could not load entity selected for edition: "
                     + listItem.getClass().getSimpleName() + ". id: " + id));
      }

      Consumer<T> persistedEntityConsumer = persistedEntity -> {
         if (listItem.getId() != null) {
            listGrid.removeItem(listItem);
         }
         listGrid.addItem(persistedEntity);
//         listGrid.sort();
         listGrid.select(persistedEntity);
      };

      model = editEntity;
      binder = new Binder<>(clazz);
      binder.setBean(model);

      FormView<T> formView = createFormView();
      Component controlLayout = createControlLayout(persistedEntityConsumer);
      VerticalLayout layout = new VerticalLayout(formView, controlLayout);

      window = new Window(formView.getFormCaption(), layout);
      binder.bindInstanceFields(formView);

      formView.manualBinding(binder);
      formView.initCustomFieldValues(model);

      customizeWindow(window);

//      getValidators()
//            .forEach(binder::addValidator);

      window.center();
      window.setModal(true);
      UI.getCurrent().addWindow(window);
   }


   protected MessageSourceAdapter getMessageSource() {
      return messages;
   }

   protected Collection<Validator<T>> getValidators() {
      return Collections.emptyList();
   }

   protected void customizeWindow(Window window) {/*hook*/}

   protected abstract EntityRepository<T, ID, ?> getEntityRepository();

   protected abstract FormView<T> createFormView();
}
