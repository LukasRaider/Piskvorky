package piskvorky;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Rastislav Boroš
 */
public class Kruh implements Runnable {
    int x;
    int y;
    int r;
    Mriezka mr;
    Semaphore sem;  
    Random rand;
    private int o, p;
    private int volneX3, volneY3, volneX3Zp, volneY3Zp;
    private int volneX4, volneY4, volneX4Zp, volneY4Zp;
    private int volneXpr, volneYpr;
    private int mojeX1, mojeY1, mojeX2, mojeY2, mojeX3, mojeY3, mojeX4, mojeY4;
    private int mojeX3Zp, mojeY3Zp, mojeX4Zp, mojeY4Zp;
    private boolean scenar3 = false;
    private boolean scenar3Zp = false;
    private boolean scenar4 = false;
    private boolean scenar4Zp = false;
    private boolean scenarPr = false;
    private boolean scenarMoje1 = false;
    private boolean scenarMoje2 = false;
    private boolean scenarMoje3 = false;
    private boolean scenarMoje3Zp = false;
    private boolean scenarMoje4 = false;
    private boolean scenarMoje4Zp = false;
            
    public Kruh(Mriezka m, Semaphore s){
        mr = m; 
        sem = s;
        new Thread(this).start();
    }
    
    @Override
    public void run(){
        skenujMriezku();
    }
    
    private void setX(int a){
        x = a;
    }
    
    private void setY(int b){
        y = b;
    }
    
    private void setR(int c){
        r = c;
    }
        
    private void skenujMriezku(){
        skenuj4();
        skenuj4Zprava();
        skenujMoje4();
        skenujMoje4Zp();
        skenujMoje3();
        skenujMoje3Zp();
        skenujMoje2();
        skenujMoje1();
        skenujPrerusovanuPiskv();
        skenuj3();
        skenuj3Zprava();

        if(scenar4 == true){
            nastavSuradnice(volneX4, volneY4);
        }       
        else if(scenar4Zp == true) {
            nastavSuradnice(volneX4Zp, volneY4Zp);
            }
        else if (scenarMoje4 == true){
            nastavSuradnice(mojeX4, mojeY4);
        }
        else if (scenarMoje4Zp == true){
            nastavSuradnice(mojeX4Zp, mojeY4Zp);
        }
        else if (scenar3 == true){           
            nastavSuradnice(volneX3, volneY3);
            }
        else if(scenar3Zp == true){
            nastavSuradnice(volneX3Zp, volneY3Zp);
            }
        else if (scenarPr == true){
            nastavSuradnice(volneXpr, volneYpr);
        }
        else if (scenarMoje3 == true){
            nastavSuradnice(mojeX3, mojeY3);
        }        
        else if (scenarMoje3Zp == true){
            nastavSuradnice(mojeX3Zp, mojeY3Zp);
        }
        else if (scenarMoje2 == true){
            nastavSuradnice(mojeX2, mojeY2);
        }
        else if (scenarMoje1 == true){
            nastavSuradnice(mojeX1, mojeY1);
        }
        else
            scenar0(); // kruh na nahodnom volnom stvorci
    }
    
