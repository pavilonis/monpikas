package lt.pavilonis.cmm.common;

import com.vaadin.data.Validator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.UserError;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.BeanBinder;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;


public abstract class AbstractFormController<T extends Identifiable<ID>, ID> {

   private T model;
   private MBeanFieldGroup<T> binding;
   private Window window;

   @Autowired
   protected MessageSourceAdapter messages;

   protected T actionSave() {
      beforeSave(model);

      if (!binding.isValid()) {
         //TODO not visible!
         window.setComponentError(new UserError("Invalid field values"));
//         Notification.show("Invalid field values", Notification.Type.WARNING_MESSAGE);
         throw new Validator.InvalidValueException("Invalid field values");
      }

      EntityRepository<T, ID> entityRepository = getEntityRepository();
      return entityRepository.saveOrUpdate(model);
   }

   protected void beforeSave(T model) {/*hook*/}

   protected void actionClose() {
      window.close();
   }

   private Component createControlLayout(Consumer<T> persistedEntityConsumer) {
      return new MHorizontalLayout(
            new MButton(
                  FontAwesome.CHECK,
                  getMessageSource().get(AbstractFormController.class, "buttonSave"),
                  click -> {
                     T entity = actionSave();
                     persistedEntityConsumer.accept(entity);
                     actionClose();
                     Notification.show(getMessageSource().get(AbstractFormController.class, "saved"), Type.TRAY_NOTIFICATION);
                  }
            ),
            new MButton(
                  FontAwesome.REMOVE,
                  getMessageSource().get(AbstractFormController.class, "buttonClose"),
                  click -> actionClose()
            )
      );
   }

   protected void edit(T entity, MTable<T> listTable) {

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
            listTable.getContainerDataSource().removeItem(entity);
         }
         listTable.addItem(persistedEntity);
         listTable.sort();
      };

      FormView<T> formView = getFormView();
      Component controlLayout = createControlLayout(persistedEntityConsumer);

      window = new Window(
            getFormCaption(),
            new MVerticalLayout(formView, controlLayout)
      );

      this.model = editEntity;
      this.binding = BeanBinder.bind(model, formView);

      formView.manualBinding(binding);
      formView.initCustomFieldValues(model);

      customizeWindow(window);

      getValidators()
            .forEach(binding::addValidator);

      window.center();
      window.setModal(true);
      UI.getCurrent().addWindow(window);
   }


   protected MessageSourceAdapter getMessageSource() {
      return messages;
   }

   protected Collection<MBeanFieldGroup.MValidator<T>> getValidators() {
      return Collections.emptyList();
   }

   protected void customizeWindow(Window window) {/*hook*/}

   //TODO move to FormView ?
   protected String getFormCaption() {
      return getMessageSource().get(this, "caption");
   }

   protected abstract EntityRepository<T, ID> getEntityRepository();

   protected abstract FormView<T> getFormView();
}
