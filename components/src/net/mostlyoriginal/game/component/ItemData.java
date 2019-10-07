package net.mostlyoriginal.game.component;

/**
 * @author Daan van Yperen
 */
public class ItemData {
    public String id;
    public String sprite;
    public String identicalTo;
    public boolean consumed;
    public boolean reward;
    public int gold;
    public int rewardChance;
    public int desireChance;
    public boolean coveted=false;
    public boolean extensionPoint=false;
    public String extensionAcceptsAround;
    public String extensionAcceptsOnTop;
    public String extensionAcceptsInside;
    public int score;
    public boolean edible=false;
    public boolean defaultLocked=false;
    public boolean dryLand=false;
    public float revealEvery=0;
    public String machine;
    public String[] machineProducts;
}
