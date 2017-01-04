package lt.pavilonis.cmm.canteen.views.pupils;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import lt.pavilonis.cmm.canteen.domain.Pupil;
import lt.pavilonis.cmm.canteen.domain.PupilLocalData;
import lt.pavilonis.cmm.canteen.repositories.PupilDataRepository;
import lt.pavilonis.cmm.canteen.repositories.UserRepository;
import lt.pavilonis.cmm.canteen.service.MealService;
import lt.pavilonis.cmm.canteen.service.PupilService;
import lt.pavilonis.cmm.canteen.views.converters.CollectionCellConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.show;
import static com.vaadin.ui.Table.Align.CENTER;

@UIScope
@SpringComponent
public class PupilTable extends MTable<Pupil> {

   private final PupilService pupilService;
   private final PupilDataRepository pupilDataRepository;
   private final UserRepository userRepository;
   private final MealService mealService;
   private final PupilEditForm pupilEditForm;

   @Autowired
   public PupilTable(PupilService pupilService,
                     PupilDataRepository pupilDataRepository,
                     UserRepository userRepository,
                     MealService mealService,
                     PupilEditForm pupilEditForm) {

      this.pupilService = pupilService;
      this.pupilDataRepository = pupilDataRepository;
      this.userRepository = userRepository;
      this.mealService = mealService;
      this.pupilEditForm = pupilEditForm;

      reload();

      setVisibleColumns("cardCode", "firstName", "lastName", "birthDate", "grade", "comment", "meals");
      setColumnHeaders("Kodas", "Vardas", "Pavardė", "Gimimo data", "Klasė", "Komentaras", "Porcijos");

      setColumnWidth("grade", 85);
      setColumnWidth("birthDate", 130);
      setColumnWidth("cardCode", 90);

      setConverter("meals", new CollectionCellConverter());

      setColumnAlignment("birthDate", CENTER);

      setColumnCollapsingAllowed(true);
      setColumnCollapsed("cardCode", true);
      setSelectable(true);
      setNullSelectionAllowed(false);
      setCacheRate(5);

      addRowClickListener(click -> {
         if (click.isDoubleClick()) {
            pupilEditForm.edit(click.getRow().getCardCode());
         }
      });
   }

   private void popupAction(Pupil pupil) {
      String cardCode = pupil.getCardCode();
      PupilEditForm view = new PupilEditForm(
            pupilDataRepository.load(cardCode).orElse(new PupilLocalData(cardCode)),
            userRepository.load(cardCode).get(),
            mealService.lastMealEvent(cardCode)
      );

      view.addAddMealButtonListener(click -> {

      });

      view.addRemoveMealButtonListener(click -> {
         @SuppressWarnings("unchecked")
         Collection<Long> selected = (Collection<Long>) view.getTable().getValue();
         view.removeFromContainer(selected);
      });

      view.addCloseButtonListener(closeBtnClick -> view.close());
      view.addSaveButtonListener(
            saveBtnClick -> {

               if (!view.isValid())
                  return;

               Collection<?> itemIds = view.getTable().getItemIds();

               PupilLocalData model = view.getModel();
               model.setMeals(new HashSet<>(mealRepository.load(itemIds)));

               view.commit();
               pupilDataRepository.saveOrUpdate(model);
               Table tbl = (Table) event.getSource();

               @SuppressWarnings("unchecked")
               BeanContainer<String, Pupil> container =
                     (BeanContainer<String, Pupil>) tbl.getContainerDataSource();

               int index = container.indexOfId(cardCode);
               container.removeItem(cardCode);
               container.addBeanAt(index, pupilService.load(cardCode).orElseThrow(IllegalStateException::new));
               view.close();
               show("Išsaugota", TRAY_NOTIFICATION);
            });

      UI.getCurrent().addWindow(view);
   }

   void updateContainer() {
      Collection<Pupil> pupils = pupilService.loadAll();
      removeAllItems();
      addBeans(pupils);
   }

   void reload() {
      updateContainer();
   }
}
