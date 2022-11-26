package com.phamtantb24.superfilmcollection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phamtantb24.superfilmcollection.databinding.ActivityHomeBinding;
import com.phamtantb24.superfilmcollection.model.Film;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends DrawerBaseActivity {

    private ActivityHomeBinding activityHomeBinding;
    private FilmAdapter filmAdapter;
    private List<Film> films;
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(activityHomeBinding.getRoot());
        allocateActivityTitle("Home");
        films = new ArrayList<>();
        filmAdapter = new FilmAdapter(films, R.layout.item_film, this);
        activityHomeBinding.listFilms.setAdapter(filmAdapter);
        myRef.child("Films").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Film filmGet = snapshot.getValue(Film.class);
                films.add(filmGet);
                filmAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Film film = snapshot.getValue(Film.class);
                if(film != null) {
                    for (int i=0;i<films.size();i++) {
                        if (films.get(i).getUuid().equals(film.getUuid()))
                            films.set(i,film);
                    }
                }
                filmAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIREBASE-ERROR", error.toString());
            }
        });
        activityHomeBinding.listFilms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FilmDetailActivity.class);
                intent.putExtra("film", films.get(position));
                startActivity(intent);
            }
        });

        activityHomeBinding.listFilms.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Use popup menu
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_crud, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                startActivity(new Intent(HomeActivity.this, EditFilmActivity.class)
                                        .putExtra("filmEdit", films.get(position)));
                                return true;
                            case R.id.remove: // Show dialog confirm
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeActivity.this);
                                builder1.setMessage("Are you sure remove this item ?");
                                builder1.setCancelable(true);
                                builder1.setPositiveButton(
                                        "Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                myRef.child("Films").child(films.get(position).getUuid()).removeValue()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(getApplicationContext(),"Remove succeed",Toast.LENGTH_SHORT)
                                                                        .show();
                                                                films.remove(position);
                                                                filmAdapter.notifyDataSetChanged();
                                                            }
                                                        }).addOnCanceledListener(new OnCanceledListener() {
                                                            @Override
                                                            public void onCanceled() {
                                                                Toast.makeText(getApplicationContext(),"Remove fail",Toast.LENGTH_SHORT)
                                                                        .show();
                                                            }
                                                        });
                                                dialog.cancel();
                                            }
                                        });

                                builder1.setNegativeButton(
                                        "No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        }).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.setForceShowIcon(true);
                popupMenu.show();
                return true;
            }
        });

        activityHomeBinding.addFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //forward tp AddFilmActivity
                Intent intent = new Intent(getApplicationContext(), AddFilmActivity.class);
                startActivity(intent);
            }
        });
    }

}