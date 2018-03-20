public class Inventory {
    private int numOre;

    public Inventory(int numOre) {
        this.numOre = numOre;
    }

    public int getNumOre() { return numOre; }
    public void addOre() { numOre++; }
    public void decrement5() { numOre -= 5; }
}
