package piskvorky;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.concurrent.Semaphore;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 *
 * @author Rastislav Boroš
 */
public class Piskvorky {
    JFrame okno;
    public static JLabel oznam;
    JLabel autor;
    JLabel Pname;
    public static JLabel Pscore;
    JLabel Cname;
    public static JLabel Cscore;
    JLabel div;
    JPanel LPanel; // Label panel
    JPanel spodnyPanel;
    JButton restart;
    JToggleButton music;
    Mriezka mriezka;
    Font font;
    private InputStream is;
    Semaphore sem;  
    JOptionPane pane;
    public static String playerName;
    JDialog nameDialog;
    JLabel dialogText;
    JTextField dialogTF;
    JButton dialogButton;
    private ImageIcon img; // pre koniecDialog Quit button
    private ImageIcon img1; // pre koniecDialog Restart button
    AudioInputStream ais;
    Clip hudba;
    
    public Piskvorky(){
        nastavLook(); // windows look sa nastavi 
        nastavHlavneOkno();
        nastavHudbu();
        img = new ImageIcon(getClass().getResource("/resources/exit.png"));
        img1 = new ImageIcon(getClass().getResource("/resources/restart.png"));
        
        is = getClass().getResourceAsStream("/resources/OCR-a.ttf");
        try {
            font = Font.createFont(Font.TRUETYPE_FONT,is).deriveFont(Font.BOLD, 24f);
        }
        catch(FontFormatException | IOException e){}
        
        playerName = "";
        
        nastavLabelPanel();
        
        oznam = new JLabel("PLAY");
        oznam.setFont(font);
        oznam.setForeground(new Color(0,0,200));
        oznam.setPreferredSize(new Dimension(600,30));
        oznam.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        nastavSpodnyPanel();
  
        sem = new Semaphore(1);
        mriezka = new Mriezka(sem, this);

        okno.setVisible(true);

        spustDialog();
        okno.add(LPanel); 
        okno.add(Box.createRigidArea(new Dimension(0,10)));
        okno.add(oznam);
        okno.add(Box.createRigidArea(new Dimension(0,20)));
        okno.add(mriezka);
        okno.add(Box.createRigidArea(new Dimension(0,30)));
        okno.add(spodnyPanel);
        okno.revalidate();
    }
    
