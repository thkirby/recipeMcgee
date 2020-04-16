package us.ait.android.shoppinglist.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import us.ait.android.shoppinglist.R;

@Entity
public class Pantry {
    @PrimaryKey
    public int id;

    public String items[];

}