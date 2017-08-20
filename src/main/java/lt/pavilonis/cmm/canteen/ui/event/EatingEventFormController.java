package lt.pavilonis.cmm.canteen.ui.event;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.canteen.domain.EatingEvent;
import lt.pavilonis.cmm.canteen.repository.EatingEventRepository;
import lt.pavilonis.cmm.canteen.service.UserEatingService;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;

@UIScope
@SpringComponent
public class EatingEventFormController extends AbstractFormController<EatingEvent, Long> {

   public EatingEventFormController() {
      super(EatingEvent.class);
   }

   @Autowired
   private EatingEventRepository repository;

   @Autowired
   private UserEatingService service;

   @Override
   protected FieldLayout<EatingEvent> createFieldLayout(EatingEvent model) {
      return new EatingEventFormView(service);
   }

   @Override
   protected Collection<Validator<EatingEvent>> getValidators() {
      return Arrays.asList(
            (value, context) -> service.portionAssigned(value.getCardCode(), value.getEatingType())
                  ? ValidationResult.ok()
                  : ValidationResult.error("Mokinys neturi leidimo šio tipo maitinimuisi"),
            (value, context) -> service.canEat(value.getCardCode(), value.getDate(), value.getEatingType())
                  ? ValidationResult.ok()
                  : ValidationResult.error("Viršijamas nurodytos dienos maitinimosi limitas"),
            (value, context) -> StringUtils.isNotBlank(value.getCardCode())
                  ? ValidationResult.ok()
                  : ValidationResult.error("Nepasirinktas mokinys")
      );
   }

   @Override
   protected EntityRepository<EatingEvent, Long, ?> getEntityRepository() {
      return repository;
   }

   @Override
   protected void customizeWindow(Window window) {
      window.setWidth(430, Unit.PIXELS);
      window.setHeight(600, Unit.PIXELS);
   }
}
