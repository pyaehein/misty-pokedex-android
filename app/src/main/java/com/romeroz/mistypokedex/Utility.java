package com.romeroz.mistypokedex;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utility {

    public static final String APP_TAG = "Pokedex";

    /**
     * Hide soft keyboard - Use this to hide keyboard when unsure of the current View
     * Usage: Utility.hideSoftKeyboard(MainActivity.this);
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //F ind the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        // If no view currently has focus, create a new one, just so we can grab a window token from it
        if(view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Takes int as "1" and returns string "#001"
     * @param pokemonId
     * @return
     */
    public static String getPokemonIdStringFromInt(int pokemonId){
        String formattedId = String.format("%03d", pokemonId);
        return "#" + String.valueOf(formattedId);
    }

    /**
     * Get the image name based on type of pokemon
     * @param type
     * @return
     */
    public static String getTypeImageName(String type){
        String typeImage = "";

        switch (type) {
            case "Normal":
                typeImage = "type_normal";
                break;
            case "Fire":
                typeImage = "type_fire";
                break;
            case "Fighting":
                typeImage = "type_fighting";
                break;
            case "Water":
                typeImage = "type_water";
                break;
            case "Poison":
                typeImage = "type_poison";
                break;
            case "Electric":
                typeImage = "type_electric";
                break;
            case "Ground":
                typeImage = "type_ground";
                break;
            case "Grass":
                typeImage = "type_grass";
                break;
            case "Flying":
                typeImage = "type_flying";
                break;
            case "Ice":
                typeImage = "type_ice";
                break;
            case "Bug":
                typeImage = "type_bug";
                break;
            case "Psychic":
                typeImage = "type_psychic";
                break;
            case "Rock":
                typeImage = "type_rock";
                break;
            case "Dragon":
                typeImage = "type_dragon";
                break;
            case "Ghost":
                typeImage = "type_ghost";
                break;
            case "Dark":
                typeImage = "type_dark";
                break;
            case "Steel":
                typeImage = "type_steel";
                break;
            case "Fairy":
                typeImage = "type_fairy";
                break;
        }

        return typeImage;
    }

}
