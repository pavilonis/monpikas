package lt.pavilonis.monpikas.user;

import lt.pavilonis.monpikas.common.Named;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class User extends Named<Long> {

   @NotNull
   private String cardCode;

   private String organizationGroup;

   private String organizationRole;

   private LocalDate birthDate;

   private String base64photo;

   private User supervisor;

   public User() {/**/}

   public User(long id, String name) {
      setId(id);
      setName(name);
   }

   public User(Long id, String cardCode, String name, String organizationGroup,
               String organizationRole, String base64photo, LocalDate birthDate, User supervisor) {

      setId(id);
      setName(name);
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

   @Override
   public String toString() {
      return "cardCode='" + cardCode + '\'' +
            ", name='" + getName() + '\'' +
            ", group='" + organizationGroup + '\'' +
            ", role='" + organizationRole + '\'' +
            ", birthDate='" + birthDate + '\'' +
            ", photoBase64='" + (StringUtils.isBlank(base64photo) ? "absent" : "present") + '\'' +
            ", supervisor='" + (supervisor == null ? null : supervisor.getName()) + '\'';
   }
}
