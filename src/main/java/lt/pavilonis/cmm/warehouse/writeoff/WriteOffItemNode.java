package lt.pavilonis.cmm.warehouse.writeoff;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class WriteOffItemNode {
   public static void main(String[] args) {
      Map<Long, BigDecimal> a = new HashMap<>();
      a.put(1l, BigDecimal.ONE);
      a.put(2l, new BigDecimal(2));
      a.put(3l, new BigDecimal(3));
      System.out.println(a);

      Map<Long, BigDecimal> b = new HashMap<>(a);
      System.out.println(b);

      a.remove(1l);
      System.out.println(b);
      System.out.println(a);


   }
}
