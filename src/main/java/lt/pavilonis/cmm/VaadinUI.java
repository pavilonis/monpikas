package lt.pavilonis.cmm;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.AbstractBeanContainer.BeanIdResolver;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Collections;

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {

   private final UserRestRepository repo;
   private final Table table = new Table();
   private final TextField filter = new TextField();
   private final Button addNewBtn = new Button("New customer", FontAwesome.PLUS);

   @Autowired
   public VaadinUI(UserRestRepository repo) {
      this.repo = repo;
   }

   @Override
   protected void init(VaadinRequest request) {
      HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
      VerticalLayout mainLayout = new VerticalLayout(actions, table);
      setContent(mainLayout);

      actions.setSpacing(true);
      mainLayout.setMargin(true);
      mainLayout.setSpacing(true);

      updateContainer(null);
      table.setVisibleColumns("cardCode", "firstName", "lastName");
      table.setColumnHeaders("Card", "First name", "Last name");
      filter.setInputPrompt("Filter by last name");

      filter.addTextChangeListener(e -> updateContainer(e.getText()));

   }

   void updateContainer(String text) {

      BeanContainer<String, UserRepresentation> container = new BeanContainer<>(UserRepresentation.class);
      container.setBeanIdResolver((BeanIdResolver<String, UserRepresentation>) UserRepresentation::getCardCode);
      try {
         container.addAll(
               StringUtils.isEmpty(text)
                     ? repo.loadAll()
                     : Collections.singletonList(repo.load(text))
         );
      } catch (Exception e) {
         Notification.show("Could not load data: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
      }

      table.setContainerDataSource(container);
   }
}
