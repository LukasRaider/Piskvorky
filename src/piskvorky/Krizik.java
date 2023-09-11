package piskvorky;

import java.awt.*;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Rastislav Boroš
 */
public class Krizik implements Runnable {
    int x1,x2,y1,y2; // koordinaty pre ciarku 1
    int u1,u2,v1,v2; // koordinaty pre ciarku 2
    Mriezka mr;
    Semaphore sem;
    int c, d;
            
    public Krizik(Mriezka m, Semaphore s, int c, int d){
        mr = m;
        sem = s;
        this.c = c;
        this.d = d;
        new Thread(this).start();       
      }
    
    @Override
    public void run(){
       nastav(c,d); 
    }
    
    private void setX(int a, int b){
        x1 = a;
        x2 = b;
    }
    
    private void setY(int a, int b){
        y1 = a;
        y2 = b;
    }
    
    private void setU(int a, int b){
        u1 = a;
        u2 = b;
    }
    
    private void setV(int a, int b){
        v1 = a;
        v2 = b;
    }

    private void nastav(int c, int d){
        switch(c){
                    case 0:
                        setX(8,32);
                        setU(32,8);               
                        break;
                    case 1:
                        setX(48,72);
                        setU(72,48);           
                        break;
                    case 2:
                        setX(88,112);
                        setU(112,88);            
                        break;
                    case 3:
                        setX(128,152);
                        setU(152,128);           
                        break;
                    case 4:
                        setX(168,192);
                        setU(192,168);           
                        break;
                    case 5:
                        setX(208,232);
                        setU(232,208);           
                        break;
                    case 6:
                        setX(248,272);
                        setU(272,248);            
                        break;
                    case 7:
                        setX(288,312);
                        setU(312,288);       
                        break;
                    case 8:
                        setX(328,352);
                        setU(352,328);              
                        break;
                    case 9:
                        setX(368,392);
                        setU(392,368);             
                        break;               
                }
         
                switch(d){
                    case 0:
                        setY(8,32);
                        setV(8,32);             
                        break;
                    case 1:
                        setY(48,72);
                        setV(48,72);             
                        break;
                    case 2:
                        setY(88,112);
                        setV(88,112);      
                        break;
                    case 3:
                        setY(128,152);
                        setV(128,152);         
                        break;
                    case 4:
                        setY(168,192);
                        setV(168,192); 
                        break;
                    case 5:
                        setY(208,232);
                        setV(208,232);       
                        break;
                    case 6:
                        setY(248,272);
                        setV(248,272);              
                        break;
                    case 7:
                        setY(288,312);
                        setV(288,312);           
                        break;
                    case 8:
                        setY(328,352);
                        setV(328,352);    
                        break;
                    case 9:
                        setY(368,392);
                        setV(368,392);              
                        break;               
                }
                
                try {
                    sem.acquire();
                    mr.repaint((int)mr.pole[c][d].x, (int)mr.pole[c][d].y, 40, 40);
                   // mr.repaint(); aj toto funguje, ale repaint(a,b,c,d) je rychlejsie
                    mr.pole[c][d].maKrizik = true;
                    mr.pole[c][d].obsadeny = true;                   
                    sem.release();
                }
                catch(InterruptedException e){}       
    }
    
    public void nakresliKrizik(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(255,51,153));    
        g2.drawLine(x1, y1, x2, y2);
        g2.drawLine(u1, v1, u2, v2);       
    } 
}