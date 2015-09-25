package insanityFlyff;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Kounex on 29.06.2015.
 */
public class SellHistory implements Serializable {
    private long perinGot;
    private long penyaGot;
    private long perinEach;
    private long penyaEach;
    private int amountSold;
    LocalDate dateSold;

    public SellHistory(int perinEach, long penyaEach, int amountSold) {
        this.perinEach = perinEach;
        this.penyaEach = penyaEach;
        this.amountSold = amountSold;
        if(penyaEach*amountSold>=100000000) {
            long perinsViaWrapAround = (penyaEach*amountSold)/100000000;
            this.perinGot = perinEach*amountSold+perinsViaWrapAround;
            this.penyaGot = penyaEach*amountSold-perinsViaWrapAround*100000000;
        } else {
            this.perinGot = perinEach*amountSold;
            this.penyaGot = penyaEach*amountSold;
        }
        this.dateSold = LocalDate.now();
    }

    public long getPerinGot() {
        return this.perinGot;
    }

    public long getPenyaGot() {
        return this.penyaGot;
    }

    public long getPerinEach() {
        return this.perinEach;
    }

    public long getPenyaEach() {
        return this.penyaEach;
    }

    @Override
    public String toString() {
        return this.amountSold + " sold:\t" + String.format("%,d",this.perinEach) + " Perins, " + String.format("%,d",this.penyaEach) + " Penya each\t\t[" + this.dateSold + "]\n\t\t" + this.perinGot + " Perins, " + this.penyaGot + " Penya earned";
    }
}
