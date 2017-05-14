package lt.pavilonis.cmm.warehouse.techcard;

import com.vaadin.data.Binder;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.techcard.field.TechCardProductGroupOutput;
import lt.pavilonis.cmm.warehouse.techcard.field.TechCardProductGroupOutputField;
import lt.pavilonis.cmm.warehouse.techcardgroup.TechCardGroup;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class TechCardForm extends FieldLayout<TechCard> {

   private final TextField name = new ATextField(TechCard.class, "name");

   private final ComboBox<TechCardGroup> techCardGroupComboBox;
   private final TechCardProductGroupOutputField productGroupOutputWeightField;

   public TechCardForm(List<TechCardGroup> techCardGroups, List<ProductGroup> productGroups) {
      techCardGroupComboBox = new ComboBox<>(App.translate(TechCard.class, "techCardGroup"), techCardGroups);
      techCardGroupComboBox.setItemCaptionGenerator(TechCardGroup::getName);

      productGroupOutputWeightField = new TechCardProductGroupOutputField(productGroups);

      addComponents(name, techCardGroupComboBox, productGroupOutputWeightField);
   }

   @Override
   public void manualBinding(Binder<TechCard> binding) {
      binding.bind(techCardGroupComboBox, TechCard::getGroup, TechCard::setGroup);

      binding.bind(
            productGroupOutputWeightField,

            techCard -> techCard.getProductGroupOutputWeight().entrySet().stream()
                  .map(entry -> new TechCardProductGroupOutput(entry.getKey(), entry.getValue()))
                  .collect(Collectors.toCollection(ArrayList::new)),

            (techCard, outputWeights) -> {
               if (CollectionUtils.isEmpty(outputWeights)) {
                  techCard.setProductGroupOutputWeight(Collections.emptyMap());
                  return;
               }
               Map<ProductGroup, BigDecimal> result = outputWeights.stream()
                     .collect(Collectors.toMap(
                           TechCardProductGroupOutput::getProductGroup,
                           TechCardProductGroupOutput::getOutputWeight
                     ));
               techCard.setProductGroupOutputWeight(result);
            }
      );
   }
}
