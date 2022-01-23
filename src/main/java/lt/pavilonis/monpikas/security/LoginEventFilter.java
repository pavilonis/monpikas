package lt.pavilonis.monpikas.security;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lt.pavilonis.monpikas.common.ui.filter.IdPeriodTextFilter;

@Getter
@SuperBuilder
public class LoginEventFilter extends IdPeriodTextFilter {

   private boolean success;
   private boolean logout;

}
