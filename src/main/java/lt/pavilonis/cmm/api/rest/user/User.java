package lt.pavilonis.cmm.api.rest.user;

import lt.pavilonis.cmm.common.Identified;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

public class User extends Identified<String> {

   private String cardCode;

   @NotNull
   private String firstName;

   @NotNull
   private String lastName;

   private String group;

   private String role;

   private String birthDate;

   private String base16photo;

   public User() {/**/}

   public User(String cardCode, String firstName, String lastName, String group,
               String role, String base16photo, String birthDate) {

      this.cardCode = cardCode;
      this.firstName = firstName;
      this.lastName = lastName;
      this.group = group;
      this.role = role;
      this.base16photo = base16photo;
      this.birthDate = birthDate;
   }

   public String getCardCode() {
      return cardCode;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getGroup() {
      return group;
   }

   public void setGroup(String group) {
      this.group = group;
   }

   public String getRole() {
      return role;
   }

   public void setRole(String role) {
      this.role = role;
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
      return getFirstName() + " " + getLastName();
   }

   @Override
   public String getId() {
      return cardCode;
   }

   @Override
   public String toString() {
      return "cardCode='" + cardCode + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", group='" + group + '\'' +
            ", role='" + role + '\'' +
            ", birthDate='" + birthDate + '\'' +
            ", photoBase16='" + (StringUtils.isBlank(base16photo) ? "absent" : "present") + '\'';
   }
}
