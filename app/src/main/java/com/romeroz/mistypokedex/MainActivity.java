package com.romeroz.mistypokedex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.EditText;

import com.romeroz.mistypokedex.adapters.PokedexAdapter;
import com.romeroz.mistypokedex.model.Pokemon;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchEditText;

    private RecyclerView mPokemonRecyclerView;
    private PokedexAdapter mPokedexAdapter;

    Realm mRealm;

    private ArrayList<Pokemon> mPokemonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();

        mPokemonList = null;
        // Get list of pokemon
        List<Pokemon> list =  mRealm.where(Pokemon.class).findAll();
        // Convert it to ArrayList so our adapter can use it
        mPokemonList = new ArrayList<>(list);


        /*int x = 0;
        for (Pokemon pokemon : mPokemonList) {
            x= x+1;
            Log.d("Roman", String.valueOf(x) + pokemon.getName() + " sp attack: " + pokemon.getBaseStats().getSpecialAttack() + " hp: " + pokemon.getBaseStats().getHp() + "type: " + pokemon.getType().get(0).getVal());

        }*/

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

        // Hide keyboard from popping up when activity starts
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close(); // Remember to close Realm when done.
    }
}
