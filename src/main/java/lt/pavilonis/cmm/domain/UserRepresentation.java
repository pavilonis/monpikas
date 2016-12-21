package lt.pavilonis.cmm.domain;

import javax.validation.constraints.NotNull;

public class UserRepresentation {

   private String cardCode;

   @NotNull
   private String firstName;

   @NotNull
   private String lastName;

   private String group;

   private String role;

   private String birthDate;

   private String base16photo;

   public UserRepresentation() {/**/}

   public UserRepresentation(String cardCode, String firstName, String lastName, String group,
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
}
