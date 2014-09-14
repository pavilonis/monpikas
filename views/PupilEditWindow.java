package lt.pavilonis.monpikas.server.views;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.monpikas.server.dao.PupilDto;

public class PupilEditWindow extends Window{
   public PupilEditWindow(PupilDto pupil, String caption){
      setCaption(caption);
      setResizable(false);
      setHeight("250px");
      setWidth("400px");
      VerticalLayout vLayout = new VerticalLayout();
      vLayout.addComponent(new Label(pupil.getFirstName()));
      vLayout.addComponent(new Label(pupil.getLastName()));
      vLayout.addComponent(new Label(pupil.getBirthDate()!=null ? pupil.getBirthDate().toString() : "empty :("));
      GridLayout layout = new GridLayout(2,3);
      layout.setSpacing(true);
      layout.setSizeFull();
      layout.addComponent(vLayout);
      layout.addComponent(new Label("Here's some content"));
      layout.addComponent(new CheckBox("Some text"));
      layout.addComponent(new TextArea("Comment"));
      layout.addComponent(new Label());
      layout.addComponent(new Button("Saugoti"));
      setContent(layout);
      setModal(true);
   }
}
