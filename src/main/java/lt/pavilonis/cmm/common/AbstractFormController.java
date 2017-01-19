package lt.pavilonis.cmm.common;

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


public abstract class AbstractFormController<T, ID> {

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
         return null;
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

      Consumer<T> persistedEntityConsumer = persistentEntity -> {
         if (listTable.containsId(persistentEntity)) {
            listTable.refreshRows();
         } else {
            listTable.addBeans(persistentEntity);
            listTable.sort();
         }
      };

      Component fieldLayout = createFieldLayout();
      Component controlLayout = createControlLayout(persistedEntityConsumer);

      window = new Window(
            getFormCaption(),
            new MVerticalLayout(fieldLayout, controlLayout)
      );

      this.model = entity;
      this.binding = BeanBinder.bind(model, fieldLayout);

      getValidators()
            .forEach(binding::addValidator);

      customizeWindow(window);
      window.center();
      UI.getCurrent().addWindow(window);
   }


   protected MessageSourceAdapter getMessageSource() {
      return messages;
   }

   protected Collection<MBeanFieldGroup.MValidator<T>> getValidators() {
      return Collections.emptyList();
   }

   protected void customizeWindow(Window window) {/*hook*/}

   protected String getFormCaption() {
      return getMessageSource().get(this, "caption");
   }

   protected abstract EntityRepository<T, ID> getEntityRepository();

   protected abstract Component createFieldLayout();
}
