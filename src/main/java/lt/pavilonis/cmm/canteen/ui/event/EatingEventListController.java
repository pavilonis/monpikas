package lt.pavilonis.cmm.canteen.ui.event;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.canteen.domain.EatingEvent;
import lt.pavilonis.cmm.canteen.domain.EatingType;
import lt.pavilonis.cmm.canteen.repository.EatingEventRepository;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@UIScope
@SpringComponent
public class EatingEventListController extends AbstractListController<EatingEvent, Long, EatingEventFilter> {

   @Autowired
   private EatingEventRepository repository;

   @Autowired
   private EatingEventFormController formController;

   @Override
   protected void addGridClickListener(ListGrid<EatingEvent> table) {/*do nothing*/}

   @Override
   protected ListGrid<EatingEvent> createGrid() {
      return new EatingEventGrid();
   }

   @Override
   protected EntityRepository<EatingEvent, Long, EatingEventFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected FilterPanel<EatingEventFilter> createFilterPanel() {
      return new EatingEventFilterPanel();
   }

   @Override
   protected Class<EatingEvent> getEntityClass() {
      return EatingEvent.class;
   }

   @Override
   protected AbstractFormController<EatingEvent, Long> getFormController() {
      return formController;
   }

   @Override
   protected EatingEvent createNewInstance() {
      EatingEvent entity = new EatingEvent();
      entity.setDate(new Date());
      entity.setEatingType(EatingType.DINNER);
      return entity;
   }

   @Override
   public String getViewName() {
      return "eating-events";
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.CUTLERY;
   }

   @Override
   public String getViewRole() {
      return "EATING_EVENTS";
   }

   @Override
   public String getViewGroupName() {
      return "canteen";
   }
}
