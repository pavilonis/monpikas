package lt.pavilonis.cmm.classroom;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.ListGrid;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
public class ClassroomListController extends AbstractListController<ClassroomOccupancy, Void, ClassroomFilter> {

   @Autowired
   private ClassroomRepository repository;

   @Override
   protected ListGrid<ClassroomOccupancy> createGrid() {
      return new ClassroomListGrid();
   }

   @Override
   protected FilterPanel<ClassroomFilter> createFilterPanel() {
      return new ClassroomFilterPanel();
   }

   @Override
   protected EntityRepository<ClassroomOccupancy, Void, ClassroomFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<ClassroomOccupancy> getEntityClass() {
      return ClassroomOccupancy.class;
   }

   @Override
   protected Component getControlPanel() {
      return null;
   }

   @Override
   public String getViewName() {
      return "classrooms";
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.TIME_FORWARD;
   }

   @Override
   public String getViewRole() {
      return "CLASSROOMS";
   }

   @Override
   public String getViewGroupName() {
      return "school";
   }
}
