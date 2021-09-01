package lt.pavilonis.monpikas.user;

import lt.pavilonis.monpikas.common.Named;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class User extends Named<Long> {

   private final LocalDateTime created;

   private final LocalDateTime updated;

   @NotNull
   private String cardCode;

   private String organizationGroup;

   private String organizationRole;

   private LocalDate birthDate;

   private String base64photo;

   private User supervisor;

   public User() {
      this.created = null;
      this.updated = null;
   }

   public User(long id, String name) {
      setId(id);
      setName(name);
      this.created = null;
      this.updated = null;
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

   public void setCardCode(String cardCode) {
      this.cardCode = cardCode;
   }

   public String getCardCode() {
      return cardCode;
   }

   public String getOrganizationGroup() {
      return organizationGroup;
   }

   public void setOrganizationGroup(String organizationGroup) {
      this.organizationGroup = organizationGroup;
   }

   public String getOrganizationRole() {
      return organizationRole;
   }

   public void setOrganizationRole(String organizationRole) {
      this.organizationRole = organizationRole;
   }

   public LocalDate getBirthDate() {
      return birthDate;
   }

   public void setBirthDate(LocalDate birthDate) {
      this.birthDate = birthDate;
   }

   public String getBase64photo() {
      return base64photo;
   }

   public void setBase64photo(String base64photo) {
      this.base64photo = base64photo;
   }

   public User getSupervisor() {
      return supervisor;
   }

   public void setSupervisor(User supervisor) {
      this.supervisor = supervisor;
   }

   public LocalDateTime getCreated() {
      return created;
   }

   public LocalDateTime getUpdated() {
      return updated;
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
