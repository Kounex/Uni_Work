package insanityFlyff;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Kounex on 29.06.2015.
 */
public class SellHistory implements Serializable {
    private int perinGot;
    private int penyaGot;
    private int perinEach;
    private int penyaEach;
    private int amountSold;
    LocalDate dateSold;

    public SellHistory(int perinEach, int penyaEach, int amountSold) {
        this.perinEach = perinEach;
        this.penyaEach = penyaEach;
        this.amountSold = amountSold;
        if(penyaEach*amountSold>=100000000) {
            int perinsViaWrapAround = (penyaEach*amountSold)/100000000;
            this.perinGot = perinEach*amountSold+perinsViaWrapAround;
            this.penyaGot = penyaEach*amountSold-perinsViaWrapAround*100000000;
        } else {
            this.perinGot = perinEach*amountSold;
            this.penyaGot = penyaEach*amountSold;
        }
        this.dateSold = LocalDate.now();
    }

    @Override
    public String toString() {
        return this.amountSold + " sold: " + this.perinEach + " Perins, " + this.penyaEach + " Penya each <---> " + this.perinGot + " Perins, " + this.penyaGot + " Penya earned";
    }
}
