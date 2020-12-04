package com.example.pas_11rpl2_risal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListTeams extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterTeams adapter;
    private ArrayList<ModelTeams> DataArrayList; //kit add kan ke main_menu
    private ImageView tambah_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_teams);
        recyclerView = (RecyclerView)findViewById(R.id.rvdata);

        addDataOnline();

    }
    void addDataOnline() {
        //data online
        AndroidNetworking.get("https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?l=English%20Premier%20League")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        Log.d("hasiljson", "onResponse: " + response.toString());
                        //jika sudah berhasil debugm lanjutkan code dibawah ini
                        DataArrayList = new ArrayList<>();
                        ModelTeams modelku;
                        try {
                            Log.d("hasiljson", "onResponse: " + response.toString());
                            JSONArray jsonArray = response.getJSONArray("teams");
                            Log.d("hasiljson2", "onResponse: " + jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                modelku = new ModelTeams();
                                modelku.setIdTeams(jsonObject.getInt("idTeam"));
                                modelku.setStrTeam(jsonObject.getString("strTeam"));
                                modelku.setIntFormedYear(jsonObject.getString("intFormedYear"));
                                modelku.setStrTeamBadge(jsonObject.getString("strTeamBadge"));
                                modelku.setStrDesc(jsonObject.getString("strDescriptionEN"));
                                modelku.setStrLeague(jsonObject.getString("strLeague"));
                                DataArrayList.add(modelku);

                            }
                            //untuk handle click
                            adapter = new AdapterTeams(DataArrayList, new AdapterTeams.Callback() {
                                @Override
                                public void onClick(int position) {
                                    ModelTeams movie =  DataArrayList.get(position);
                                    Intent intent = new Intent(getApplicationContext(), DetailTeams.class);
                                    intent.putExtra("judul", movie.strTeam);
                                    intent.putExtra("date", movie.intFormedYear);
                                    intent.putExtra("deskripsi", movie.strDesc);
                                    intent.putExtra("path", movie.strTeamBadge);
                                    startActivity(intent);
                                    Toast.makeText(com.example.pas_11rpl2_risal.ListTeams.this, "" + position, Toast.LENGTH_LONG);
                                }

                                @Override
                                public void test() {

                                }
                            });
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(com.example.pas_11rpl2_risal.ListTeams.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("errorku", "onError errorCode : " + error.getErrorCode());
                        Log.d("errorku", "onError errorBody : " + error.getErrorBody());
                        Log.d("errorku", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

}
