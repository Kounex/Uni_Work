package insanityFlyff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kounex on 26.06.15.
 */
public class IngameItem implements Serializable, Comparable {
    private String itemName;
    private String imageURL;
    private List<Offer> offerList = new ArrayList<>();
    private List<SellHistory> sellHistoryList = new ArrayList<>();
    int amountAvailable;
    int amountSold;
    int shopPerin;
    int shopPenya;
    private boolean auction;

    public IngameItem(int amountAvailable, boolean auction, String itemName, String imageURL) {
        this.amountAvailable = amountAvailable;
        this.auction = auction;
        this.itemName = itemName;
        this.imageURL = imageURL;
    }

    public void addOffer(int perin, int penya, String tradeItem, String bidderName) {
        this.offerList.add(new Offer(perin,penya,tradeItem,bidderName,this));
    }

    public void removeOffer(Offer offer) {
        this.offerList.remove(offer);
    }

    public void updateItemName(String itemName) {
        this.itemName = itemName;
    }

    public void updateImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void updateAmountAvailable(int amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public void updateShopPerin(int shopPerin) {
        this.shopPerin = shopPerin;
    }

    public void updateShopPenya(int shopPenya) {
        this.shopPenya = shopPenya;
    }

    public void updateSellHistory(int perinEach, int penyaEach, int amountSold) {
        this.amountAvailable -= amountSold;
        this.amountSold += amountSold;
        this.sellHistoryList.add(new SellHistory(perinEach, penyaEach, amountSold));
    }

    public int getAmountAvailable() {
        return this.amountAvailable;
    }

    public int getAmountSold() {
        return this.amountSold;
    }

    public int getShopPerin() {
        return this.shopPerin;
    }

    public int getShopPenya() {
        return this.shopPenya;
    }

    public String getItemName() {
        return this.itemName;
    }

    public boolean getAuctionState() {
       return this.auction;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public List<Offer> getOfferList() {
        return this.offerList;
    }

    public List<SellHistory> getSellHistoryList() {
        return this.sellHistoryList;
    }

    @Override
    public String toString() {
        return this.itemName;
    }

    @Override
    public int compareTo(Object o) {
        int integerResult = ((Integer) this.getAmountAvailable()).compareTo(((IngameItem) o).getAmountAvailable());
        if(integerResult==0) {
            return this.itemName.compareTo(((IngameItem) o).getItemName());
        } else {
            return integerResult;
        }
    }
}
