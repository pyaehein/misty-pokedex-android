package com.romeroz.mistypokedex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

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
import com.romeroz.mistypokedex.adapters.PokedexAdapter;
import com.romeroz.mistypokedex.model.Pokemon;
import com.romeroz.mistypokedex.model.RealmString;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mPokemonRecyclerView;
    private PokedexAdapter mPokedexAdapter;

    Realm mRealm;

    private ArrayList<Pokemon> mPokemonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_view);
        mainLayout.requestFocus();

        mRealm = Realm.getDefaultInstance();

        mRealm.beginTransaction();
        mRealm.deleteAll();
        mRealm.commitTransaction();

        loadPokemon();

        mPokemonList = null;
        // Get list of pokemon
        List<Pokemon> list =  mRealm.where(Pokemon.class).findAll();
        // Convert it to ArrayList so our adapter can use it
        mPokemonList = new ArrayList<>(list);


        int x = 0;
        for (Pokemon pokemon : mPokemonList) {
            x= x+1;
            Log.d("Roman", String.valueOf(x) + pokemon.getName() + " sp attack: " + pokemon.getBaseStats().getSpecialAttack() + " hp: " + pokemon.getBaseStats().getHp() + "type: " + pokemon.getType().get(0).getVal());

        }

        // Set up RecyclerView
        mPokemonRecyclerView = (RecyclerView) findViewById(R.id.pokedex_recycler_view);
        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mPokemonRecyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mPokemonRecyclerView.setLayoutManager(linearLayoutManager);
        // If using nested scroll view, set to false to enable smooth scrolling
        mPokemonRecyclerView.setNestedScrollingEnabled(false);

        // Set up our RecyclerView's Adapter
        mPokedexAdapter = new PokedexAdapter(MainActivity.this);
        mPokemonRecyclerView.setAdapter(mPokedexAdapter);

        mPokedexAdapter.swapData(mPokemonList);


    }

    //See: https://github.com/realm/realm-java -> Examples -> GridviewExample for source
    private void loadPokemon() {
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
