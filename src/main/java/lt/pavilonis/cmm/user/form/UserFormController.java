package lt.pavilonis.cmm.user.form;

import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.user.domain.PresenceTimeRepresentation;
import lt.pavilonis.cmm.user.domain.UserRepresentation;
import lt.pavilonis.cmm.user.repository.UserRestRepository;
import lt.pavilonis.cmm.common.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SpringComponent
@UIScope
public class UserFormController extends AbstractFormController<UserRepresentation, String> {

   @Autowired
   private UserRestRepository userRepository;

   @Autowired
   private ImageService imageService;

   @Autowired
   public UserFormController() {
      super(UserRepresentation.class);
   }

   @Override
   protected EntityRepository<UserRepresentation, String, ?> getEntityRepository() {
      return userRepository;
   }

   @Override
   protected FieldLayout<UserRepresentation> createFieldLayout() {
      List<PresenceTimeRepresentation> presenceTimeData = userRepository.loadPresenceTime(model.getCardCode());
      Resource image = imageService.imageResource(model.getBase16photo());
      return new UserFormView(presenceTimeData, image);
   }
}
