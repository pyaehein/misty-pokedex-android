package com.romeroz.mistypokedex.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.romeroz.mistypokedex.R;
import com.romeroz.mistypokedex.Utility;
import com.romeroz.mistypokedex.adapters.PokedexAdapter;
import com.romeroz.mistypokedex.model.Pokemon;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

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

        mSearchEditText = (EditText) findViewById(R.id.search_edit_text);
        mSearchEditText.addTextChangedListener(new SearchTextWatcher());


        mRealm = Realm.getDefaultInstance();

        mPokemonList = null;
        // Get list of pokemon
        List<Pokemon> list =  mRealm.where(Pokemon.class).findAll();
        // Convert it to ArrayList so our adapter can use it
        mPokemonList = new ArrayList<>(list);


        // Debug code
        /*int x = 0;
        for (Pokemon pokemon : mPokemonList) {
            x= x+1;
            Log.d(Utility.APP_TAG, String.valueOf(x) + " " + pokemon.getBaseStats().getSpeed());
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

        // Remember to set android:imeOptions="actionSearch" in the last EditText
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_SEARCH) {
                    // Hide keyboard when search IME button pressed
                    Utility.hideSoftKeyboard(MainActivity.this);
                    return true;
                }
                return false;
            }
        });

    }

    // Execute code after text has changed in an EditText
    private class SearchTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            final String searchText = s.toString();
            final int textLength = s.length();

            if (searchText.isEmpty()){
                mPokedexAdapter.swapData(mPokemonList);
                return;
            }

            if(textLength >= 1){
                Log.d("Roman", "textLength: " + String.valueOf(textLength));
                RealmResults<Pokemon> searchPokemonList = mRealm.where(Pokemon.class).contains("name", searchText, Case.INSENSITIVE).findAll();
                ArrayList<Pokemon> searchPokemonArrayList = new ArrayList<>(searchPokemonList);
                mPokedexAdapter.swapData(searchPokemonArrayList);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close(); // Remember to close Realm when done.
    }
}
