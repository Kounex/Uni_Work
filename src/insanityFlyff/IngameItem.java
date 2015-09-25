package insanityFlyff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kounex on 26.06.15.
 */
public class IngameItem implements Serializable, Comparable {
    private String itemName;
    private List<String> imageURLS = new ArrayList<>();
    private List<Offer> offerList = new ArrayList<>();
    private Offer offerWon;
    private List<SellHistory> sellHistoryList = new ArrayList<>();
    private int amountAvailable;
    private int amountSold;
    private long shopPerin;
    private long shopPenya;
    private boolean auction;

    /**
     * Old version, this constructor only added one image to the list (has been a string only before)
     * after multiple image function had been implemented this has become unnecessary
     */
//    public IngameItem(int amountAvailable, boolean auction, String itemName, String imageURL) {
//        this.amountAvailable = amountAvailable;
//        this.auction = auction;
//        this.itemName = itemName;
//        this.imageURLS.add(imageURL);
//    }

    public IngameItem(int amountAvailable, boolean auction, String itemName, List<String> imageURLS) {
        this.amountAvailable = amountAvailable;
        this.auction = auction;
        this.itemName = itemName;
        this.imageURLS = imageURLS;
    }

    public void addOffer(long perin, long penya, String tradeItem, String bidderName) {
        this.offerList.add(new Offer(perin,penya,tradeItem,bidderName,this));
    }

    public void removeOffer(Offer offer) {
        this.offerList.remove(offer);
    }

    public void updateItemName(String itemName) {
        this.itemName = itemName;
    }

    public void updateImageURL(String imageURL_OLD, String imageURL_NEW) {
//        this.imageURLS.forEach(s -> {
//           if(imageURL_OLD.equals(s)) {
//               s = imageURL_NEW;
//           }
//        });
        int oldPlace = this.imageURLS.indexOf(imageURL_OLD);
        this.imageURLS.remove(imageURL_OLD);
        this.imageURLS.add(oldPlace, imageURL_NEW);
    }

    public void deleteSingleImageURL(String imageURL) {
        this.imageURLS.remove(imageURL);
    }

    public void addImageURL(String imageURL) {
        this.imageURLS.add(imageURL);
    }

    public void deleteImageURLS() {
        this.imageURLS = new ArrayList<>();
    }

    public void updateAmountAvailable(int amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public void updateShopPerin(long shopPerin) {
        this.shopPerin = shopPerin;
    }

    public void updateShopPenya(long shopPenya) {
        this.shopPenya = shopPenya;
    }

    public void updateSellHistory(long perinEach, long penyaEach, int amountSold) {
        this.amountAvailable -= amountSold;
        this.amountSold += amountSold;
        this.sellHistoryList.add(new SellHistory(perinEach, penyaEach, amountSold));
    }

    public void updateOfferWon(Offer offerWon) {
        this.offerWon = offerWon;
    }

    public Offer getOfferWon() {
        return this.offerWon;
    }

    public int getAmountAvailable() {
        return this.amountAvailable;
    }

    public int getAmountSold() {
        return this.amountSold;
    }

    public long getShopPerin() {
        return this.shopPerin;
    }

    public long getShopPenya() {
        return this.shopPenya;
    }

    public String getItemName() {
        return this.itemName;
    }

    public boolean getAuctionState() {
       return this.auction;
    }

    public List<String> getImageURLS() {
        return this.imageURLS;
    }

    public List<Offer> getOfferList() {
        return this.offerList;
    }

    public List<SellHistory> getSellHistoryList() {
        return this.sellHistoryList;
    }

    @Override
    public String toString() {
        if(this.amountAvailable > 0) {
            return this.itemName;
        } else {
            return "[SOLD] " + this.itemName;
        }
    }

    @Override
    public int compareTo(Object o) {
        int integerResult = ((Integer) this.getAmountAvailable()).compareTo(((IngameItem) o).getAmountAvailable());
        if(integerResult==0) {
            return this.itemName.compareTo(((IngameItem) o).getItemName());
        } else {
            return integerResult*-1;
        }
    }
}
