
package com.abani.concapps.android.nutritionguide.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "preferred_foods")
public class Food implements Parcelable {

    @SerializedName("id")
    @PrimaryKey
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("scientificName")
    @ColumnInfo(name = "scientific_name")
    private String scientificName;

    @SerializedName("description")
    private String description;

    @SerializedName("quantityOfNutrients")
    @ColumnInfo(name = "quantity_of_nutrients")
    private Map<String, String> quantityOfNutrients = new HashMap<>();

    @SerializedName("imageUrl")
    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @SerializedName("wikiUrl")
    @ColumnInfo(name = "wiki_url")
    private String wikiUrl;

    @Ignore
    public Food() {
    }

    @Ignore
    protected Food(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        scientificName = in.readString();
        description = in.readString();
        quantityOfNutrients = in.readHashMap(String.class.getClassLoader());
        imageUrl = in.readString();
        wikiUrl = in.readString();
    }

    public Food(Integer id, String name, String scientificName, String description, Map<String, String> quantityOfNutrients, String imageUrl, String wikiUrl) {
        this.id = id;
        this.name = name;
        this.scientificName = scientificName;
        this.description = description;
        this.quantityOfNutrients = quantityOfNutrients;
        this.imageUrl = imageUrl;
        this.wikiUrl = wikiUrl;
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getQuantityOfNutrients() {
        return quantityOfNutrients;
    }

    public void setQuantityOfNutrients(Map<String, String> quantityOfNutrients) {
        this.quantityOfNutrients = quantityOfNutrients;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(scientificName);
        dest.writeString(description);
        dest.writeMap(quantityOfNutrients);
        dest.writeString(imageUrl);
        dest.writeString(wikiUrl);
    }
}
