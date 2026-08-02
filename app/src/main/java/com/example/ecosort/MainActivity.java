
package com.example.ecosort;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecosort.APIResponse;
import com.example.ecosort.WasteService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewForWastes;

    EditText searchEditText;

    ArrayList<Waste> wasteArrayList = new ArrayList<>();

    WasteAdapter wasteAdapter;
    Button buttonAll,
            buttonGreen,
            buttonBlue,
            buttonRed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewForWastes =
                findViewById(R.id.recyclerViewForWastes);

        searchEditText =
                findViewById(R.id.searchEditText);
        buttonAll =
                findViewById(R.id.buttonAll);

        buttonGreen =
                findViewById(R.id.buttonGreen);

        buttonBlue =
                findViewById(R.id.buttonBlue);

        buttonRed =
                findViewById(R.id.buttonRed);


        recyclerViewForWastes.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false
                )
        );

        wasteAdapter = new WasteAdapter(wasteArrayList);

        recyclerViewForWastes.setAdapter(wasteAdapter);


        // SEARCH
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count) {

                wasteAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        buttonAll.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        wasteAdapter.filterByBinColor("All");

                    }
                }
        );

        buttonGreen.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        wasteAdapter.filterByBinColor("Green");

                    }
                }
        );

        buttonBlue.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        wasteAdapter.filterByBinColor("Blue");

                    }
                }
        );

        buttonRed.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        wasteAdapter.filterByBinColor("Red");

                    }
                }
        );


        // GET DATA FROM API

        WasteService wasteService =
                WasteService.getInstance();

        wasteService.getWastes().enqueue(
                new Callback<APIResponse>() {

                    @Override
                    public void onResponse(
                            Call<APIResponse> call,
                            Response<APIResponse> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            wasteArrayList.clear();

                            wasteArrayList.addAll(
                                    response.body().getWastes()
                            );

                            wasteAdapter.updateList(
                                    wasteArrayList
                            );

                            Toast.makeText(
                                    MainActivity.this,
                                    "Items: " + wasteArrayList.size(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }


                    @Override
                    public void onFailure(
                            Call<APIResponse> call,
                            Throwable t) {

                        Toast.makeText(
                                MainActivity.this,
                                "API Error: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.menu_main,
                menu
        );

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuSortAZ) {

            wasteAdapter.sortAZ();

            return true;
        }

        if (item.getItemId() == R.id.menuSortZA) {

            wasteAdapter.sortZA();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

