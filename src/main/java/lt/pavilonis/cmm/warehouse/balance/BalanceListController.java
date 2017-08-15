package lt.pavilonis.cmm.warehouse.balance;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Component;
import com.vaadin.ui.TreeGrid;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.AbstractViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BalanceListController extends AbstractViewController {

   @Autowired
   private BalanceRepository repository;

   @Override
   protected Component getMainArea() {

//      TreeData<BalanceNode> treeData = new TreeData<>();
//      Collection<BalanceNode> items = loadTreeData();
//      treeData.addRootItems(items);
//
      TreeGrid<BalanceNode> tree = new TreeGrid<>();
      tree.setItems(loadTreeData(), BalanceNode::getChildren);
      tree.addColumn(BalanceNode::getName)
            .setCaption(App.translate(BalanceNode.class, "name"));
      tree.addColumn(BalanceNode::getQuantity)
            .setCaption(App.translate(BalanceNode.class, "quantity"));

      tree.setSizeFull();
      return tree;
   }

   private Collection<BalanceNode> loadTreeData() {

      List<Balance> data = repository.load();
      return data
            .stream()
            .map(b -> {
               String name = b.getProductGroup().getName();
               BigDecimal sum = b.getQuantity()
                     .values()
                     .stream()
                     .reduce(BigDecimal.ZERO, BigDecimal::add);

               List<BalanceNode> children = b.getQuantity()
                     .entrySet()
                     .stream()
                     .map(entry -> new BalanceNode(
                           entry.getKey().getName(),
                           entry.getValue(),
                           Collections.emptyList()
                     ))
                     .collect(Collectors.toList());

               return new BalanceNode(name, sum, children);
            })
            .collect(Collectors.toList());
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.SCALE_UNBALANCE;
   }

   @Override
   public String getViewRole() {
      return "WAREHOUSE";
   }

   @Override
   public String getViewGroupName() {
      return "warehouse";
   }

   @Override
   public String getViewName() {
      return "balance";
   }
}
