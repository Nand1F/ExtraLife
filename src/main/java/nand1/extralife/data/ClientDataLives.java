package nand1.extralife.data;

import nand1.extralife.capability.IHardcoreLives;

public class ClientDataLives {

    private static int lives = 0;
    private  String name;



    public static int getLives() {
        return lives;
    }


    public static void setLives(int value) {
        lives = value;
    }


    public static void removeLife() {
        lives--;
    }
}
