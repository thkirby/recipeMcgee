package us.ait.android.shoppinglist.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import us.ait.android.shoppinglist.R;

@Entity
public class Item implements Serializable{
    public enum ItemType{
        GROCERY(0, R.drawable.grocery),
        CLOTHING(1,R.drawable.clothing), ELECTRONICS(2,R.drawable.electronics);

        private int value;
        private int iconId;

        ItemType(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public int getValue() {
            return value;
        }

        public int getIconId() {
            return iconId;
        }

        public static ItemType fromInt(int value){
            for (ItemType item: ItemType.values()) {
                if(item.value == value){
                    return item;
                }

            }
            return GROCERY;
        }
    }

    @PrimaryKey(autoGenerate = true)
    private long itemID;

    private String itemName;
    private String description;
    private double estimatedPrice;
    private int itemType;
    private boolean bought;

    public Item(String itemName, String description, double estimatedPrice, int itemType, boolean bought) {
        this.itemName = itemName;
        this.description = description;
        this.estimatedPrice = estimatedPrice;
        this.itemType = itemType;
        this.bought = bought;
    }

    public long getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }

    public double getEstimatedPrice() {
        return estimatedPrice;
    }

    public int getItemType() {
        return itemType;
    }

    public boolean isBought() {
        return bought;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEstimatedPrice(double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public ItemType getItemTypeAsEnum(){
        return ItemType.fromInt(itemType);
    }
}
