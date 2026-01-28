package nand1.extralife.data;

import nand1.extralife.capability.IHardcoreLives;

public class ClientDataLives {

    private static int lives = 0;
    private  String name;
    private static boolean isHardcore = false;



    public static int getLives() {
        return lives;
    }


    public static void setLives(int value) {
        lives = value;
    }


    public static void removeLife() {
        lives--;
    }

    public static boolean getIsHardcore() {
        return isHardcore;
    }

    public static void setIsHardcore(boolean value) {
        isHardcore = value;
    }

}
