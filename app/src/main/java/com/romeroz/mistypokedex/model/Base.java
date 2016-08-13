package com.romeroz.mistypokedex.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Note: property names must match case of json!
 */
public class Base extends RealmObject {
    @SerializedName("attack")
    private String attack;
    @SerializedName("defense")
    private String defense;
    @SerializedName("hp")
    private String hp;
    @SerializedName("special_attack")
    private String specialAttack;
    @SerializedName("special_defense")
    private String specialDefense;
    @SerializedName("speed")
    private String speed;

    public String getAttack() {
        return attack;
    }

    public void setAttack(String attack) {
        this.attack = attack;
    }

    public String getDefense() {
        return defense;
    }

    public void setDefense(String defense) {
        this.defense = defense;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(String specialAttack) {
        this.specialAttack = specialAttack;
    }

    public String getSpecialDefense() {
        return specialDefense;
    }

    public void setSpecialDefense(String specialDefense) {
        this.specialDefense = specialDefense;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
