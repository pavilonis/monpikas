package lt.pavilonis.monpikas.common;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
public class Identified<ID> {

   @Getter
   @Setter
   private ID id;
}
