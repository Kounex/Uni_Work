package insanityFlyff;

import java.io.Serializable;

/**
 * Created by Kounex on 26.06.15.
 *
 * Class to represent a offer that has been made by a player
 *
 * int perin: amount of perins offered
 * int penya: amount of penya offered
 * String bidderName: name of the player who placed the bid
 */
public class Offer implements Serializable{
    private int perin;
    private int penya;
    private String tradeItems;
    private String bidderName;
    private IngameItem item;

    public Offer(int perin, int penya, String tradeItems, String bidderName, IngameItem item) {
        this.perin = perin;
        this.penya = penya;
        this.tradeItems = tradeItems;
        this.bidderName = bidderName;
        this.item = item;
    }

    public int getPerin() {
        return this.perin;
    }

    public int getPenya() {
        return this.penya;
    }

    public String getBidderName() {
        return this.bidderName;
    }

    @Override
    public String toString() {
        return this.perin + " Perins; " + this.penya + " Penya; + " + this.tradeItems + " | by: " + this.bidderName;
    }
}
