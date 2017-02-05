
/**
 * The GUI for the OrbVenture battle calculator.
 * 
 * # = finished
 * TO DO:
 * - Finish GUI setup#
 * - Create window when you click on respectful buttons#
 * - Add#/remove# slots
 *      - Choose enemy or ally#
 * - Input boxes#
 * - Character card class#
 *      - Constructor#
 *      - Accessor methods#
 *      - Calculations for other stats#
 * - Action functions
 *      - Attack#
 *      - Change stat(s)#
 *      - Accuracy + crit chance#
 * - "Clear All" button#   
 * - Error handling (optional)
 * - Format to fit any screen + full screen mode (optional)
 * - Organize code + documentation#
 * - Upload to GitHub, share on FB OV group and FB
 * 
 * ***************************************
 * 
 * @author Catherine Guevara, Nisnow
 * 
 * ***************************************
 * PROGRESS LOG
 * 
 * 1/24/2017 --- started project and created frame
 * 1/30/2017 --- Finished basic GUI
 *               Created windows when you click on create enemy/ally buttons
 *               Generate cards in respectful places
 * 1/31/2017 --- Finished input boxes (text fields) -- no functionality yet  
 * 2/01/2017 --- NEW PLAN: Unit class
 *           --- Created basic Unit class (constructor and accessor methods)
 *           --- Fixed the card deletion bug
 *           --- Set-up basic layout of a Unit's stat card
 * 2/02/2017 --- Created an ArrayList of Units to hold each character (this will 
 *               allow easy referencing for the attack function
 *           --- Created attack box    
 *           --- Attack function works
 * 2/03/2017 --- Created "change stat" function          
 *           --- Started "remove all" functions
 *           --- Officially functionable! Time to work on clean-up
 * **************************************          
 *           --- Added text to calculate accuracy and chance of crit
 *           --- Fixed text display of accuracy and crit
 *           --- Fixed negative damage --> now returns 0
 *           --- Completed the deleteEnemies/Allies buttons
 *           --- Completed documentation
 *           --- Fixed some pointer errors
 * 
 * ******************
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class GUI
{
    private static JFrame frame;
    private static JButton enemyButton, allyButton, deleteEnemies, deleteAllies;
    private static Container pane, ePane, aPane;
    private static JPanel boxArea;
    private static int eCardCount, aCardCount;
    private static ArrayList<Unit> cardList = new ArrayList<Unit>();
    private static ArrayList<Unit> enemyList = new ArrayList<Unit>();
    private static ArrayList<Unit> allyList = new ArrayList<Unit>();
    
    private static void createframe()
    {
        /* Create the main window */
        frame = new JFrame("OrbVenture Battle Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /* pane is used to separate the buttons from the boxes */
        pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());
        
        createButtons();
        
        /* Display the frame */
        frame.setSize(1750, 725);
        frame.setResizable(false);
        frame.setVisible(true);    
    }
    
    /* Generate buttons that create enemies and allies*/
    private static void createButtons()
    {
        /* Panel to hold the buttons */
        JPanel buttonPanel = new JPanel(); 
        buttonPanel.setLayout(new GridLayout(4, 0));
        buttonPanel.setPreferredSize(new Dimension(150, 100));
        
        /* Panel that holds all the character cards */
        boxArea = new JPanel();
        boxArea.setLayout(new BorderLayout());
        
        enemyButton   = new JButton("Create Enemy");        
        allyButton    = new JButton("Create Ally");
        deleteEnemies = new JButton("Delete all enemies");
        deleteAllies  = new JButton("Delete all allies");
        
        /* Call method to create the input windows */
        enemyButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) {
                createInputWindow(0, enemyButton);
                //TODO: disable button when you have it open
            }          
        });
        allyButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) {
                createInputWindow(1, allyButton);
            }          
        });
        
        /* add the buttons */
        buttonPanel.add(enemyButton);
        buttonPanel.add(allyButton);
        buttonPanel.add(deleteEnemies);
        buttonPanel.add(deleteAllies);
        
        /* Create snazzy borders */
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Options"));
        boxArea.setBorder(BorderFactory.createLineBorder(Color.black));
        
        /* Add the two areas to pane */
        pane.add(boxArea, BorderLayout.CENTER);
        pane.add(buttonPanel, BorderLayout.WEST); 
        
        /* Add Container to hold enemy cards */
        ePane = new JPanel();
        boxArea.add(ePane, BorderLayout.NORTH);
        ePane.setLayout(new FlowLayout());
        
        /* Add Container to hold ally cards*/
        aPane = new JPanel();
        boxArea.add(aPane, BorderLayout.SOUTH);
        aPane.setLayout(new FlowLayout(FlowLayout.CENTER));
    }
    
    private static JFormattedTextField createInputField(JPanel pPanel, String pText)
    {
        JLabel label = new JLabel(pText);
        
        JFormattedTextField field = new JFormattedTextField();
        field.setColumns(10);
        
        pPanel.add(label);
        pPanel.add(field);
        
        return field;
    }
    
    /* Input window */
    private static void createInputWindow(int type, JButton button) //0 = enemy; 1 = ally
    {
        String fLabel = "Create an ";
        if(type == 0) fLabel += "enemy";
        if(type == 1) fLabel += "ally";
        JFrame sFrame = new JFrame(fLabel);
        sFrame.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        sFrame.addWindowListener(new WindowAdapter() 
        {
            public void WindowClosing(WindowEvent e)
            {
                button.setEnabled(true);
            }
        });
        
        String sLabel = "";
        if(type == 0) sLabel = "enemy";
        if(type == 1) sLabel = "ally";
        JLabel headerLabel = new JLabel("");
        headerLabel.setText("Input stats for new " + sLabel);
        headerLabel.setVerticalAlignment(JLabel.TOP);
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        
        /* Add the labels and fields to the pane */
        JPanel wStatPane = new JPanel(new GridLayout(6, 2));
        JPanel eStatPane = new JPanel(new GridLayout(6, 2));
        
        /* Text fields */
        JFormattedTextField PWR_FIELD = createInputField(wStatPane, "PWR: ");
        PWR_FIELD.setValue(5);
        
        JFormattedTextField DEF_FIELD = createInputField(wStatPane, "DEF: ");
        DEF_FIELD.setValue(5);
        
        JFormattedTextField APT_FIELD = createInputField(wStatPane, "APT: ");
        APT_FIELD.setValue(5);
        
        JFormattedTextField RES_FIELD = createInputField(wStatPane, "RES: ");
        RES_FIELD.setValue(5);
        
        JFormattedTextField SPD_FIELD = createInputField(eStatPane, "SPD: ");
        SPD_FIELD.setValue(5);
        
        JFormattedTextField TGT_FIELD = createInputField(eStatPane, "TGT: ");
        TGT_FIELD.setValue(5);
        
        JFormattedTextField AGI_FIELD = createInputField(eStatPane, "PWR: ");
        AGI_FIELD.setValue(5);
        
        JFormattedTextField CRT_FIELD = createInputField(eStatPane, "CTR: ");
        CRT_FIELD.setValue(5);
        
        JFormattedTextField HP_FIELD = createInputField(wStatPane, "HP: ");
        HP_FIELD.setValue(1);
        
        JFormattedTextField NAME_FIELD = createInputField(eStatPane, "NAME: ");
        NAME_FIELD.setValue("");
        
        JFormattedTextField LVL_FIELD = createInputField(wStatPane, "LVL: ");
        LVL_FIELD.setValue(1);
        
        eStatPane.add(new JLabel(" "));
        eStatPane.add(new JLabel(" "));
        
        JButton okButton = new JButton("OK");
        panel.add(okButton);
        sFrame.add(panel, BorderLayout.SOUTH);
        
        /* Create a new Unit object (the character) upon clicking "OK" button */
        okButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) {
                Unit character = new Unit(
                    Integer.parseInt(PWR_FIELD.getText()),
                    Integer.parseInt(DEF_FIELD.getText()),
                    Integer.parseInt(APT_FIELD.getText()),
                    Integer.parseInt(RES_FIELD.getText()), 
                    Integer.parseInt(SPD_FIELD.getText()),
                    Integer.parseInt(TGT_FIELD.getText()),
                    Integer.parseInt(AGI_FIELD.getText()),
                    Integer.parseInt(CRT_FIELD.getText()),
                    Integer.parseInt(HP_FIELD.getText()),
                    Integer.parseInt(LVL_FIELD.getText()),
                    NAME_FIELD.getText()
                );
                
                /* Add to respective ArrayLists */
                cardList.add(character);
                if(type == 0) enemyList.add(character);
                if(type == 1) allyList.add(character);
                
                createCharacter(type, character);
                
                sFrame.dispose();
            }          
        });
        
        /* Add the panels to the frame */
        sFrame.add(headerLabel, BorderLayout.NORTH);
        sFrame.add(wStatPane, BorderLayout.WEST);
        sFrame.add(eStatPane, BorderLayout.CENTER);

        sFrame.setPreferredSize(new Dimension(500, 500));
        sFrame.setLocationByPlatform(true);
        sFrame.pack();
        sFrame.setResizable(false);
        sFrame.setVisible(true);
    }
    
    /* Creates a new card of character */
    private static void createCharacter(int type, Unit unit) //0 = enemy; 1 = ally
    {
        /* Create a card that represents the character */
        JButton charCard = new JButton();
        charCard.setPreferredSize(new Dimension(175, 175));
        charCard.setHorizontalTextPosition(SwingConstants.CENTER);
        JLabel name = new JLabel("");
        
        Font cardFont = new Font("Consolas", Font.PLAIN, 24);
        name.setFont(cardFont);
        
        name.setText(unit.getName());
        charCard.add(name);
        
        /* Add each type of card to their respective areas */
        if(type == 1) 
        {
            aPane.add(charCard, BorderLayout.SOUTH);
            charCard.setBackground(Color.decode("#00cc99"));
        }
        if(type == 0) 
        {
            ePane.add(charCard, BorderLayout.NORTH);
            charCard.setBackground(Color.decode("#ff5050"));
        }
     
        boxArea.revalidate();
        boxArea.repaint();
        
        /* Create character card frame with all its amazing windows */
        charCard.addActionListener(new ActionListener() 
        {
            /* Window that holds basic information */
            public void actionPerformed(ActionEvent e) {
                
            JFrame sFrame = new JFrame(unit.getName());
            sFrame.setLayout(new GridLayout(4, 1));
            
            /* Top panels holds unit's name and HP */
            JPanel top = new JPanel();
            top.setLayout(new GridLayout(3, 1)); //name, LVL, HP
                JLabel NAME = new JLabel("Name: " + unit.getName()); top.add(NAME);
                JLabel LVL  = new JLabel("LVL: "  + unit.getLVL());  top.add(LVL);
                JLabel HP   = new JLabel("HP: "   + unit.getHP() + " / " + unit.getMaxHP());   top.add(HP);
            top.setBorder(BorderFactory.createLineBorder(Color.black));
            
            /* All the combat stats in the middel panel */
            JPanel combatStats = new JPanel();
            combatStats.setLayout(new GridLayout(4, 2));
                JLabel PWR = new JLabel("PWR: " + unit.getPWR()); combatStats.add(PWR);
                JLabel DEF = new JLabel("DEF: " + unit.getDEF()); combatStats.add(DEF);
                JLabel APT = new JLabel("APT: " + unit.getAPT()); combatStats.add(APT);
                JLabel RES = new JLabel("RES: " + unit.getRES()); combatStats.add(RES);
                JLabel SPD = new JLabel("SPD: " + unit.getSPD()); combatStats.add(SPD);
                JLabel TGT = new JLabel("TGT: " + unit.getTGT()); combatStats.add(TGT);
                JLabel AGI = new JLabel("AGI: " + unit.getAGI()); combatStats.add(AGI);
                JLabel CRT = new JLabel("CRT: " + unit.getCRT()); combatStats.add(CRT);
            combatStats.setBorder(BorderFactory.createLineBorder(Color.black));    
            
            /* Reference stats in bottom panel */
            JPanel refStats = new JPanel();
            refStats.setLayout(new GridLayout(4, 2));
                JLabel ATK  = new JLabel("ATK: "  + unit.getATK());  refStats.add(ATK);
                JLabel PTC  = new JLabel("PTC: "  + unit.getPTC());  refStats.add(PTC);
                JLabel cATK = new JLabel("cATK: " + unit.getcATK()); refStats.add(cATK);
                JLabel cPTC = new JLabel("cPTC: " + unit.getcPTC()); refStats.add(cPTC);
                JLabel ACC  = new JLabel("ACC: "  + unit.getACC());  refStats.add(ACC);
                JLabel SKL  = new JLabel("SKL: "  + unit.getSKL());  refStats.add(SKL);
                JLabel LCK  = new JLabel("LCK: "  + unit.getLCK());  refStats.add(LCK);
                JLabel STUD = new JLabel("STUD: " + unit.getSTUD()); refStats.add(STUD);
            refStats.setBorder(BorderFactory.createLineBorder(Color.black));
            
            /* Add panels to the frame */
            sFrame.add(top, BorderLayout.NORTH);
            sFrame.add(combatStats, BorderLayout.CENTER);
            sFrame.add(refStats, BorderLayout.CENTER);
            
            /* Panel that holds the action buttons */
            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.CENTER));

            /* "Attack" button and its window */
            JButton atkButton = new JButton("Attack");
            panel.add(atkButton);
            atkButton.addActionListener(new ActionListener()
            {
                /* Create window that lets you select a target */
                public void actionPerformed(ActionEvent e) {
                    JFrame atkFrame = new JFrame("Attack!!");
                    JPanel buttonPane = new JPanel();
                    buttonPane.setLayout(new FlowLayout());
                    
                    /* Radio buttons that let you select whether an attack is melee or magic */
                    JRadioButton meleeButton = new JRadioButton("Melee");
                    meleeButton.setSelected(true);
                    JRadioButton magicButton = new JRadioButton("Magic");
                    
                    /* Create button group for the radio buttons */
                    ButtonGroup choices = new ButtonGroup();
                    choices.add(meleeButton);
                    choices.add(magicButton);
                    
                    /* Add the buttons to the pane */
                    buttonPane.add(meleeButton);
                    buttonPane.add(magicButton);
                    
                    atkFrame.add(buttonPane, BorderLayout.NORTH);
                    
                    /* Create checkbox that let's you select a critical attack */
                    JCheckBox checkCrit = new JCheckBox("Critical");
                    JPanel checkPane = new JPanel(new FlowLayout());
                    checkPane.add(checkCrit);
                    atkFrame.add(checkPane, BorderLayout.CENTER);
                    
                    /* Create a label to display your accuracy and chance of critical hit */
                    JLabel accLabel = new JLabel();
                    accLabel.setText("Choose your attack type and target");
                    JPanel bottomPane = new JPanel();
                    bottomPane.setLayout(new BoxLayout(bottomPane, BoxLayout.Y_AXIS));
                    
                    Unit selected = new Unit(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "");
                    if(type == 0) //if this unit is an enemy, show a comobo-box of allies
                    {
                        JComboBox allyMenu = new JComboBox();
                        for(int k = 0; k < allyList.size(); k++)
                            allyMenu.addItem(allyList.get(k));
                        allyMenu.setBorder(BorderFactory.createEmptyBorder());
                        selected = (Unit)allyMenu.getSelectedItem();
                        
                        /* Update the label as so */
                        allyMenu.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent e)
                            {
                                Unit selected = (Unit)allyMenu.getSelectedItem();
                                
                                accLabel.setText(unit.calculateACC(selected));
                                bottomPane.revalidate();
                                bottomPane.repaint();
                                
                                checkCrit.addActionListener(new ActionListener()
                                {
                                    public void actionPerformed(ActionEvent e)
                                    {
                                        Unit selected = (Unit)allyMenu.getSelectedItem();
                                        
                                        if(checkCrit.isSelected())
                                            accLabel.setText(unit.calculateCRT(selected));
                                        else
                                            accLabel.setText(unit.calculateACC(selected));
                                    }
                                });
                            }
                        });
                        
                            /* Add "OK" button to confirm your action */
                        JButton okButton = new JButton("OK");
                        okButton.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent e)
                            {
                                Unit selected = (Unit)allyMenu.getSelectedItem();
                                /* Also create a new window displaying how much damage was dealt */
                                JFrame result = new JFrame();
                                JLabel dmg = new JLabel();
                                int iDmg = 0;
                                
                                /* Check whether melee or magic is selected and whether crit checkbox is as well */
                                if(meleeButton.isSelected() && !checkCrit.isSelected())
                                {
                                    unit.attack(selected, "melee");
                                    iDmg = unit.printDamage(selected, "melee");
                                }
                                if(meleeButton.isSelected() && checkCrit.isSelected())
                                {
                                    unit.critAttack(selected, "melee");
                                    iDmg = unit.printCritDamage(selected, "melee");
                                }
                                if(magicButton.isSelected() && !checkCrit.isSelected())
                                {
                                    unit.attack(selected, "magic");
                                    iDmg = unit.printDamage(selected, "magic");
                                }
                                if(magicButton.isSelected() && checkCrit.isSelected())
                                {
                                    unit.critAttack(selected, "magic");
                                    iDmg = unit.printCritDamage(selected, "magic");
                                }
                                
                                /* Set the text */
                                dmg.setText(selected + " took " + iDmg + " damage!");
                                result.add(dmg, SwingConstants.CENTER);
                                
                                top.revalidate();
                                top.repaint();
                                
                                result.setPreferredSize(new Dimension(300, 250));
                                result.pack();
                                result.setLocationByPlatform(true);
                                result.setResizable(false);
                                result.setVisible(true);
                                
                                atkFrame.dispose();
                            }
                        });
                    
                        checkPane.add(okButton);
                        
                        atkFrame.add(allyMenu, BorderLayout.PAGE_END);
                    }
                    if(type == 1) //if this unit is an ally, then show a combo-box of enemies
                    {
                        JComboBox enemyMenu = new JComboBox();
                        for(int j = 0; j < enemyList.size(); j++)
                            enemyMenu.addItem(enemyList.get(j));  
                        enemyMenu.setBorder(BorderFactory.createEmptyBorder());    
                        selected = (Unit)enemyMenu.getSelectedItem();
                        final Unit nSel = selected;
                        
                        /* Update the label as so */
                        enemyMenu.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent e)
                            {
                                Unit selected = (Unit)enemyMenu.getSelectedItem();
                                
                                accLabel.setText(unit.calculateACC(selected));
                                bottomPane.revalidate();
                                bottomPane.repaint();
                                
                                checkCrit.addActionListener(new ActionListener()
                                {
                                    public void actionPerformed(ActionEvent e)
                                    {
                                        Unit selected = (Unit)enemyMenu.getSelectedItem();
                                        
                                        if(checkCrit.isSelected())
                                            accLabel.setText(unit.calculateCRT(selected));
                                        else
                                            accLabel.setText(unit.calculateACC(selected));
                                    }
                                });
                            }
                        });
                        
                        /* Add "OK" button to confirm your action */
                        JButton okButton = new JButton("OK");
                        okButton.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent e)
                            {
                                Unit selected = (Unit)enemyMenu.getSelectedItem();
                                /* Also create a new window displaying how much damage was dealt */
                                JFrame result = new JFrame();
                                JLabel dmg = new JLabel();
                                int iDmg = 0;
                                
                                /* Check whether melee or magic is selected and whether crit checkbox is as well */
                                if(meleeButton.isSelected() && !checkCrit.isSelected())
                                {
                                    unit.attack(selected, "melee");
                                    iDmg = unit.printDamage(selected, "melee");
                                }
                                if(meleeButton.isSelected() && checkCrit.isSelected())
                                {
                                    unit.critAttack(selected, "melee");
                                    iDmg = unit.printCritDamage(selected, "melee");
                                }
                                if(magicButton.isSelected() && !checkCrit.isSelected())
                                {
                                    unit.attack(selected, "magic");
                                    iDmg = unit.printDamage(selected, "magic");
                                }
                                if(magicButton.isSelected() && checkCrit.isSelected())
                                {
                                    unit.critAttack(selected, "magic");
                                    iDmg = unit.printCritDamage(selected, "magic");
                                }
                                
                                /* Set the text */
                                dmg.setText(selected + " took " + iDmg + " damage!");
                                result.add(dmg, SwingConstants.CENTER);
                                
                                top.revalidate();
                                top.repaint();
                                
                                result.setPreferredSize(new Dimension(300, 250));
                                result.pack();
                                result.setLocationByPlatform(true);
                                result.setResizable(false);
                                result.setVisible(true);
                                
                                atkFrame.dispose();
                            }
                        });
                        
                        checkPane.add(okButton);
                        
                        atkFrame.add(enemyMenu, BorderLayout.PAGE_END);
                    }
                    
                    /* Add accuracy label to the bottom pane */
                    bottomPane.add(accLabel);
                    bottomPane.revalidate();
                    bottomPane.repaint();
                    
                    bottomPane.add(checkPane);
                    
                    atkFrame.add(bottomPane, BorderLayout.CENTER);
                    atkFrame.setPreferredSize(new Dimension(300, 250));
                    atkFrame.pack();
                    atkFrame.setLocationByPlatform(true);
                    atkFrame.setResizable(false);
                    atkFrame.setVisible(true);
                }
            });
            
            /* Button that lets you change stats */
            JButton changeButton = new JButton("Change stat");
            String[] stats = {"HP", "PWR", "DEF", "APT", "RES", "SPD", "TGT", "AGI", "CRT", "LVL"};
            
            changeButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    JFrame scFrame = new JFrame("Change a stat");
                    JPanel scPane = new JPanel();
                    scPane.setLayout(new BoxLayout(scPane, BoxLayout.Y_AXIS));
                    JPanel buttonPane = new JPanel();
                    buttonPane.setLayout(new FlowLayout());
                    
                    /* Create a combobox that shows all the stats */
                    JComboBox statMenu = new JComboBox(stats);
                    
                    /* Create the text field to enter a value */
                    JLabel vfLabel = new JLabel("Change stat by: ");
                    JFormattedTextField valueField = new JFormattedTextField();
                    valueField.setPreferredSize(new Dimension(250, 10));
                    valueField.setValue(0);
                    valueField.setColumns(10);
                    
                    JButton okButton = new JButton("OK");
                    okButton.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            /* Call the unit's changeStat() method */
                            String selected = statMenu.getSelectedItem().toString();
                            unit.changeStat(selected, Integer.parseInt(valueField.getText()));
                            
                            combatStats.revalidate();
                            combatStats.repaint();
                            
                            scFrame.dispose();
                        }
                    });
                    
                    /* Also an option to change all stats */
                    JButton changeAll = new JButton("Change all");
                    changeAll.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            for(int k = 0; k < stats.length - 1; k++) //don't include LVL
                            {
                                String selected = stats[k].toString();
                                unit.changeStat(selected, Integer.parseInt(valueField.getText()));
                                
                                combatStats.revalidate();
                                combatStats.repaint();
                            
                                scFrame.dispose();
                            }
                        }
                    });
                    
                    /* Add everything to the panel */
                    scPane.add(vfLabel);
                    scPane.add(valueField);
                    scPane.add(statMenu);
                    scPane.add(okButton);
                    scPane.add(buttonPane);
                    
                    buttonPane.add(okButton); buttonPane.add(changeAll);
                    
                    /* Frame settings */
                    scFrame.add(scPane);
                    scFrame.setPreferredSize(new Dimension(300, 250));
                    scFrame.pack();
                    scFrame.setLocationByPlatform(true);
                    scFrame.setResizable(false);
                    scFrame.setVisible(true);
                }
            });
            panel.add(changeButton);
            
            /* Create the delete button */
            JButton delButton = new JButton("Delete");
            panel.add(delButton);
            sFrame.add(panel, BorderLayout.SOUTH);
            
            delButton.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) {
                    /* Delete the card and remove from ArrayList according to its type */
                    if(type == 0)
                    {
                        ePane.remove(charCard);
                        enemyList.remove(unit);
                    }
                    if(type == 1)
                    {
                        aPane.remove(charCard);
                        allyList.remove(unit);
                    }
                    
                    boxArea.revalidate();
                    boxArea.repaint();
                    cardList.remove(unit);
                    sFrame.dispose();
                }          
            });
            
            sFrame.setPreferredSize(new Dimension(500, 500));
            sFrame.pack();
            sFrame.setLocationByPlatform(true);
            sFrame.setResizable(false);
            sFrame.setVisible(true);
            }          
        });
        
        /* ActionListener for the deleteEnemies button */
        deleteEnemies.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) {
                /* Loop through the array and delete */
                for(int k = 0; k < enemyList.size(); k++)
                {
                    enemyList.remove(unit);
                    ePane.remove(charCard);
                }
                boxArea.revalidate();
                boxArea.repaint();
            }          
        });
        
        /* ActionListener for the deleteAllies button */
        deleteAllies.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) {
                /* Loop through the array and delete */
                for(int k = 0; k < allyList.size(); k++)
                {
                    allyList.remove(unit);
                    aPane.remove(charCard);
                }
                boxArea.revalidate();
                boxArea.repaint();
            }
        });
    }
    
    /* Main method to run the application itself */
    public static void main(String[] args)
    {
        createframe();
    }
}
