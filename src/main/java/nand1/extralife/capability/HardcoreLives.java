package nand1.extralife.capability;

import nand1.extralife.config.ModConfigs;

public class HardcoreLives implements IHardcoreLives {
    private int lives;

    public HardcoreLives() {
      lives = 0;
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public void setLives(int lives) {
        this.lives = lives;
    }

    @Override
    public void removeLife() {
        lives--;
    }

}
