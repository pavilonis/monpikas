package lt.pavilonis.monpikas.server.dto;

import java.io.Serializable;

public class ClientPupilDto implements Serializable {

   private static final long serialVersionUID = 1L;
   private String cardId;
   private String name;
   private boolean dinnerPermitted;
   private boolean hadDinnerToday;

   public ClientPupilDto() {
   }

   public ClientPupilDto(String cardId, String name, boolean dinnerPermitted, Boolean hadDinnerToday) {
      this.cardId = cardId;
      this.name = name;
      this.dinnerPermitted = dinnerPermitted;
      this.hadDinnerToday = hadDinnerToday;
   }

   public String getCardId() {
      return cardId;
   }

   public void setCardId(String cardId) {
      this.cardId = cardId;
   }

   public Boolean getHadDinnerToday() {
      return hadDinnerToday;
   }

   public void setHadDinnerToday(Boolean hadDinnerToday) {
      this.hadDinnerToday = hadDinnerToday;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isDinnerPermitted() {
      return dinnerPermitted;
   }

   public void setDinnerPermitted(boolean dinnerPermitted) {
      this.dinnerPermitted = dinnerPermitted;
   }

   public boolean isHadDinnerToday() {
      return hadDinnerToday;
   }

   public void setHadDinnerToday(boolean hadDinnerToday) {
      this.hadDinnerToday = hadDinnerToday;
   }
}
