package lt.pavilonis.cmm.warehouse.writeoff;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.AllOf.allOf;

public class BigDecimalMapValueExtractorTest {
   private Map<Long, BigDecimal> map;

   @Before
   public void setUp() {
      map = new HashMap<>(3);
      map.put(1l, BigDecimal.ONE);
      map.put(2l, BigDecimal.TEN);
      map.put(3l, BigDecimal.valueOf(100));
   }

   @Test
   public void testCorrectResult() {
      Map<Long, BigDecimal> result = BigDecimalMapValueExtractor.extract(BigDecimal.valueOf(15.2), map);
      assertThat(
            result,
            allOf(
                  hasEntry(1l, BigDecimal.ONE),
                  hasEntry(2l, BigDecimal.valueOf(10)),
                  hasEntry(3l, BigDecimal.valueOf(4.2))
            )
      );
   }

   @Test
   public void testCorrectRemainder() {
      BigDecimalMapValueExtractor.extract(BigDecimal.valueOf(15.2), map);
      assertThat(
            map,
            allOf(
                  hasEntry(1l, BigDecimal.ZERO),
                  hasEntry(2l, BigDecimal.ZERO),
                  hasEntry(3l, BigDecimal.valueOf(95.8))
            )
      );
   }
}