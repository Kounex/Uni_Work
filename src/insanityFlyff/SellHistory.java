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

    public int getPerinGot() {
        return this.perinGot;
    }

    public int getPenyaGot() {
        return this.penyaGot;
    }

    public int getPerinEach() {
        return this.perinEach;
    }

    public int getPenyaEach() {
        return this.penyaEach;
    }

    @Override
    public String toString() {
        return this.amountSold + " sold:\t" + this.perinEach + " Perins, " + this.penyaEach + " Penya each\t\t[" + this.dateSold + "]\n\t\t" + this.perinGot + " Perins, " + this.penyaGot + " Penya earned";
    }
}