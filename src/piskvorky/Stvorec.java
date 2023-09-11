package piskvorky;

import java.awt.geom.Rectangle2D;

/**
 *
 * @author Rastislav Boroš
 */
public class Stvorec extends Rectangle2D.Double {

  boolean maKrizik;
  boolean maKruh;
  boolean obsadeny;
  
  public Stvorec(int a, int b){
      this.x = a;
      this.y = b;
      this.width = 40;
      this.height = 40;
      maKrizik = false;
      maKruh = false;
      obsadeny = false;
  }
}
