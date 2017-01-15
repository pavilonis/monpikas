package lt.pavilonis.cmm.ui.key;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.domain.KeyRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;

import java.util.Optional;

@SpringComponent
@UIScope
public class KeyListController extends AbstractListController<KeyRepresentation, String> {

   @Autowired
   private KeyListFilterPanel keyListFilterPanel;

   @Autowired
   private KeyTable keyTable;

   @Override
   protected MTable<KeyRepresentation> getTable() {
      return keyTable;
   }

   @Override
   protected Optional<Component> getHeader() {
      return Optional.of(keyListFilterPanel);
   }

   @Override
   protected EntityRepository<KeyRepresentation, String> getEntityRepository() {
      return null;
   }

   @Override
   protected void loadTableData(MTable<KeyRepresentation> table) {
      keyListFilterPanel.reload();
   }
}
