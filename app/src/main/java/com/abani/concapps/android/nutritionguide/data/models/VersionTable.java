package com.abani.concapps.android.nutritionguide.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "version_table")
public class VersionTable {

    @SerializedName("id")
    @PrimaryKey
    private Integer id;
    @SerializedName("version")
    private Integer version;

    public VersionTable(Integer id, Integer version) {
        this.id = id;
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
