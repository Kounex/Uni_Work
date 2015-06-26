package insanityFlyff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kounex on 26.06.15.
 */
public class IngameItem implements Serializable {
    private String itemName;
    private String imageURL;
    private List<Offer> offerList = new ArrayList<>();
    // -> Possible implementation of a/w price? Necessary?

    public IngameItem(String itemName, String imageURL) {
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

    public String getImageURL() {
        return this.imageURL;
    }

    public List<Offer> getOfferList() {
        return this.offerList;
    }

    @Override
    public String toString() {
        return this.itemName;
    }
}
