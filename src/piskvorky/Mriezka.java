package piskvorky;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Semaphore;
import javax.sound.sampled.*;
import javax.swing.JPanel;

/**
 *
 * @author Rastislav Boroš
 */
public class Mriezka extends JPanel{
    Stvorec[][] pole;
    Semaphore sem;
    ArrayList<Krizik> nakresleneKriziky;
    ArrayList<Kruh> nakresleneKruhy;
    ArrayList<Ciara> nakresleneCiary;
    ArrayList<CiaraP> nakresleneCiaryP;
    AudioInputStream ais;
    Clip zvuk1, zvuk2; // zvuk1 - ked hrac ziska piskvorku; zvuk2 ked pocitac ziska piskvorku
    int c,d;
    public static int counter; // na sledovanie poctu obsadenych poli; 100 obsadenych == koniec hry
    public static int playerScore;
    public static int computerScore;
    Piskvorky pisk;
       
    public Mriezka(Semaphore s, Piskvorky p){
        counter = 0;
        playerScore = 0;
        computerScore = 0;
        sem = s;    
        pisk = p;
        nakresleneKriziky = new ArrayList<>();
        nakresleneKruhy = new ArrayList<>();
        nakresleneCiary = new ArrayList<>();
        nakresleneCiaryP = new ArrayList<>();
        int coordX = 0;
        int coordY = 0;
        
        pole = new Stvorec[10][10];      
        // tento cyklus naplni pole objektami Stvorec
        for(int i=0; i < 10; i++) {
           for(int j=0; j < 10; j++){
               pole[i][j] = new Stvorec(coordX, coordY);
               coordY +=40;              
           }
           coordY = 0;
           coordX +=40;         
       }

        setPreferredSize(new Dimension(401,401));
        setBackground(Color.white);
        setAlignmentX(Component.CENTER_ALIGNMENT); // potrebne pre BoxLayout, ktory je na JFrame okno - vid trieda Piskvorky
        setMaximumSize(new Dimension(401,401));
      //  this.setBorder(BorderFactory.createLineBorder(Color.yellow, 5));
        
        addMouseListener(new MouseAdapter(){
           @Override
           public void mouseClicked(MouseEvent me){ 
               c = me.getX()/40;
               d = me.getY()/40;
                
              if(pole[c][d].obsadeny == true)
                    Piskvorky.oznam.setText("This cell is occupied");
              else {                     
                    urobKrizik(c, d);
                    // cakat - treba kvoli odovzdaniu semaphore permitu medzi krizikom a kruhom, inak sa obcas stalo, ze sa kruh aj krizik nakreslil na rovnake miesto
                    try {
                        Thread.sleep(250);
                    }
                    catch(InterruptedException e){}  
                    
                    urobKruh();                  
                   }
              
            if(counter==100) {
                Piskvorky.oznam.setText("NO MORE MOVES POSSIBLE. END OF GAME");
                pisk.koniec();
            }
          }          
       });
    }
    
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        
        g2.setStroke(new BasicStroke(4f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
        for(int i=0; i < 10; i++)
             for(int j=0; j < 10; j++) {
                 g2.draw(pole[i][j]);              
             }

        Iterator<Krizik> Kitr = nakresleneKriziky.iterator();
        while(Kitr.hasNext())         
            Kitr.next().nakresliKrizik(g);

        Iterator<Kruh> Kitr2 = nakresleneKruhy.iterator();
        while(Kitr2.hasNext())
            Kitr2.next().nakresliKruh(g);  
        
        Iterator<Ciara> Citr = nakresleneCiary.iterator();
        while(Citr.hasNext())
            Citr.next().nakresliCiaru(g);
        
        Iterator<CiaraP> Pitr = nakresleneCiaryP.iterator();
        while(Pitr.hasNext())
            Pitr.next().nakresliCiaru(g);
        
        vyhodnotKriziky(); 
        vyhodnotKruhy();
    } 
        
    private void urobKrizik(int c, int d){
        counter++;
        nakresleneKriziky.add(new Krizik(this, sem, c, d));
    }
    
    private void urobKruh(){
        counter++;
        nakresleneKruhy.add(new Kruh(this, sem));
    }
    
    private void vyhodnotKriziky(){
        int r = 0;
        int s;   
        
        // vyhodnot stlpce
        for(int n=1; n < 11; n++) {           
            s = 0;
            for(int k = 1; k < 7; k++) {            
                if(pole[r][s].maKrizik == true && pole[r][s+1].maKrizik == true && pole[r][s+2].maKrizik == true
                   && pole[r][s+3].maKrizik == true && pole[r][s+4].maKrizik == true){
                    Piskvorky.oznam.setText(Piskvorky.playerName + ": VERTICAL");                  
                    piskvorkaHrac(); // zahra zvuk
                    playerScore++;
                    Piskvorky.Pscore.setText("  " + Integer.toString(playerScore));
                    nakresleneCiary.add(new Ciara((int)pole[r][s].x+20, (int)pole[r][s].y+20, (int)pole[r][s+4].x+20, (int)pole[r][s+4].y+20));
                    
                    pole[r][s].maKrizik = false; // nastavenie na false zabezpeci, ze sa nezarata piskvorka dvakrat na tych istych stvorcoch
                    pole[r][s+1].maKrizik = false;
                    pole[r][s+2].maKrizik = false;
                    pole[r][s+3].maKrizik = false;
                    pole[r][s+4].maKrizik = false;
                    
                    repaint();
                }
                s++;
            }
            r++;
        }
       // vyhodnot riadky
       r = 0;      
       for(int n=1; n < 11; n++) {
           s=0;
           for(int k=1; k <7; k++){
               if(pole[s][r].maKrizik == true && pole[s+1][r].maKrizik == true && pole[s+2][r].maKrizik == true
                  && pole[s+3][r].maKrizik == true && pole[s+4][r].maKrizik == true){
                   Piskvorky.oznam.setText(Piskvorky.playerName + ": HORIZONTAL");
                   piskvorkaHrac(); // zahra zvuk
                   playerScore++;
                   Piskvorky.Pscore.setText("  " + Integer.toString(playerScore));
                   nakresleneCiary.add(new Ciara((int)pole[s][r].x+20,(int)pole[s][r].y+20,(int)pole[s+4][r].x+20,(int)pole[s+4][r].y+20));
                   
                   pole[s][r].maKrizik = false;
                   pole[s+1][r].maKrizik = false;
                   pole[s+2][r].maKrizik = false;
                   pole[s+3][r].maKrizik = false;
                   pole[s+4][r].maKrizik = false;
                  
                   repaint();
               }
               s++;
           }
           r++;
       }
       
       // vyhodnot uhlopriecne - severozapad - juhovychod
       r=0;
       for(int n=1; n < 7; n++){
           s=0;
           for(int k=1; k < 7; k++){
               if(pole[r][s].maKrizik == true && pole[r+1][s+1].maKrizik == true && pole[r+2][s+2].maKrizik == true
                  && pole[r+3][s+3].maKrizik == true && pole[r+4][s+4].maKrizik == true) {
                   Piskvorky.oznam.setText(Piskvorky.playerName + ": DIAGONAL");
                   piskvorkaHrac(); // zahra zvuk
                   playerScore++;
                   Piskvorky.Pscore.setText("  " + Integer.toString(playerScore));
                   nakresleneCiary.add(new Ciara((int)pole[r][s].x+20,(int)pole[r][s].y+20,(int)pole[r+4][s+4].x+20,(int)pole[r+4][s+4].y+20));
                   
                   pole[r][s].maKrizik = false;
                   pole[r+1][s+1].maKrizik = false;
                   pole[r+2][s+2].maKrizik = false;
                   pole[r+3][s+3].maKrizik = false;
                   pole[r+4][s+4].maKrizik = false;

                   repaint();
               }
               s++;
           }
           r++;
       }
       
       // vyhodnot uhlopriecne - severovychod - juhozapad
       r = 9;
       for(int n=1; n < 7; n++) {
           s = 0;
           for(int k=1; k < 7; k++) {
               if(pole[r][s].maKrizik == true && pole[r-1][s+1].maKrizik == true && pole[r-2][s+2].maKrizik == true
                  && pole[r-3][s+3].maKrizik == true && pole[r-4][s+4].maKrizik == true) {
                   Piskvorky.oznam.setText(Piskvorky.playerName + ": DIAGONAL");         
                   piskvorkaHrac(); // zahra zvuk
                   playerScore++;
                   Piskvorky.Pscore.setText("  " + Integer.toString(playerScore));
                   nakresleneCiary.add(new Ciara((int)pole[r][s].x+20, (int)pole[r][s].y+20, (int)pole[r-4][s+4].x+20, (int)pole[r-4][s+4].y+20));
                   
                   pole[r][s].maKrizik = false;
                   pole[r-1][s+1].maKrizik = false;
                   pole[r-2][s+2].maKrizik = false;
                   pole[r-3][s+3].maKrizik = false;
                   pole[r-4][s+4].maKrizik = false;

                   repaint();
               }
               s++;
           }
           r--;
       }      
    }
    
    private void vyhodnotKruhy(){
        int r = 0;
        int s;   
        
        // vyhodnot stlpce
        for(int n=1; n < 11; n++) {           
            s = 0;
            for(int k = 1; k < 7; k++) {            
                if(pole[r][s].maKruh == true && pole[r][s+1].maKruh == true && pole[r][s+2].maKruh == true
                   && pole[r][s+3].maKruh == true && pole[r][s+4].maKruh == true){
                    Piskvorky.oznam.setText("Computer: VERTICAL");
                    piskvorkaPocitac(); // zahra zvuk
                    computerScore++;
                    Piskvorky.Cscore.setText(Integer.toString(computerScore) + "  ");
                    nakresleneCiaryP.add(new CiaraP((int)pole[r][s].x+20, (int)pole[r][s].y+20, (int)pole[r][s+4].x+20, (int)pole[r][s+4].y+20));
                    pole[r][s].maKruh = false; // nastavenie na false zabezpeci, ze sa nezarata piskvorka dvakrat na tych istych stvorcoch
                    pole[r][s+1].maKruh = false;
                    pole[r][s+2].maKruh = false;
                    pole[r][s+3].maKruh = false;
                    pole[r][s+4].maKruh = false;
                    
                    repaint();
                }
                s++;
            }
            r++;
        }
       // vyhodnot riadky
       r = 0;      
       for(int n=1; n < 11; n++) {
           s=0;
           for(int k=1; k <7; k++){
               if(pole[s][r].maKruh == true && pole[s+1][r].maKruh == true && pole[s+2][r].maKruh == true
                  && pole[s+3][r].maKruh == true && pole[s+4][r].maKruh == true){
                   Piskvorky.oznam.setText("Computer: HORIZONTAL");
                   piskvorkaPocitac(); // zahra zvuk
                   computerScore++;
                   Piskvorky.Cscore.setText(Integer.toString(computerScore) + "  ");
                   nakresleneCiaryP.add(new CiaraP((int)pole[s][r].x+20,(int)pole[s][r].y+20,(int)pole[s+4][r].x+20,(int)pole[s+4][r].y+20));
                   pole[s][r].maKruh = false;
                   pole[s+1][r].maKruh = false;
                   pole[s+2][r].maKruh = false;
                   pole[s+3][r].maKruh = false;
                   pole[s+4][r].maKruh = false;
                   
                   repaint();

               }
               s++;
           }
           r++;
       }
       
       // vyhodnot uhlopriecne - severozapad - juhovychod
       r=0;
       for(int n=1; n < 7; n++){
           s=0;
           for(int k=1; k < 7; k++){
               if(pole[r][s].maKruh == true && pole[r+1][s+1].maKruh == true && pole[r+2][s+2].maKruh == true
                  && pole[r+3][s+3].maKruh == true && pole[r+4][s+4].maKruh == true) {
                   Piskvorky.oznam.setText("Computer: DIAGONAL");               
                   piskvorkaPocitac(); // zahra zvuk
                   computerScore++;
                   Piskvorky.Cscore.setText(Integer.toString(computerScore) + "  ");
                   nakresleneCiaryP.add(new CiaraP((int)pole[r][s].x+20,(int)pole[r][s].y+20,(int)pole[r+4][s+4].x+20,(int)pole[r+4][s+4].y+20));
                   pole[r][s].maKruh = false;
                   pole[r+1][s+1].maKruh = false;
                   pole[r+2][s+2].maKruh = false;
                   pole[r+3][s+3].maKruh = false;
                   pole[r+4][s+4].maKruh = false;
                   
                   repaint();
               }
               s++;
           }
           r++;
       }
       
       // vyhodnot uhlopriecne - severovychod - juhozapad
       r = 9;
       for(int n=1; n < 7; n++) {
           s = 0;
           for(int k=1; k < 7; k++) {
               if(pole[r][s].maKruh == true && pole[r-1][s+1].maKruh == true && pole[r-2][s+2].maKruh == true
                  && pole[r-3][s+3].maKruh == true && pole[r-4][s+4].maKruh == true) {
                   Piskvorky.oznam.setText("Computer: DIAGONAL");
                   piskvorkaPocitac(); // zahra zvuk
                   computerScore++;
                   Piskvorky.Cscore.setText(Integer.toString(computerScore) + "  ");
                   nakresleneCiaryP.add(new CiaraP((int)pole[r][s].x+20, (int)pole[r][s].y+20, (int)pole[r-4][s+4].x+20, (int)pole[r-4][s+4].y+20));
                   pole[r][s].maKruh = false;
                   pole[r-1][s+1].maKruh = false;
                   pole[r-2][s+2].maKruh = false;
                   pole[r-3][s+3].maKruh = false;
                   pole[r-4][s+4].maKruh = false;
                   
                   repaint();

               }
               s++;
           }
           r--;
       }
    }
    
    private void piskvorkaHrac(){
        try {
           ais = AudioSystem.getAudioInputStream(getClass().getResource("/resources/zvuk1.wav")); 
           zvuk1 = AudioSystem.getClip();
           zvuk1.open(ais);
           zvuk1.start();
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){}
    }
    
    private void piskvorkaPocitac(){
        try {
           ais = AudioSystem.getAudioInputStream(getClass().getResource("/resources/zvuk2.wav")); 
           zvuk2 = AudioSystem.getClip();
           zvuk2.open(ais);
           zvuk2.start();
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){}
    }
    
    
}