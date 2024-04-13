package edu.up.cs301.sorry;

// @authors Quince Pham, Kira Kunitake, Annalise Walkers, Corwin Carr
public class SorryPawn {
   private int color;
   boolean isHome;
   boolean isInStart;
   public int location;
   private int imageResourceId;

   public SorryPawn(SorryPawn s) {
      this.color = s.color;
      this.isHome = s.isHome;
      this.isInStart = s.isInStart;
      this.location = s.location;
      this.imageResourceId = s.imageResourceId;
   }

   public SorryPawn(int color, int imageResourceId) {
      this.color = color;
      this.isHome = true;
      this.isInStart = true;
      this.location = 1;
      this.imageResourceId = imageResourceId;
   }


   public int getImageResourceId() {
      return imageResourceId;
   }
}