package lt.pavilonis.cmm.canteen.views.user;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.UI;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.canteen.views.user.form.UserMealPopup;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.converter.CollectionCellConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.vaadin.ui.Table.Align.CENTER;

@UIScope
@SpringComponent
public class UserMealTable extends ListTable<UserMeal> {

   private final UserMealService pupilService;

   @Autowired
   public UserMealTable(UserMealService pupilService, UserMealPopup userMealForm) {

      this.pupilService = pupilService;

      withProperties("user.cardCode", "user.firstName", "user.lastName",
            "user.birthDate", "user.group", "mealData.comment", "mealData.meals");

//      setSortableProperties("user.cardCode", "user.firstName", "user.lastName",
//            "user.birthDate", "user.group", "mealData.comment", "mealData.meals");

      withColumnHeaders("Kodas", "Vardas", "Pavardė", "Gimimo data", "Klasė", "Komentaras", "Porcijos");

      setColumnWidth("user.group", 85);
      setColumnWidth("user.birthDate", 130);
      setColumnWidth("user.cardCode", 180);

      setConverter("mealData.meals", new CollectionCellConverter());

      setColumnAlignment("user.birthDate", CENTER);


      addRowClickListener(click -> {
         if (click.isDoubleClick()) {
            userMealForm.edit(click.getRow().getUser().getCardCode());
            UI.getCurrent().addWindow(userMealForm);
         }
      });
   }

   void updateContainer() {
      Collection<UserMeal> userMeals = pupilService.loadAll();
      removeAllItems();
      addBeans(userMeals);
   }

   void reload() {
      updateContainer();
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Collections.singletonList("user.cardCode");
   }
}
