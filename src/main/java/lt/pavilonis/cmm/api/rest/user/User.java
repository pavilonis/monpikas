package lt.pavilonis.cmm.api.rest.user;

import lt.pavilonis.cmm.common.Identified;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

public class User extends Identified<String> {

   private String cardCode;

   @NotNull
   private String name;

   private String organizationGroup;

   private String organizationRole;

   private String birthDate;

   private String base16photo;

   public User() {/**/}

   public User(String cardCode, String name, String organizationGroup,
               String organizationRole, String base16photo, String birthDate) {

      this.cardCode = cardCode;
      this.name = name;
      this.organizationGroup = organizationGroup;
      this.organizationRole = organizationRole;
      this.base16photo = base16photo;
      this.birthDate = birthDate;
   }

   public String getCardCode() {
      return cardCode;
   }

   public void setName(String name) {
      this.name = name;
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

   public String getBirthDate() {
      return birthDate;
   }

   public void setBirthDate(String birthDate) {
      this.birthDate = birthDate;
   }

   public String getBase16photo() {
      return base16photo;
   }

   public void setBase16photo(String base16photo) {
      this.base16photo = base16photo;
   }

   public String getName() {
      return name;
   }

   @Override
   public String getId() {
      return cardCode;
   }

   @Override
   public String toString() {
      return "cardCode='" + cardCode + '\'' +
            ", name='" + name + '\'' +
            ", group='" + organizationGroup + '\'' +
            ", role='" + organizationRole + '\'' +
            ", birthDate='" + birthDate + '\'' +
            ", photoBase16='" + (StringUtils.isBlank(base16photo) ? "absent" : "present") + '\'';
   }
}
