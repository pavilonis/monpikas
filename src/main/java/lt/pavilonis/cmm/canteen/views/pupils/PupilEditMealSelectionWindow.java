package lt.pavilonis.monpikas.server.views.pupils;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.monpikas.server.domain.Meal;
import lt.pavilonis.monpikas.server.views.converters.MealTypeCellConverter;
import lt.pavilonis.monpikas.server.views.converters.ModifiedStringToDoubleConverter;

import static com.vaadin.ui.Alignment.BOTTOM_CENTER;
import static com.vaadin.ui.Button.ClickListener;
import static lt.pavilonis.monpikas.server.utils.Messages.label;

public class PupilEditMealSelectionWindow extends Window {

   private final Button save = new Button("Pridėti pasirinktą", FontAwesome.PLUS);
   private final Button close = new Button("Uždaryti", FontAwesome.TIMES);
   private final BeanContainer<Long, Meal> container = new BeanContainer<>(Meal.class);
   private final Table table = new MealTable("PupilEditMealSelectionWindow.MealSelectionPopup.TableCaption", container);

   public PupilEditMealSelectionWindow() {

      //TODO set form model (container) instead of getting values from fields manually

      setCaption(label("PupilEditMealSelectionWindow.MealSelectionPopup.Caption"));
      setCaption(label("PupilEditMealSelectionWindow.MealSelectionPopup.TableCaption"));
      setResizable(false);
      setWidth("550px");
      setHeight("540px");

      VerticalLayout vl = new VerticalLayout(table);
      vl.setSpacing(true);
      vl.setMargin(true);

      HorizontalLayout buttons = new HorizontalLayout(save, close);
      buttons.setSpacing(true);
      vl.addComponent(buttons);
      vl.setComponentAlignment(buttons, BOTTOM_CENTER);
      setContent(vl);
      setModal(true);
   }

   public void addSaveButtonListener(ClickListener listener) {
      save.addClickListener(listener);
   }

   public void addCloseButtonListener(ClickListener listener) {
      close.addClickListener(listener);
   }

   public BeanContainer<Long, Meal> getContainer() {
      return container;
   }

   public Table getTable() {
      return table;
   }

   private class MealTable extends Table {
      public MealTable(String caption, BeanContainer<Long, Meal> container) {
         super(caption, container);
         container.setBeanIdProperty("id");
         setSizeFull();
         setVisibleColumns(new String[]{"name", "type", "price"});
         setColumnHeaders("Pavadinimas", "Tipas", "Kaina");

         setConverter("type", new MealTypeCellConverter());
         setConverter("price", new ModifiedStringToDoubleConverter());

         setColumnCollapsingAllowed(true);
         setSelectable(true);
         setMultiSelect(true);
         setNullSelectionAllowed(false);
         setCacheRate(5);
         setHeight("370px");
      }
   }
}
