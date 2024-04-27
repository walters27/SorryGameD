package edu.up.cs301.sorry;

import android.graphics.Color;

import java.util.Random;

// @authors Quince Pham, Kira Kunitake, Annalise Walkers, Corwin Carr
public class SorryPawn {
   public int color;
   boolean isHome;
   boolean isInStart;
   public int location;
   private int imageResourceId;
   // x position of pawn
   public float x;
   // y position of pawn
   public float y;

   public SorryPawn(SorryPawn s) {
      if (s != null) {
         this.color = s.color;
         this.isHome = s.isHome;
         this.isInStart = s.isInStart;
         this.location = s.location;
         this.imageResourceId = s.imageResourceId;
         this.x = s.x;
         this.y = s.y;
      }
   }

   public SorryPawn(int color, int imageResourceId) {
      this.color = color;
      this.isHome = false;
      this.isInStart = true;
      this.location = getStartBoxLocation(color);
      this.imageResourceId = imageResourceId;
   }

   private int getStartBoxLocation(int color) {
      switch (color) {
         case Color.RED:
            return 20; // Assuming red's start box locations are 20, 34, 35, 36
         case Color.BLUE:
            return 58; // Assuming blue's start box locations are 58, 73, 88, 74
         case Color.YELLOW:
            return 191; // Assuming yellow's start box locations are 191, 192, 206, 207
         case Color.GREEN:
            return 138; // Assuming green's start box locations are 138, 153, 168, 152
         default:
            return 1;
      }
   }

   public int getImageResourceId() {
      return imageResourceId;
   }

}