package insanityFlyff;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.time.LocalDate;

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
    private long perin;
    private long penya;
    private String tradeItems;
    private String bidderName;
    private LocalDate dateOffered;
    private LocalDate dateOfferAccepted;
    private IngameItem item;

    public Offer(long perin, long penya, String tradeItems, String bidderName, IngameItem item) {
        if(penya >= 100000000) {
            long perinsViaWrapAround = penya/100000000;
            this.perin = perin + perinsViaWrapAround;
            this.penya = penya - (perinsViaWrapAround*100000000);
        } else {
            this.perin = perin;
            this.penya = penya;
        }
        this.tradeItems = tradeItems;
        this.bidderName = bidderName;
        this.item = item;
        this.dateOffered = LocalDate.now();
    }

    public long getPerin() {
        return this.perin;
    }

    public long getPenya() {
        return this.penya;
    }

    public String getTradeItems() {
        return this.tradeItems;
    }

    public String getBidderName() {
        return this.bidderName;
    }

    public LocalDate getDateOfferAccepted() {
        return this.dateOfferAccepted;
    }

    public void setDateOfferAccepted(LocalDate date) {
        this.dateOfferAccepted = date;
    }

    @Override
    public String toString() {
        if(this.dateOfferAccepted == null) {
            if (!this.tradeItems.isEmpty()) {
                return String.format("%,d",this.perin) + " Perins; " + String.format("%,d",this.penya) + " Penya ----> by: " + this.bidderName + "\t\t[" + this.dateOffered + "]\n+ " + this.tradeItems;
            } else {
                return String.format("%,d",this.perin) + " Perins; " + String.format("%,d",this.penya) + " Penya ----> by: " + this.bidderName + "\t\t[" + this.dateOffered + "]\n+ No items";
            }
        } else {
            if (!this.tradeItems.isEmpty()) {
                return String.format("%,d",this.perin) + " Perins; " + String.format("%,d",this.penya) + " Penya ----> by: " + this.bidderName + "\t[" + this.dateOfferAccepted + "]\n+ " + this.tradeItems;
            } else {
                return String.format("%,d",this.perin) + " Perins; " + String.format("%,d",this.penya) + " Penya ----> by: " + this.bidderName + "\t[" + this.dateOfferAccepted + "]\n+ No items";
            }
        }
    }
}
