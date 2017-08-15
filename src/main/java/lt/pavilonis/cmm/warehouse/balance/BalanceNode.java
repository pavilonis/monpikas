package lt.pavilonis.cmm.warehouse.balance;

import java.math.BigDecimal;
import java.util.List;

public class BalanceNode {

   private final String name;
   private final BigDecimal quantity;
   private final List<BalanceNode> children;

   public BalanceNode(String name, BigDecimal quantity, List<BalanceNode> children) {
      this.name = name;
      this.quantity = quantity;
      this.children = children;
   }

   public String getName() {
      return name;
   }

   public BigDecimal getQuantity() {
      return quantity;
   }

   public List<BalanceNode> getChildren() {
      return children;
   }
}
