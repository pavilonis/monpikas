package lt.pavilonis.cmm;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.vaadin.server.VaadinService.getCurrent;
import static com.vaadin.ui.Button.ClickListener;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.slf4j.LoggerFactory.getLogger;

@SpringComponent
@UIScope
public class UserEditPopup extends Window {

   private static final Logger LOG = getLogger(UserEditPopup.class.getSimpleName());
   private static final String NO_PHOTO_PATH = "static/images/noPhoto.png";
   private final Button save = new Button("Saugoti");
   private final Button close = new Button("Uždaryti");
   private final BeanFieldGroup<UserRepresentation> group = new BeanFieldGroup<>(UserRepresentation.class);
   private final UserRestRepository userRepository;

   private UserRepresentation currentUser;

   @PropertyId("firstName")
   TextField firstName = new TextField("First name");
   TextField lastName = new TextField("Last name");

   @Autowired
   public UserEditPopup(UserRestRepository userRepository) {
      this.userRepository = userRepository;

      Layout layout = new VerticalLayout();
      layout.addComponents(firstName, lastName);

      setCaption("Mokinio nustatymai");
      setResizable(false);
      setWidth("655px");
      setHeight("650px");
      HorizontalLayout buttons = new HorizontalLayout(save, close);
      buttons.setSpacing(true);
      buttons.setMargin(new MarginInfo(true, false, false, false));
      setContent(layout);
      setModal(true);
   }

   public void edit(UserRepresentation user) {

      boolean persisted = user.getCardCode() != null;

      currentUser = persisted
            ? userRepository.load(user.getCardCode())
            : user;

      BeanFieldGroup.bindFieldsUnbuffered(currentUser, this);

      // A hack to ensure the whole form is visible
      save.focus();
      // Select all text in firstName field automatically
      firstName.selectAll();
      VaadinUI.getCurrent().addWindow(this);
   }

   public void addSaveButtonListener(ClickListener listener) {
      save.addClickListener(listener);
   }

   public void addCloseButtonListener(ClickListener listener) {
      close.addClickListener(listener);
   }

   public boolean isValid() {
      return group.isValid();
   }

   private Image image(String url) {
      Resource resource = remoteImageExists(url)
            ? new ExternalResource(url)
            : new FileResource(new File(
            //TODO simplify?
            getCurrent().getBaseDirectory().getAbsolutePath() + File.separator + NO_PHOTO_PATH));

      Image image = new Image(null, resource);
      image.setWidth("170px");
      image.setHeight("211px");
      return image;
   }

   private boolean remoteImageExists(String url) {
      try {
         URL u = new URL(url);
         u.getPath();
         HttpURLConnection http = (HttpURLConnection) u.openConnection();
         http.setInstanceFollowRedirects(false);
         http.setRequestMethod("HEAD");
         http.connect();
         return (http.getResponseCode() == HTTP_OK);
      } catch (Exception e) {
         LOG.error("Image request error: " + e);
         return false;
      }
   }
}
