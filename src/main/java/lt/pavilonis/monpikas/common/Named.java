package lt.pavilonis.monpikas.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Named<ID> extends Identified<ID> {

   public Named(ID id, String name) {
      setId(id);
      this.name = name;
   }

   @NotBlank
   private String name;
}