    private void skenuj3(){
        int t = 0;
        int s;
               
        // vyhodnot stlpce
        for(int m=1; m < 11; m++) {           
            s = 0;
            for(int k = 1; k < 7; k++) {            
                if(mr.pole[t][s].maKrizik == true && mr.pole[t][s+1].maKrizik == true && mr.pole[t][s+2].maKrizik == true
                   && mr.pole[t][s+3].obsadeny == false) {
                    volneX3 = t;
                    volneY3 = s+3;
                    scenar3 = true;
                }
                s++;
            }
            t++;
        }
               
        // vyhodnot riadky
       t = 0;      
       for(int m=1; m < 11; m++) {
           s=0;
           for(int k=1; k <7; k++){
               if(mr.pole[s][t].maKrizik == true && mr.pole[s+1][t].maKrizik == true && mr.pole[s+2][t].maKrizik == true
                  && mr.pole[s+3][t].obsadeny == false){
                   volneX3 = s+3;
                   volneY3 = t;
                   scenar3 = true;
               }
               s++;
           }
           t++;
       }
      
       // vyhodnot uhlopriecne - severozapad - juhovychod
       t=0;
       for(int m=1; m < 7; m++){
           s=0;
           for(int k=1; k < 7; k++){
               if(mr.pole[t][s].maKrizik == true && mr.pole[t+1][s+1].maKrizik == true && mr.pole[t+2][s+2].maKrizik == true
                  && mr.pole[t+3][s+3].obsadeny == false) {
                   volneX3 = t+3;
                   volneY3 = s+3;
                   scenar3 = true;
               }
               s++;
           }
           t++;
       }
      
       // vyhodnot uhlopriecne - severovychod - juhozapad
       t = 9;
       for(int m=1; m < 7; m++) {
           s = 0;
           for(int k=1; k < 7; k++) {
               if(mr.pole[t][s].maKrizik == true && mr.pole[t-1][s+1].maKrizik == true && mr.pole[t-2][s+2].maKrizik == true
                  && mr.pole[t-3][s+3].obsadeny == false) {
                   volneX3 = t-3;
                   volneY3 = s+3;
                   scenar3 = true;
               }
               s++;
           }
           t--;
       }
    }
    
