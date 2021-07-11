package lt.pavilonis.cmm.api.rest.user;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.common.Named;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class User extends Named<Long> {

   private String cardCode;

   private String organizationGroup;

   private String organizationRole;

   private LocalDate birthDate;

   private String base16photo;

   public User() {/**/}

   public User(Long id, String cardCode, String name, String organizationGroup,
               String organizationRole, String base16photo, LocalDate birthDate) {

      setId(id);
      setName(name);
      this.cardCode = cardCode;
      this.organizationGroup = organizationGroup;
      this.organizationRole = organizationRole;
      this.base16photo = base16photo;
      this.birthDate = birthDate;
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

   public String getBase16photo() {
      return base16photo;
   }

   public void setBase16photo(String base16photo) {
      this.base16photo = base16photo;
   }

   @Override
   public String toString() {
      return "cardCode='" + cardCode + '\'' +
            ", name='" + getName() + '\'' +
            ", group='" + organizationGroup + '\'' +
            ", role='" + organizationRole + '\'' +
            ", birthDate='" + birthDate + '\'' +
            ", photoBase16='" + (StringUtils.isBlank(base16photo) ? "absent" : "present") + '\'';
   }
}
