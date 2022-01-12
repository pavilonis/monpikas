package lt.pavilonis.monpikas.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lt.pavilonis.monpikas.common.Named;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@SuperBuilder
@Getter
public class User extends Named<Long> {

   private LocalDateTime created;
   private LocalDateTime updated;

   @NotNull
   @Setter
   private String cardCode;

   @Setter
   private String organizationGroup;

   @Setter
   private String organizationRole;

   @Setter
   private LocalDate birthDate;

   @Setter
   private String base64photo;

   @Setter
   private User supervisor;

   public User(long id, String name) {
      setId(id);
      setName(name);
   }

   public User(Long id, LocalDateTime created, LocalDateTime updated, String cardCode,
               String name, String organizationGroup, String organizationRole,
               String base64photo, LocalDate birthDate, User supervisor) {

      setId(id);
      setName(name);
      this.created = created;
      this.updated = updated;
      this.cardCode = cardCode;
      this.organizationGroup = organizationGroup;
      this.organizationRole = organizationRole;
      this.base64photo = base64photo;
      this.birthDate = birthDate;
      this.supervisor = supervisor;
   }

   @Override
   public String toString() {
      return "cardCode='" + cardCode + '\'' +
            ", name='" + getName() + '\'' +
            ", group='" + organizationGroup + '\'' +
            ", role='" + organizationRole + '\'' +
            ", birthDate='" + birthDate + '\'' +
            ", photoBase64='" + (StringUtils.hasText(base64photo) ? "present" : "absent") + '\'' +
            ", supervisor='" + (supervisor == null ? null : supervisor.getName()) + '\'';
   }
}