    private void skenuj3Zprava(){
        int t = 0;
        int s;
               
        // vyhodnot stlpce
        for(int m=1; m < 11; m++) {           
            s = 9;
            for(int k = 1; k < 7; k++) {            
                if(mr.pole[t][s].maKrizik == true && mr.pole[t][s-1].maKrizik == true && mr.pole[t][s-2].maKrizik == true
                   && mr.pole[t][s-3].obsadeny == false) {
                    volneX3Zp = t;
                    volneY3Zp = s-3;
                    scenar3Zp = true;
                }
                s--;
            }
            t++;
        }
        
        // vyhodnot riadky
       t = 0;      
       for(int m=1; m < 11; m++) {
           s=9;
           for(int k=1; k < 7; k++){
               if(mr.pole[s][t].maKrizik == true && mr.pole[s-1][t].maKrizik == true && mr.pole[s-2][t].maKrizik == true
                  && mr.pole[s-3][t].obsadeny == false){
                   volneX3Zp = s-3;
                   volneY3Zp = t;
                   scenar3Zp = true;
               }
               s--;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - juhozapad - severovychod
       t=0;
       for(int m=1; m < 7; m++){
           s=9;
           for(int k=1; k < 7; k++){
               if(mr.pole[t][s].maKrizik == true && mr.pole[t+1][s-1].maKrizik == true && mr.pole[t+2][s-2].maKrizik == true
                  && mr.pole[t+3][s-3].obsadeny == false) {
                   volneX3Zp = t+3;
                   volneY3Zp = s-3;
                   scenar3Zp = true;
               }
               s--;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - juhovychod - severozapad
       t = 9;
       for(int m=1; m < 7; m++) {
           s = 9;
           for(int k=1; k < 7; k++) {
               if(mr.pole[t][s].maKrizik == true && mr.pole[t-1][s-1].maKrizik == true && mr.pole[t-2][s-2].maKrizik == true
                  && mr.pole[t-3][s-3].obsadeny == false) {
                   volneX3Zp = t-3;
                   volneY3Zp = s-3;
                   scenar3Zp = true;
               }
               s--;
           }
           t--;
       }              
    }
    
    private void skenuj4(){
      int t = 0;
      int s;
               
        // vyhodnot stlpce
        for(int m=1; m < 11; m++) {           
            s = 0;
            for(int k = 1; k < 7; k++) {            
                if(mr.pole[t][s].maKrizik == true && mr.pole[t][s+1].maKrizik == true && mr.pole[t][s+2].maKrizik == true
                   && mr.pole[t][s+3].maKrizik == true && mr.pole[t][s+4].obsadeny == false) {
                    volneX4 = t;
                    volneY4 = s+4;
                    scenar4 = true;
                }
                s++;
            }
            t++;
        }
        
        // vyhodnot riadky
       t = 0;      
       for(int m=1; m < 11; m++) {
           s=0;
           for(int k=1; k <7; k++){
               if(mr.pole[s][t].maKrizik == true && mr.pole[s+1][t].maKrizik == true && mr.pole[s+2][t].maKrizik == true
                  && mr.pole[s+3][t].maKrizik == true && mr.pole[s+4][t].obsadeny == false){
                   volneX4 = s+4;
                   volneY4 = t;
                   scenar4 = true;
               }
               s++;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - severozapad - juhovychod
       t=0;
       for(int m=1; m < 7; m++){
           s=0;
           for(int k=1; k < 7; k++){
               if(mr.pole[t][s].maKrizik == true && mr.pole[t+1][s+1].maKrizik == true && mr.pole[t+2][s+2].maKrizik == true
                  && mr.pole[t+3][s+3].maKrizik == true && mr.pole[t+4][s+4].obsadeny == false) {
                   volneX4 = t+4;
                   volneY4 = s+4;
                   scenar4 = true;
               }
               s++;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - severovychod - juhozapad
       t = 9;
       for(int m=1; m < 7; m++) {
           s = 0;
           for(int k=1; k < 7; k++) {
               if(mr.pole[t][s].maKrizik == true && mr.pole[t-1][s+1].maKrizik == true && mr.pole[t-2][s+2].maKrizik == true
                  && mr.pole[t-3][s+3].maKrizik == true && mr.pole[t-4][s+4].obsadeny == false) {
                   volneX4 = t-4;
                   volneY4 = s+4;
                   scenar4 = true;
               }
               s++;
           }
           t--;
       }  
    }
    
    private void skenujPrerusovanuPiskv(){
      int t = 0;
      int s;
               
        // vyhodnot stlpce - druhy stvorec volny
        for(int m=1; m < 11; m++) {           
            s = 0;
            for(int k = 1; k < 7; k++) {            
                if(mr.pole[t][s].maKrizik == true && mr.pole[t][s+1].obsadeny == false && mr.pole[t][s+2].maKrizik == true
                   && mr.pole[t][s+3].maKrizik == true) {
                    volneXpr = t;
                    volneYpr = s+1;
                    scenarPr = true;
                }
                s++;
            }
            t++;
        }
        
        t = 0;
        // vyhodnot stlpce - treti stvorec volny
        for(int m=1; m < 11; m++) {           
            s = 0;
            for(int k = 1; k < 7; k++) {            
                if(mr.pole[t][s].maKrizik == true && mr.pole[t][s+1].maKrizik == true && mr.pole[t][s+2].obsadeny == false
                   && mr.pole[t][s+3].maKrizik == true) {
                    volneXpr = t;
                    volneYpr = s+2;
                    scenarPr = true;
                }
                s++;
            }
            t++;
        }
        
        // vyhodnot riadky - druhy stvorec volny
       t = 0;      
       for(int m=1; m < 11; m++) {
           s=0;
           for(int k=1; k <7; k++){
               if(mr.pole[s][t].maKrizik == true && mr.pole[s+1][t].obsadeny == false && mr.pole[s+2][t].maKrizik == true
                  && mr.pole[s+3][t].maKrizik == true){
                   volneXpr = s+1;
                   volneYpr = t;
                   scenarPr = true;
               }
               s++;
           }
           t++;
       }
       
       // vyhodnot riadky - treti stvorec volny
       t = 0;
       for(int m=1; m < 11; m++) {
           s=0;
           for(int k=1; k <7; k++){
               if(mr.pole[s][t].maKrizik == true && mr.pole[s+1][t].maKrizik == true && mr.pole[s+2][t].obsadeny == false
                  && mr.pole[s+3][t].maKrizik == true){
                   volneXpr = s+2;
                   volneYpr = t;
                   scenarPr = true;
               }
               s++;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - severozapad - juhovychod - druhy stvorec volny
       t = 0;
       for(int m=1; m < 7; m++){
           s=0;
           for(int k=1; k < 7; k++){
               if(mr.pole[t][s].maKrizik == true && mr.pole[t+1][s+1].obsadeny == false && mr.pole[t+2][s+2].maKrizik == true
                  && mr.pole[t+3][s+3].maKrizik == true) {
                   volneXpr = t+1;
                   volneYpr = s+1;
                   scenarPr = true;
               }
               s++;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - severozapad - juhovychod - treti stvorec volny
       t = 0;
       for(int m=1; m < 7; m++){
           s=0;
           for(int k=1; k < 7; k++){
               if(mr.pole[t][s].maKrizik == true && mr.pole[t+1][s+1].maKrizik == true && mr.pole[t+2][s+2].obsadeny == false
                  && mr.pole[t+3][s+3].maKrizik == true) {
                   volneXpr = t+2;
                   volneYpr = s+2;
                   scenarPr = true;
               }
               s++;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - severovychod - juhozapad - druhy stvorec volny
       t = 9;
       for(int m=1; m < 7; m++) {
           s = 0;
           for(int k=1; k < 7; k++) {
               if(mr.pole[t][s].maKrizik == true && mr.pole[t-1][s+1].obsadeny == false && mr.pole[t-2][s+2].maKrizik == true
                  && mr.pole[t-3][s+3].maKrizik == true) {
                   volneXpr = t-1;
                   volneYpr = s+1;
                   scenarPr = true;
               }
               s++;
           }
           t--;
       } 
       
       // vyhodnot uhlopriecne - severovychod - juhozapad - treti stvorec volny
       t = 9;
       for(int m=1; m < 7; m++) {
           s = 0;
           for(int k=1; k < 7; k++) {
               if(mr.pole[t][s].maKrizik == true && mr.pole[t-1][s+1].maKrizik == true && mr.pole[t-2][s+2].obsadeny == false
                  && mr.pole[t-3][s+3].maKrizik == true) {
                   volneXpr = t-2;
                   volneYpr = s+2;
                   scenarPr = true;
               }
               s++;
           }
           t--;
       } 
       
    }
    
    private void skenuj4Zprava(){
       int t = 0;
       int s;
               
        // vyhodnot stlpce
        for(int m=1; m < 11; m++) {           
            s = 9;
            for(int k = 1; k < 7; k++) {            
                if(mr.pole[t][s].maKrizik == true && mr.pole[t][s-1].maKrizik == true && mr.pole[t][s-2].maKrizik == true
                   && mr.pole[t][s-3].maKrizik == true && mr.pole[t][s-4].obsadeny == false) {
                    volneX4Zp = t;
                    volneY4Zp = s-4;
                    scenar4Zp = true;
                }
                s--;
            }
            t++;
        }
        
        // vyhodnot riadky
       t = 0;      
       for(int m=1; m < 11; m++) {
           s=9;
           for(int k=1; k < 7; k++){
               if(mr.pole[s][t].maKrizik == true && mr.pole[s-1][t].maKrizik == true && mr.pole[s-2][t].maKrizik == true
                  && mr.pole[s-3][t].maKrizik == true && mr.pole[s-4][t].obsadeny == false){
                   volneX4Zp = s-4;
                   volneY4Zp = t;
                   scenar4Zp = true;
               }
               s--;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - juhozapad - severovychod
       t=0;
       for(int m=1; m < 7; m++){
           s=9;
           for(int k=1; k < 7; k++){
               if(mr.pole[t][s].maKrizik == true && mr.pole[t+1][s-1].maKrizik == true && mr.pole[t+2][s-2].maKrizik == true
                  && mr.pole[t+3][s-3].maKrizik == true && mr.pole[t+4][s-4].obsadeny == false) {
                   volneX4Zp = t+4;
                   volneY4Zp = s-4;
                   scenar4Zp = true;
               }
               s--;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - juhovychod - severozapad
       t = 9;
       for(int m=1; m < 7; m++) {
           s = 9;
           for(int k=1; k < 7; k++) {
               if(mr.pole[t][s].maKrizik == true && mr.pole[t-1][s-1].maKrizik == true && mr.pole[t-2][s-2].maKrizik == true
                  && mr.pole[t-3][s-3].maKrizik == true && mr.pole[t-4][s-4].obsadeny == false) {
                   volneX4Zp = t-4;
                   volneY4Zp = s-4;
                   scenar4Zp = true;
               }
               s--;
           }
           t--;
       }              
    }
    
    private void skenujMoje1(){
      int t = 0;
      int s;
               
        // vyhodnot stlpce
        for(int m=1; m < 11; m++) {           
            s = 0;
            for(int k = 1; k < 7; k++) {            
                if(mr.pole[t][s].maKruh == true && mr.pole[t][s+1].obsadeny == false) {
                    mojeX1 = t;
                    mojeY1 = s+1;
                    scenarMoje1 = true;
                }
                s++;
            }
            t++;
        }
        
        // vyhodnot riadky
       t = 0;      
       for(int m=1; m < 11; m++) {
           s=0;
           for(int k=1; k <7; k++){
               if(mr.pole[s][t].maKruh == true && mr.pole[s+1][t].obsadeny == false){
                   mojeX1 = s+1;
                   mojeY1 = t;
                   scenarMoje1 = true;
               }
               s++;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - severozapad - juhovychod
       t=0;
       for(int m=1; m < 7; m++){
           s=0;
           for(int k=1; k < 7; k++){
               if(mr.pole[t][s].maKruh == true && mr.pole[t+1][s+1].obsadeny == false) {
                   mojeX1 = t+1;
                   mojeY1 = s+1;
                   scenarMoje1 = true;
               }
               s++;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - severovychod - juhozapad
       t = 9;
       for(int m=1; m < 7; m++) {
           s = 0;
           for(int k=1; k < 7; k++) {
               if(mr.pole[t][s].maKruh == true && mr.pole[t-1][s+1].obsadeny == false) {
                   mojeX1 = t-1;
                   mojeY1 = s+1;
                   scenarMoje1 = true;
               }
               s++;
           }
           t--;
       }
    }
    
    private void skenujMoje2(){
      int t = 0;
      int s;
               
        // vyhodnot stlpce
        for(int m=1; m < 11; m++) {           
            s = 0;
            for(int k = 1; k < 7; k++) {            
                if(mr.pole[t][s].maKruh == true && mr.pole[t][s+1].maKruh == true && mr.pole[t][s+2].obsadeny == false) {
                    mojeX2 = t;
                    mojeY2 = s+2;
                    scenarMoje2 = true;
                }
                s++;
            }
            t++;
        }
        
        // vyhodnot riadky
       t = 0;      
       for(int m=1; m < 11; m++) {
           s=0;
           for(int k=1; k <7; k++){
               if(mr.pole[s][t].maKruh == true && mr.pole[s+1][t].maKruh == true && mr.pole[s+2][t].obsadeny == false){
                   mojeX2 = s+2;
                   mojeY2 = t;
                   scenarMoje2 = true;
               }
               s++;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - severozapad - juhovychod
       t=0;
       for(int m=1; m < 7; m++){
           s=0;
           for(int k=1; k < 7; k++){
               if(mr.pole[t][s].maKruh == true && mr.pole[t+1][s+1].maKruh == true && mr.pole[t+2][s+2].obsadeny == false) {
                   mojeX2 = t+2;
                   mojeY2 = s+2;
                   scenarMoje2 = true;
               }
               s++;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - severovychod - juhozapad
       t = 9;
       for(int m=1; m < 7; m++) {
           s = 0;
           for(int k=1; k < 7; k++) {
               if(mr.pole[t][s].maKruh == true && mr.pole[t-1][s+1].maKruh == true && mr.pole[t-2][s+2].obsadeny == false) {
                   mojeX2 = t-2;
                   mojeY2 = s+2;
                   scenarMoje2 = true;
               }
               s++;
           }
           t--;
       }
    }
    
    private void skenujMoje3(){
      int t = 0;
      int s;
               
        // vyhodnot stlpce
        for(int m=1; m < 11; m++) {           
            s = 0;
            for(int k = 1; k < 7; k++) {            
                if(mr.pole[t][s].maKruh == true && mr.pole[t][s+1].maKruh == true && mr.pole[t][s+2].maKruh == true
                   && mr.pole[t][s+3].obsadeny == false) {
                    mojeX3 = t;
                    mojeY3 = s+3;
                    scenarMoje3 = true;
                }
                s++;
            }
            t++;
        }
        
        // vyhodnot riadky
       t = 0;      
       for(int m=1; m < 11; m++) {
           s=0;
           for(int k=1; k <7; k++){
               if(mr.pole[s][t].maKruh == true && mr.pole[s+1][t].maKruh == true && mr.pole[s+2][t].maKruh == true
                  && mr.pole[s+3][t].obsadeny == false){
                   mojeX3 = s+3;
                   mojeY3 = t;
                   scenarMoje3 = true;
               }
               s++;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - severozapad - juhovychod
       t=0;
       for(int m=1; m < 7; m++){
           s=0;
           for(int k=1; k < 7; k++){
               if(mr.pole[t][s].maKruh == true && mr.pole[t+1][s+1].maKruh == true && mr.pole[t+2][s+2].maKruh == true
                  && mr.pole[t+3][s+3].obsadeny == false) {
                   mojeX3 = t+3;
                   mojeY3 = s+3;
                   scenarMoje3 = true;
               }
               s++;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - severovychod - juhozapad
       t = 9;
       for(int m=1; m < 7; m++) {
           s = 0;
           for(int k=1; k < 7; k++) {
               if(mr.pole[t][s].maKruh == true && mr.pole[t-1][s+1].maKruh == true && mr.pole[t-2][s+2].maKruh == true
                  && mr.pole[t-3][s+3].obsadeny == false) {
                   mojeX3 = t-3;
                   mojeY3 = s+3;
                   scenarMoje3 = true;
               }
               s++;
           }
           t--;
       }
    }
    
    private void skenujMoje3Zp(){
      int t = 0;
      int s;
               
        // vyhodnot stlpce
        for(int m=1; m < 11; m++) {           
            s = 9;
            for(int k = 1; k < 7; k++) {            
                if(mr.pole[t][s].maKruh == true && mr.pole[t][s-1].maKruh == true && mr.pole[t][s-2].maKruh == true
                   && mr.pole[t][s-3].obsadeny == false) {
                    mojeX3Zp = t;
                    mojeY3Zp = s-3;
                    scenarMoje3Zp = true;
                }
                s--;
            }
            t++;
        }
        
        // vyhodnot riadky
       t = 0;      
       for(int m=1; m < 11; m++) {
           s=9;
           for(int k=1; k <7; k++){
               if(mr.pole[s][t].maKruh == true && mr.pole[s-1][t].maKruh == true && mr.pole[s-2][t].maKruh == true
                  && mr.pole[s-3][t].obsadeny == false){
                   mojeX3Zp = s-3;
                   mojeY3Zp = t;
                   scenarMoje3Zp = true;
               }
               s--;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - juhozapad - severovychod
       t=0;
       for(int m=1; m < 7; m++){
           s=9;
           for(int k=1; k < 7; k++){
               if(mr.pole[t][s].maKruh == true && mr.pole[t+1][s-1].maKruh == true && mr.pole[t+2][s-2].maKruh == true
                  && mr.pole[t+3][s-3].obsadeny == false) {
                   mojeX3Zp = t+3;
                   mojeY3Zp = s-3;
                   scenarMoje3Zp = true;
               }
               s--;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - juhovychod - severozapad
       t = 9;
       for(int m=1; m < 7; m++) {
           s = 9;
           for(int k=1; k < 7; k++) {
               if(mr.pole[t][s].maKruh == true && mr.pole[t-1][s-1].maKruh == true && mr.pole[t-2][s-2].maKruh == true
                  && mr.pole[t-3][s-3].obsadeny == false) {
                   mojeX3Zp = t-3;
                   mojeY3Zp = s-3;
                   scenarMoje3Zp = true;
               }
               s--;
           }
           t--;
       }
    }
    
    private void skenujMoje4(){
      int t = 0;
      int s;
               
        // vyhodnot stlpce
        for(int m=1; m < 11; m++) {           
            s = 0;
            for(int k = 1; k < 7; k++) {            
                if(mr.pole[t][s].maKruh == true && mr.pole[t][s+1].maKruh == true && mr.pole[t][s+2].maKruh == true
                   && mr.pole[t][s+3].maKruh == true && mr.pole[t][s+4].obsadeny == false) {
                    mojeX4 = t;
                    mojeY4 = s+4;
                    scenarMoje4 = true;
                }
                s++;
            }
            t++;
        }
        
        // vyhodnot riadky
       t = 0;      
       for(int m=1; m < 11; m++) {
           s=0;
           for(int k=1; k <7; k++){
               if(mr.pole[s][t].maKruh == true && mr.pole[s+1][t].maKruh == true && mr.pole[s+2][t].maKruh == true
                  && mr.pole[s+3][t].maKruh == true && mr.pole[s+4][t].obsadeny == false){
                   mojeX4 = s+4;
                   mojeY4 = t;
                   scenarMoje4 = true;
               }
               s++;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - severozapad - juhovychod
       t=0;
       for(int m=1; m < 7; m++){
           s=0;
           for(int k=1; k < 7; k++){
               if(mr.pole[t][s].maKruh == true && mr.pole[t+1][s+1].maKruh == true && mr.pole[t+2][s+2].maKruh == true
                  && mr.pole[t+3][s+3].maKruh == true && mr.pole[t+4][s+4].obsadeny == false) {
                   mojeX4 = t+4;
                   mojeY4 = s+4;
                   scenarMoje4 = true;
               }
               s++;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - severovychod - juhozapad
       t = 9;
       for(int m=1; m < 7; m++) {
           s = 0;
           for(int k=1; k < 7; k++) {
               if(mr.pole[t][s].maKruh == true && mr.pole[t-1][s+1].maKruh == true && mr.pole[t-2][s+2].maKruh == true
                  && mr.pole[t-3][s+3].maKruh == true && mr.pole[t-4][s+4].obsadeny == false) {
                   mojeX4 = t-4;
                   mojeY4 = s+4;
                   scenarMoje4 = true;
               }
               s++;
           }
           t--;
       }
    }
    
    private void skenujMoje4Zp(){
      int t = 0;
      int s;
               
        // vyhodnot stlpce
        for(int m=1; m < 11; m++) {           
            s = 9;
            for(int k = 1; k < 7; k++) {            
                if(mr.pole[t][s].maKruh == true && mr.pole[t][s-1].maKruh == true && mr.pole[t][s-2].maKruh == true
                   && mr.pole[t][s-3].maKruh == true && mr.pole[t][s-4].obsadeny == false) {
                    mojeX4Zp = t;
                    mojeY4Zp = s-4;
                    scenarMoje4Zp = true;
                }
                s--;
            }
            t++;
        }
        
        // vyhodnot riadky
       t = 0;      
       for(int m=1; m < 11; m++) {
           s=9;
           for(int k=1; k <7; k++){
               if(mr.pole[s][t].maKruh == true && mr.pole[s-1][t].maKruh == true && mr.pole[s-2][t].maKruh == true
                  && mr.pole[s-3][t].maKruh == true && mr.pole[s-4][t].obsadeny == false){
                   mojeX4Zp = s-4;
                   mojeY4Zp = t;
                   scenarMoje4Zp = true;
               }
               s--;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - - juhozapad - severovychod
       t=0;
       for(int m=1; m < 7; m++){
           s=9;
           for(int k=1; k < 7; k++){
               if(mr.pole[t][s].maKruh == true && mr.pole[t+1][s-1].maKruh == true && mr.pole[t+2][s-2].maKruh == true
                  && mr.pole[t+3][s-3].maKruh == true && mr.pole[t+4][s-4].obsadeny == false) {
                   mojeX4Zp = t+4;
                   mojeY4Zp = s-4;
                   scenarMoje4Zp = true;
               }
               s--;
           }
           t++;
       }
       
       // vyhodnot uhlopriecne - juhovychod - severozapad
       t = 9;
       for(int m=1; m < 7; m++) {
           s = 9;
           for(int k=1; k < 7; k++) {
               if(mr.pole[t][s].maKruh == true && mr.pole[t-1][s-1].maKruh == true && mr.pole[t-2][s-2].maKruh == true
                  && mr.pole[t-3][s-3].maKruh == true && mr.pole[t-4][s-4].obsadeny == false) {
                   mojeX4Zp = t-4;
                   mojeY4Zp = s-4;
                   scenarMoje4Zp = true;
               }
               s--;
           }
           t--;
       }
    }
        
    private void scenar0(){
        setR(26);         
        rand = new Random();
        o = rand.nextInt(10);
        p = rand.nextInt(10);
        
        while(mr.pole[o][p].obsadeny == true){
           o = rand.nextInt(10);
           p = rand.nextInt(10);
        }
        
        switch(o){           
                case 0:
                    setX(7);              
                    break;
                case 1:
                    setX(47);
                    break;
                case 2:
                    setX(87);
                    break;
                case 3:
                    setX(127);
                    break;
                case 4:
                    setX(167);
                    break;
                case 5:
                    setX(207);
                    break;
                case 6:
                    setX(247);
                    break;
                case 7:
                    setX(287);
                    break;
                case 8:
                    setX(327);
                    break;
                case 9:
                    setX(367);
                    break;
            }
        
            switch(p){
                case 0:
                    setY(7);
                    break;
                case 1:
                    setY(47);
                    break;
                case 2:
                    setY(87);
                    break;
                case 3:
                    setY(127);
                    break;
                case 4:
                    setY(167);
                    break;
                case 5:
                    setY(207);
                    break;
                case 6:
                    setY(247);
                    break;
                case 7:
                    setY(287);
                    break;
                case 8:
                    setY(327);
                    break;
                case 9:
                    setY(367);
                    break;
            }
            try {
                sem.acquire();
                mr.repaint((int)mr.pole[o][p].x, (int)mr.pole[o][p].y, 40, 40);
                mr.pole[o][p].maKruh = true;
                mr.pole[o][p].obsadeny = true;
                Piskvorky.oznam.setText("YOUR TURN");               
                sem.release();
            }
            catch(InterruptedException e){}
    }
    
    private void nastavSuradnice(int a, int b){
        setR(26);
        switch(a){           
                case 0:
                    setX(7);              
                    break;
                case 1:
                    setX(47);
                    break;
                case 2:
                    setX(87);
                    break;
                case 3:
                    setX(127);
                    break;
                case 4:
                    setX(167);
                    break;
                case 5:
                    setX(207);
                    break;
                case 6:
                    setX(247);
                    break;
                case 7:
                    setX(287);
                    break;
                case 8:
                    setX(327);
                    break;
                case 9:
                    setX(367);
                    break;
            }
        
            switch(b){
                case 0:
                    setY(7);
                    break;
                case 1:
                    setY(47);
                    break;
                case 2:
                    setY(87);
                    break;
                case 3:
                    setY(127);
                    break;
                case 4:
                    setY(167);
                    break;
                case 5:
                    setY(207);
                    break;
                case 6:
                    setY(247);
                    break;
                case 7:
                    setY(287);
                    break;
                case 8:
                    setY(327);
                    break;
                case 9:
                    setY(367);
                    break;
            }
            try {
              sem.acquire();
              mr.repaint((int)mr.pole[a][b].x, (int)mr.pole[a][b].y, 40, 40);
              mr.pole[a][b].maKruh = true;
              mr.pole[a][b].obsadeny = true;
              Piskvorky.oznam.setText("YOUR TURN");          
              sem.release();
           }
           catch(InterruptedException e){}
    }
    
    public void nakresliKruh(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5f));
        g2.setColor(new Color(51,153,255));
        g2.drawOval(x, y, r, r);
    }
}