package com.romeroz.mistypokedex.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Base extends RealmObject {
    @SerializedName("attack")
    private int attack;
    @SerializedName("defense")
    private int defense;
    @SerializedName("hp")
    private int hp;
    @SerializedName("special_attack")
    private int specialAttack;
    @SerializedName("special_defense")
    private int specialDefense;
    // Speed wouldn't parse if set as int for some reason
    @SerializedName("speed")
    private String speed;

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(int specialAttack) {
        this.specialAttack = specialAttack;
    }

    public int getSpecialDefense() {
        return specialDefense;
    }

    public void setSpecialDefense(int specialDefense) {
        this.specialDefense = specialDefense;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
