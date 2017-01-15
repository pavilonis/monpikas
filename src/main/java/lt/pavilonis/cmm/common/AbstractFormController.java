package lt.pavilonis.cmm.common;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.UserError;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.BeanBinder;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;


public abstract class AbstractFormController<T, ID> extends Window implements FormController {

   private T model;
   private MBeanFieldGroup<T> binding;

   @Autowired
   protected MessageSourceAdapter messageSourceAdapter;

   public AbstractFormController() {
      setCaption(messageSourceAdapter.get(this, "caption"));
      setContent(composeLayout());
   }

   @Override
   public void actionSave() {
      if (!binding.isValid()) {
         setComponentError(new UserError("Invalid field values"));
//         Notification.show("Invalid field values", Notification.Type.WARNING_MESSAGE);
         return;
      }

      EntityRepository<T, ID> entityRepository = getEntityRepository();
      entityRepository.saveOrUpdate(model);
   }

   @Override
   public void actionClose() {
      close();
   }

   public Component composeLayout() {
      Component fieldLayout = createFieldLayout();
      Component controlLayout = createControlLayout();
      return new MVerticalLayout(
            fieldLayout,
            controlLayout
      );
   }

   private Component createControlLayout() {
      return new MHorizontalLayout(
            new MButton(FontAwesome.CHECK, messageSourceAdapter.get(this, "buttonSave"), click -> actionSave()),
            new MButton(FontAwesome.REMOVE, messageSourceAdapter.get(this, "buttonClose"), click -> actionClose())
      );
   }

   protected abstract Component createFieldLayout();

   public void edit(T entity) {
      this.model = entity;
      this.binding = BeanBinder.bind(model, this);
      UI.getCurrent().addWindow(this);
   }

   protected abstract EntityRepository<T, ID> getEntityRepository();
}
