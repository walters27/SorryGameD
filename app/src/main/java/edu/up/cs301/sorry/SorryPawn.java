package edu.up.cs301.sorry;
// @authors Quince Pham, Kira Kunitake, Annalise Walkers, Corwin Carr
public class SorryPawn {

   private int color;

   private boolean isHome;

   private boolean isInStart;

   private int location;

public SorryPawn(SorryPawn s) {
   this.color = s.color;
   this.isHome = s.isHome;
   this.isInStart = s.isInStart;
   this.location = s.location;
}
public SorryPawn()
{
   color = 50;
   isHome = true;
   isInStart = true;
   location = 50;
}



}
