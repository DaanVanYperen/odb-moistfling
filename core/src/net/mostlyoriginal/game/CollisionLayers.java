package net.mostlyoriginal.game;

/**
 * Game specific layers.
 *
 * @author Daan van Yperen
 */
public class CollisionLayers {
    public static final long NOTHING = 0b000000000000000;
//    public static final long ITEM    = 0b000000000000001; // 1
    public static final long PLAYER  = 0b000000000000010; // 2
//    public static final long SLOT    = 0b000000000000100; // 4
    public static final long DOOR    = 0b000000000001000; // 8
    public static final long HOPPER  = 0b000000000010000; // 16
}
