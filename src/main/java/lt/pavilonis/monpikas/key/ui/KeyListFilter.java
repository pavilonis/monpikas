package lt.pavilonis.monpikas.key.ui;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lt.pavilonis.monpikas.common.ui.filter.IdPeriodTextFilter;

@Getter
@SuperBuilder
public final class KeyListFilter extends IdPeriodTextFilter {

   private Long scannerId;
   private boolean logMode;

}
