package lt.pavilonis.monpikas.common.ui.filter;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
public class IdPeriodFilter {

   private Long id;
   private LocalDate periodStart;
   private LocalDate periodEnd;

}
