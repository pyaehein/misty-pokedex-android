package com.romeroz.mistypokedex.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Note: property names must match case of json!
 */
public class Pokemon extends RealmObject {
    private int id;
    @SerializedName("ename")
    private String name;
    @SerializedName("base")
    private Base baseStats;
    private RealmList<RealmString> type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Base getBaseStats() {
        return baseStats;
    }

    public void setBaseStats(Base baseStats) {
        this.baseStats = baseStats;
    }

    public RealmList<RealmString> getType() {
        return type;
    }

    public void setType(RealmList<RealmString> type) {
        this.type = type;
    }
}

