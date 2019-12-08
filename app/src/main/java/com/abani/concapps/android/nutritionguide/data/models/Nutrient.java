
package com.abani.concapps.android.nutritionguide.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "nutrient")
public class Nutrient implements Parcelable {

    @SerializedName("id")
    @PrimaryKey
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("foods")
    private List<Food> foods = new ArrayList<>();

    @SerializedName("alsoKnownAs")
    @ColumnInfo(name = "also_known_as")
    private List<String> alsoKnownAs = null;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("wikiUrl")
    @ColumnInfo(name = "wiki_url")
    private String wikiUrl;

    public Nutrient(Integer id, String name, String description, List<Food> foods, List<String> alsoKnownAs, String symbol, String wikiUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.foods = foods;
        this.alsoKnownAs = alsoKnownAs;
        this.symbol = symbol;
        this.wikiUrl = wikiUrl;
    }

    protected Nutrient(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        description = in.readString();
        foods = in.createTypedArrayList(Food.CREATOR);
        alsoKnownAs = in.createStringArrayList();
        symbol = in.readString();
        wikiUrl = in.readString();
    }

    public static final Creator<Nutrient> CREATOR = new Creator<Nutrient>() {
        @Override
        public Nutrient createFromParcel(Parcel in) {
            return new Nutrient(in);
        }

        @Override
        public Nutrient[] newArray(int size) {
            return new Nutrient[size];
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public List<String> getAlsoKnownAs() {
        return alsoKnownAs;
    }

    public void setAlsoKnownAs(List<String> alsoKnownAs) {
        this.alsoKnownAs = alsoKnownAs;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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
        dest.writeString(description);
        dest.writeTypedList(foods);
        dest.writeStringList(alsoKnownAs);
        dest.writeString(symbol);
        dest.writeString(wikiUrl);
    }
}
