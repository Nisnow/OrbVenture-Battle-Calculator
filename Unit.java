
/**
 * Create new character cards and do wacky stuff with them
 * 
 * Combat stats are inputted through the stat input window. While HP is 
 * usually calculated from the basic stats, you must calculate it yourself
 * and use that here! 
 * Note that you MUST input your TOTAL stats, not your BASE!!!
 * 
 * The rest of the stats are calculated by plugging in the base stats into
 * their respective formulas
 * 
 * @author Catherine Guevara, Nisnow 
 * 1/31/2017 --- Created class
 *           --- Created constructor
 * 2/02/2017 --- Created attack functions    
 * 2/03/2017 --- Fixed some rounding errors of stats
 *           --- Class complete!
 * ************************************
 *           --- Set any negative damage to 0
 *           --- Added some print-damage methods
 */

public class Unit
{
    private int PWR, DEF, APT, RES, SPD, TGT, AGI, CRT; //combat stats
    private int ATK, PTC, cATK, cPTC, ACC, SKL, LCK, STUD; //ref stats
    private int HP, LVL; //HP is all alone. How sad :(
    private int MAXHP;
    private String NAME; //so is name. How sad as well D: HAHA
    
    /* Contructor */
    public Unit(int pwr, int def, int apt, int res, int spd,
                int tgt, int agi, int crt, int hp, int lvl, String name)
    {
        PWR = pwr;
        DEF = def;
        APT = apt;
        RES = res;
        SPD = spd;
        TGT = tgt;
        AGI = agi;
        CRT = crt;
        HP = hp;
        LVL = lvl;
        NAME = name;
        MAXHP = hp;
        
        ACC = tgt * 2;
        SKL = (int)(Math.ceil((double)spd / 2));
        LCK = (int)(Math.ceil((double)crt * 2.5));
        STUD = (int)(Math.ceil((double)crt / 2));
        
        if(Math.abs(pwr - apt) <= LVL * 2)
        {
            ATK = (pwr + SKL) + (int)(Math.ceil((double)apt / 2));
            PTC = (apt + SKL) + (int) (Math.ceil((double)pwr / 2));
        }
        else
        {
            ATK = pwr + SKL;
            PTC = apt + SKL;
        }
        
        cATK = ATK + (int)(Math.ceil((double)crt * 1.5));
        cPTC = PTC + (int)(Math.ceil((double)crt * 1.5));
    }
    
    /* Calculate what the unit must roll higher than to hit the target */
    public String calculateACC(Unit enemy)
    {
        int rollACC = (int)(((double)enemy.AGI / ACC) * 100);        
        return getName() + " must roll higher than " + rollACC + " to hit.";
    }
    
    /* Calculate what the unit must roll higher than to get a crit */
    public String calculateCRT(Unit enemy)
    {
        int highLCK = 0;
        if(enemy.LCK > LCK) highLCK = enemy.LCK;
        else highLCK = LCK;
        
        int rollCRT = (int)(((double)CRT / highLCK) * 100);
        return getName() + " must roll lower than " + rollCRT + " for critical.";
    }
    
    /* This unit attacks another unit with either melee or magic */
    public void attack(Unit enemy, String type)
    {
        int DMG = 0;
        if(type.equalsIgnoreCase("melee"))
        {
            DMG = ATK - enemy.DEF;
            if(DMG <= 0) return;
            enemy.HP -= DMG;
        }
        if(type.equalsIgnoreCase("magic"))  
        {
            DMG = PTC - enemy.RES;
            if(DMG <= 0) return;
            enemy.HP -= ( PTC - enemy.RES );
        }
    }
    
    /* This unit attacks another unit with either critical melee or critical magic */
    public void critAttack(Unit enemy, String type)
    {
        int DMG = 0;
        if(type.equalsIgnoreCase("melee"))
        {
            DMG = cATK - (enemy.DEF + enemy.STUD);
            if(DMG <= 0) return;
            enemy.HP -= DMG;
        }
        if(type.equalsIgnoreCase("magic"))
        {
            DMG = cPTC - (enemy.RES + enemy.STUD);
            if(DMG <= 0) return;
            enemy.HP -= DMG;
        }
    }
    
    /* Method passed through the change stat button */
    public void changeStat(String stat, int value)
    {    
        String nStat = stat.toString();
        if("HP".equals(stat))
            HP += value;
        else if(stat.toString().equals("PWR"))
            PWR += value;
        else if("DEF".equals(stat))
            DEF += value;
        else if("RES".equals(stat))
            RES += value;
        else if("APT".equals(stat))
            APT += value;
        else if("SPD".equals(stat))
            SPD += value;  
        else if("TGT".equals(stat))
            TGT += value;    
        else if("AGI".equals(stat))
            AGI += value;
        else if("CRT".equals(stat))
            CRT += value;   
        else if("LVL".equals(stat))
            LVL += value;
    }
    
    public int printDamage(Unit enemy, String type)
    {
        int DMG = 0;
        if(type.equalsIgnoreCase("melee"))
        {
            DMG = ATK - enemy.DEF;
            if(DMG <= 0) return 0;
        } 
        if(type.equalsIgnoreCase("magic"))   
        {
            DMG = PTC - enemy.RES;
            if(DMG <= 0) return 0;
        }
            
        return DMG;    
    }
    
    public int printCritDamage(Unit enemy, String type)
    {
        int DMG = 0;
        if(type.equalsIgnoreCase("melee"))
        {
            DMG = cATK - (enemy.DEF + enemy.STUD);
            if(DMG <= 0) return 0;
        }
        if(type.equalsIgnoreCase("magic"))    
        {
            DMG = cPTC - (enemy.RES + enemy.STUD);
            if(DMG <= 0) return 0;
        }
            
        return DMG;    
    }
    
    public String toString()
    {
        return this.NAME;
    }

 
    /* Accessor methods */
    
    public int getPWR()
    {
        return this.PWR;
    }
    
    public int getDEF()
    {
        return this.DEF;
    }
    
    public int getAPT()
    {
        return this.APT;
    }
    
    public int getRES()
    {
        return this.RES;
    }
    
    public int getsSPD()
    {
        return this.SPD;
    }
    
    public int getTGT()
    {
        return this.TGT;
    }
    
    public int getAGI()
    {
        return this.AGI;
    }
    
    public int getCRT()
    {
        return this.CRT;
    }
    
    public int getATK()
    {
        return this.ATK;
    }
    
    public int getPTC()
    {
        return this.PTC;
    }
    
    public int getcATK()
    {
        return this.cATK;
    }
    
    public int getcPTC()
    {
        return this.cPTC;
    }
    
    public int getACC()
    {
        return this.ACC;
    }
    
    public int getSKL()
    {
        return this.SKL;
    }
    
    public int getSPD()
    {
        return this.SPD;
    }
    
    public int getHP()
    {
        if(this.HP <= 0) this.HP = 0;
        return this.HP;
    }
    
    public int getMaxHP()
    {
        return this.MAXHP;
    }
    
    public int getLCK()
    {
        return this.LCK;
    }
    
    public int getSTUD()
    {
        return this.STUD;
    }
    
    public int getLVL()
    {
        return this.LVL;
    }
   
    public String getName()
    {
        return this.NAME;
    }
}
