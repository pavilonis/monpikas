//package lt.pavilonis.cmm.warehouse.writeoff;
//
//import com.google.common.collect.ImmutableMap;
//import com.vaadin.ui.Component;
//import com.vaadin.ui.CssLayout;
//import com.vaadin.ui.TreeGrid;
//import lt.pavilonis.cmm.App;
//import lt.pavilonis.cmm.common.field.OneToManyField;
//import lt.pavilonis.cmm.warehouse.balance.BalanceNode;
//
//public class WriteOffItemsField extends OneToManyField<WriteOffItem> {
//
//   public WriteOffItemsField() {
//      super(
//            WriteOffItem.class,
//            ImmutableMap.of(
//                  "receiptItem", item -> item.getReceiptItem().getProduct().getName(),
//                  "productGroup", item -> item.getReceiptItem().getProduct().getProductGroup().getName()
//            ),
//            "productGroup", "receiptItem", "quantity", "unitPrice", "cost"
//      );
//   }
//
//   @Override
//   protected Component createControls() {
//      return new CssLayout();
//   }
//
//   @Override
//   protected Component initContent() {
//
//      TreeGrid<WriteOffItemNode> tree = new TreeGrid<>();
//
//      getValue()
//
//      tree.setItems(loadTreeData(), BalanceNode::getChildren);
//      tree.addColumn(WriteOffItemNode::getName)
//            .setCaption(App.translate(BalanceNode.class, "name"));
//      tree.addColumn(WriteOffItemNode::getQuantity)
//            .setCaption(App.translate(BalanceNode.class, "quantity"));
//
//
//      tree.setSizeFull();
//      return tree;
//
//   }
//}
