import java.util.Random;

public class Die {
    private static char nextDieNum = 'a';
    private final Random randomGen = new Random();
    private final char dieNum;
    private final int sides;

    private boolean isHeld;
    private int faceValue;


    public Die(int sides) {
        this.dieNum = nextDieNum++;
        this.sides = sides;

        rollDie();
    }

    public void rollDie() {
        if (!isHeld) {
            faceValue = randomGen.nextInt(sides) + 1;
        }
    }

    public char getDieNum() {
        return dieNum;
    }

    public void holdDie() {
        this.isHeld = true;
    }

    public boolean isBeingHeld() {
        return this.isHeld;
    }

    public void resetDie() {
        this.isHeld = false;
    }

    public String toString() {
        return dieNum + ". " + getFaceValue() + (isHeld ? " (H)" : " (-)");
    }

    public int getFaceValue() {
        return faceValue;
    }
}
