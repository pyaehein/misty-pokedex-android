package com.romeroz.mistypokedex;

import android.app.Application;
import android.content.ContextWrapper;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.pixplicity.easyprefs.library.Prefs;
import com.romeroz.mistypokedex.model.Pokemon;
import com.romeroz.mistypokedex.model.RealmString;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmObject;

public class MyApplication extends Application {

    public static String POKEMON_DATA_LOADED_KEY;
    Realm mRealm;

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * Instantiate Realm with default configuration
         */
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        /**
         * Initialize EasyPreferences - See: https://github.com/Pixplicity/EasyPreferences
         */
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        /**
         * Check if this is the first time launching the app. If so, we need to load pokemon data into Realm
         */
        Boolean dataLoaded = Prefs.getBoolean(POKEMON_DATA_LOADED_KEY, false);

        if(!dataLoaded){

            mRealm = Realm.getDefaultInstance();

            // Clear all data
            mRealm.beginTransaction();
            mRealm.deleteAll();
            mRealm.commitTransaction();

            loadPokemonData();

            Prefs.putBoolean(POKEMON_DATA_LOADED_KEY, true);
        }

    }

    // See: https://github.com/realm/realm-java -> Examples -> GridviewExample for source
    private void loadPokemonData() {
        // In this case we're loading from local assets.
        // NOTE: could alternatively easily load from network
        InputStream stream;
        try {
            stream = getAssets().open("pokedata/pokedex.json");
        } catch (IOException e) {
            Log.d(Utility.APP_TAG,"Could not load pokemon data");
            return;
        }

        // Realm does not currently support lists of primitives. This is a work around.
        // Normally you would just use: Gson gson = new GsonBuilder().create();
        // See: http://stackoverflow.com/a/28129461/3075340
        Type token = new TypeToken<RealmList<RealmString>>(){}.getType();
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(token, new TypeAdapter<RealmList<RealmString>>() {

                    @Override
                    public void write(JsonWriter out, RealmList<RealmString> value) throws IOException {
                        // Ignore
                    }

                    @Override
                    public RealmList<RealmString> read(JsonReader in) throws IOException {
                        RealmList<RealmString> list = new RealmList<RealmString>();
                        in.beginArray();
                        while (in.hasNext()) {
                            list.add(new RealmString(in.nextString()));
                        }
                        in.endArray();
                        return list;
                    }
                })
                .create();

        // Get JSON from stream
        JsonElement json = new JsonParser().parse(new InputStreamReader(stream));

        // Convert JSON to objects as normal
        List<Pokemon> pokemon = gson.fromJson(json, new TypeToken<List<Pokemon>>(){}.getType());

        // Open a transaction to store items into the realm
        // Use copyToRealm() to convert the objects into proper RealmObjects managed by Realm.
        mRealm.beginTransaction();
        Collection<Pokemon> pokemonList = mRealm.copyToRealm(pokemon);
        mRealm.commitTransaction();
    }
}