    private void nastavLook(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex){}
    }
    
    private void nastavHlavneOkno(){
        okno = new JFrame("TIC TAC TOE");
        okno.setSize(600,600);
        okno.setLocationRelativeTo(null);
        okno.setResizable(false);
        okno.getContentPane().setBackground(new Color(204,229,255));       
        okno.isAlwaysOnTop();
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        okno.setIconImage(new ImageIcon(getClass().getResource("/resources/Pikona2.jpg")).getImage());
        okno.setLayout(new BoxLayout(okno.getContentPane(),BoxLayout.PAGE_AXIS));        
    }
    
    private void nastavHudbu(){
        try {
            ais = AudioSystem.getAudioInputStream(getClass().getResource("/resources/TTT.wav"));
            hudba = AudioSystem.getClip();
            hudba.open(ais);
            hudba.loop(Clip.LOOP_CONTINUOUSLY);
            hudba.start();
        }
        catch(LineUnavailableException | IOException | UnsupportedAudioFileException e) {}
    }
    
    private void nastavLabelPanel(){
        LPanel = new JPanel();
        LPanel.setPreferredSize(new Dimension(600,50));
        LPanel.setMaximumSize(new Dimension(600,50));
        LPanel.setBackground(new Color(255,223,0));
        Pname = new JLabel();
        Pname.setFont(font);
        Pname.setForeground(new Color(0,204,0));
        Pscore = new JLabel("  0"); 
        Pscore.setFont(font);
        Pscore.setForeground(new Color(255,51,153));
        Cscore = new JLabel("0  "); 
        Cscore.setFont(font);
        Cscore.setForeground(new Color(255,51,153));
        Cname = new JLabel("Computer");
        Cname.setFont(font);
        Cname.setForeground(new Color(0,204,0));
        div = new JLabel("  :  ");
        div.setFont(font);
        LPanel.add(Pname);
        LPanel.add(Pscore);
        LPanel.add(div);
        LPanel.add(Cscore);
        LPanel.add(Cname);
    }
    
    private void nastavSpodnyPanel(){
        spodnyPanel = new JPanel();
        spodnyPanel.setLayout(new BorderLayout());
        spodnyPanel.setPreferredSize(new Dimension(600,30));
        spodnyPanel.setMaximumSize(new Dimension(600,30));
        spodnyPanel.setBackground(new Color(204,229,255));
                
        autor = new JLabel("(c) Rasto Boros, 2015 ");
        font = font.deriveFont(Font.PLAIN, 12f);
        autor.setFont(font); 
        autor.setMaximumSize(new Dimension(200,25));
        autor.setPreferredSize(new Dimension(200,25));
        autor.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        autor.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        font = font.deriveFont(Font.BOLD, 18f);
        restart = new JButton("Restart");
        restart.setFont(font);
        restart.setForeground(new Color(255,140,0));
        restart.setPreferredSize(new Dimension(120,25));
        restart.setMaximumSize(new Dimension(120,25));
        restart.setAlignmentX(Component.LEFT_ALIGNMENT);
        restart.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        restart.setFocusable(false);
        restart.setBorderPainted(false);
        restart.setContentAreaFilled(false);
        restart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        restart.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(Mriezka.counter < 80)
                    oznam.setText("Grid less than 80% full. Keep playing.");                
                else 
                    novyStart();                            
             }          
        });
        
        music = new JToggleButton("Music off", new ImageIcon(getClass().getResource("/resources/music_off.png")));
        music.setFont(font);
        music.setForeground(new Color(102,204,0));
        music.setPreferredSize(new Dimension(130,40));
        music.setMaximumSize(new Dimension(130,40));
        music.setAlignmentX(Component.CENTER_ALIGNMENT);
        music.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        music.setFocusable(false);
        music.setBorderPainted(false);
        music.setContentAreaFilled(false);
        music.setCursor(new Cursor(Cursor.HAND_CURSOR));
        music.setIconTextGap(8);
        music.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent ie){
                if(music.isSelected()){
                    music.setText("Music on");
                    music.setIcon(new ImageIcon(getClass().getResource("/resources/music.png")));
                    hudba.stop();
                }
                else {
                    music.setText("Music off");
                    music.setIcon(new ImageIcon(getClass().getResource("/resources/music_off.png")));
                    hudba.start();
                    hudba.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }
        });
        
        
        spodnyPanel.add(restart, BorderLayout.LINE_START);
        spodnyPanel.add(music, BorderLayout.CENTER);
        spodnyPanel.add(autor, BorderLayout.LINE_END);       
    }

    private void spustDialog(){     
        nameDialog = new JDialog(okno,"What is your name?",true);
        nameDialog.setSize(new Dimension(350,120));
        nameDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        nameDialog.setIconImage(new ImageIcon(getClass().getResource("/resources/question_mark.png")).getImage());
        nameDialog.getContentPane().setBackground(new Color(255,223,0));
        nameDialog.setLayout(null);
        nameDialog.setResizable(false);
        nameDialog.setLocationRelativeTo(okno);
        
        font = font.deriveFont(Font.BOLD, 15f);
        dialogText = new JLabel("Enter your name:");
        dialogText.setFont(font);
        dialogText.setForeground(Color.red);
        dialogText.setBounds(10,10,170,20);
        
        dialogTF = new JTextField(15);
        dialogTF.setFont(font);
        dialogTF.setBounds(170,10,160,23);
        
        font = font.deriveFont(Font.PLAIN, 18f);
        dialogButton = new JButton("Submit");
        dialogButton.setFont(font);
        dialogButton.setForeground(new Color(0,153,0));
        dialogButton.setBounds(110,50,110,30);
        dialogButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dialogButton.setFocusable(false);     
        dialogButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(dialogTF.getText().equals("")){
                    font = font.deriveFont(Font.BOLD,16f);
                    UIManager.put("OptionPane.messageForeground",Color.RED); // UIManager nastavi messageForeground property pre JOptionPane
                    UIManager.put("OptionPane.messageFont", font); // UIManager nastavi messageFont property
                    UIManager.put("OptionPane.buttonFont", font); // UIManager nastavi buttonFont property                   
                    JOptionPane.showMessageDialog(nameDialog,"Please enter your name.", "No name entered", JOptionPane.ERROR_MESSAGE);
                }                   
                else {
                    playerName = dialogTF.getText();
                    Pname.setText(playerName);
                    nameDialog.setVisible(false);
                }   
            }
        });
        
        nameDialog.add(dialogText);
        nameDialog.add(dialogTF);   
        nameDialog.add(dialogButton);
        nameDialog.setVisible(true);
    }
    
    public void koniec(){
        final JDialog koniecDialog = new JDialog(okno,"End of game.", true);       
        koniecDialog.setLocation(okno.getLocation().x + 50, okno.getLocation().y +200);
        koniecDialog.setSize(new Dimension(500,180));
        koniecDialog.getContentPane().setBackground(new Color(255,223,0));
        koniecDialog.setLayout(null);       
        koniecDialog.setUndecorated(true);
        
        font = font.deriveFont(Font.BOLD, 24f);
        JLabel k = new JLabel("<html><p>NO MORE MOVES POSSIBLE.<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;END OF GAME.</html>");
        k.setFont(font);
        k.setForeground(new Color(0,0,200));
        k.setBounds(80,10,480,50);
        
        font = font.deriveFont(Font.ITALIC, 30f);
        JLabel r = new JLabel();
        r.setFont(font);
        r.setForeground(new Color(255,0,255));
        r.setText(ktoVyhral());
        r.setBounds(120,75,480,40);
        
        font = font.deriveFont(Font.BOLD, 22f);
        JButton quitButton = new JButton("QUIT", img);
        quitButton.setFont(font);
        quitButton.setForeground(new Color(160,0,0));
        quitButton.setBounds(70,130,130,30);
        quitButton.setFocusPainted(false);   
        quitButton.setContentAreaFilled(false);
        quitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        quitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                System.exit(0);
            }
        });
        
        JButton againButton = new JButton("Play again", img1);
        againButton.setFont(font);
        againButton.setForeground(new Color(0,204,0));
        againButton.setBounds(220,130,240,30);
        againButton.setFocusPainted(false);
        againButton.setContentAreaFilled(false);
        againButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        againButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                novyStart();
                koniecDialog.setVisible(false);
            }
        });
        
        koniecDialog.add(k);
        koniecDialog.add(r);
        koniecDialog.add(quitButton);
        koniecDialog.add(againButton);
        koniecDialog.setVisible(true);
    }
    
    private void novyStart(){
        mriezka.nakresleneKriziky.clear();
        mriezka.nakresleneKruhy.clear();
        mriezka.nakresleneCiary.clear();
        mriezka.nakresleneCiaryP.clear();
        oznam.setText("PLAY");
        oznam.repaint();              
        Mriezka.playerScore = 0;
        Mriezka.computerScore = 0;
        Mriezka.counter = 0;
        Pscore.setText("  0");
        Cscore.setText("0  ");
        LPanel.repaint();
        for(int i=0; i < 10; i++)
            for(int j=0; j < 10; j++) {
                  mriezka.pole[i][j].obsadeny = false;
                  mriezka.pole[i][j].maKrizik = false;
                  mriezka.pole[i][j].maKruh = false;
            }                        
        mriezka.repaint();    
    }
    
    private String ktoVyhral(){
        if(Mriezka.playerScore > Mriezka.computerScore) {
            return playerName + " wins !";
        }
        else if(Mriezka.playerScore < Mriezka.computerScore){
            return "Computer wins !";
        }
        else return "Tie end !";        
    }
       
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                Piskvorky piskvorky = new Piskvorky();
            }
        });
    }    
}