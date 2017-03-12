package lt.pavilonis.cmm.canteen.views.user;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.common.component.TableControlPanel;
import lt.pavilonis.cmm.common.field.AButton;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class UserMealSelectionPopup extends Window {

   public UserMealSelectionPopup(List<Meal> mealsToSelectFrom, Consumer<Set<Meal>> selectionConsumer) {

      MessageSourceAdapter messages = App.context.getBean(MessageSourceAdapter.class);
      setCaption(messages.get(this, "caption"));

      setWidth(700, Unit.PIXELS);
      setHeight(490, Unit.PIXELS);

      MealGrid grid = new MealGrid(mealsToSelectFrom);
      grid.collapseColumns();
      grid.addSelectionListener(click -> selectAction(selectionConsumer, click.getAllSelectedItems()));

      HorizontalLayout controls = new HorizontalLayout(
            new AButton(TableControlPanel.class.getSimpleName() + ".addSelected")
                  .withIcon(VaadinIcons.PLUS)
                  .withClickListener(click -> selectAction(selectionConsumer, grid.getSelectedItems())),
            new AButton("Uždaryti")
                  .withIcon(VaadinIcons.CLOSE)
                  .withClickListener(click -> close())
      );

      VerticalLayout layout = new VerticalLayout(grid, controls);
      layout.setSizeFull();
      layout.setExpandRatio(grid, 1);

      setContent(layout);
      setModal(true);

      UI.getCurrent().addWindow(this);
   }

   protected void selectAction(Consumer<Set<Meal>> mealSelectionConsumer, Set<Meal> selectedValue) {
      if (CollectionUtils.isNotEmpty(selectedValue)) {
         mealSelectionConsumer.accept(selectedValue);
      }
      close();
   }
}
