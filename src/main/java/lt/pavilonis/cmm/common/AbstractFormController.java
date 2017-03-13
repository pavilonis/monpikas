package lt.pavilonis.cmm.common;

import com.vaadin.data.Binder;
import com.vaadin.data.Validator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.UserError;
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
   private T model;
   private Window window;

   @Autowired
   protected MessageSourceAdapter messages;

   public AbstractFormController(Class<T> clazz) {
      this.clazz = clazz;
   }

   protected T actionSave() {
      beforeSave(model);

      if (!binder.isValid()) {
         //TODO not visible!
         window.setComponentError(new UserError("Invalid field values"));
//         Notification.show("Invalid field values", Notification.Type.WARNING_MESSAGE);
//         throw new Validator.InvalidValueException("Invalid field values");
      }

      EntityRepository<T, ID, ?> entityRepository = getEntityRepository();
      return entityRepository.saveOrUpdate(model);
   }

   protected void beforeSave(T model) {/*hook*/}

   protected void actionClose() {
      window.close();
   }

   private Component createControlLayout(Consumer<T> persistedEntityConsumer) {
      return new HorizontalLayout(
            new AButton(AbstractFormController.class.getSimpleName() + ".buttonSave")
                  .withIcon(VaadinIcons.CHECK)
                  .withClickListener(click -> {
                           T entity = actionSave();
                           persistedEntityConsumer.accept(entity);
                           actionClose();
                           Notification.show(
                                 getMessageSource().get(AbstractFormController.class, "saved"),
                                 Type.TRAY_NOTIFICATION
                           );
                        }
                  ),
            new AButton(AbstractFormController.class.getSimpleName() + ".buttonClose")
                  .withIcon(VaadinIcons.FILE_REMOVE)
                  .withClickListener(click -> actionClose())
      );
   }

   protected void edit(T entity, ListGrid<T> listGrid) {

      ID id = entity.getId();
      T editEntity;
      if (id == null) {
         editEntity = entity;
      } else {
         editEntity = getEntityRepository().load(id)
               .orElseThrow(() -> new RuntimeException("Could not load entity selected for edition: "
                     + entity.getClass().getSimpleName() + ". id: " + id));
      }

      Consumer<T> persistedEntityConsumer = persistedEntity -> {
         if (entity.getId() != null) {
            listGrid.removeItem(entity);
         }
         listGrid.addItem(persistedEntity);
//         listGrid.sort();
         listGrid.select(persistedEntity);
      };

      FormView<T> formView = createFormView();
      Component controlLayout = createControlLayout(persistedEntityConsumer);

      window = new Window(
            formView.getFormCaption(),
            new VerticalLayout(formView, controlLayout)
      );


      model = editEntity;
      binder = new Binder<>(clazz);
      binder.setBean(model);
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
