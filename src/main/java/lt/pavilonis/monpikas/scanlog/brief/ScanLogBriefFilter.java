package lt.pavilonis.monpikas.scanlog.brief;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lt.pavilonis.monpikas.common.ui.filter.IdPeriodTextFilter;

@Getter
@SuperBuilder
public final class ScanLogBriefFilter extends IdPeriodTextFilter {

   private Long scannerId;
   private String role;

}
