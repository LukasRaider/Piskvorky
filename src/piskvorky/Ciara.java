package piskvorky;

import java.awt.*;
/**
 *
 * @author Rastislav Boroš
 */
public class Ciara {
    int x1,x2,y1,y2;
    
    public Ciara(int x1, int y1, int x2, int y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void nakresliCiaru(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(6f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(102,204,0));
        g2.drawLine(x1, y1, x2, y2);
    }
}
