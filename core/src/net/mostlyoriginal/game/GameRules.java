package net.mostlyoriginal.game;

import com.badlogic.gdx.audio.Music;
import net.mostlyoriginal.game.component.Score;

/**
 * @author Daan van Yperen
 */
public abstract class GameRules {
    public static final int WALKING_SPEED_VISITORS = 50;

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int CAMERA_ZOOM = 2;
    public static final int CELL_SIZE = 16;

    public static final int LAYER_DIALOG_TEXT = 2010;
    public static final int LAYER_DIALOG_BOX= 2000;
    public static final int LAYER_SHOPPER = 900;
    public static final int LAYER_ITEM = 750;
    public static final int LAYER_SLOTS = LAYER_ITEM + GameRules.SCREEN_HEIGHT;
    public static final int LAYER_PLAYER = LAYER_SLOTS+100;
    public static final int LAYER_ITEM_CARRIED = LAYER_PLAYER+100;
    public static final int LAYER_SHADOWS = 550;
    public static final int LAYER_MACHINES = 500;
    public static final int LAYER_DESIRE_INDICATOR = LAYER_PLAYER+200;
    public static final int LAYER_SCORE_TEXT = 60;
    public static final int LAYER_WINDOWS = 50;
    public static final int LAYER_DOORS = 50;

    public static final int LAYER_PAYMENT = 2000;
    public static final int LAYER_INGREDIENT_HINTS = 600;

    public static final boolean DEBUG_ENABLED = false;
    public static final boolean MUSIC_ENABLED = true;
    public static final boolean LOGO_ENABLED = true;
    public static final int VISITORS_EACH_DAY = 20;


    public static int lastScore = -1;
    public static int level = 0;
    public static Music music;
    public static boolean sfxOn=true;
    public static boolean musicOn=true;
    public static String nextMap;
    public static String currentMap;

    public static Score score = new Score();
}
