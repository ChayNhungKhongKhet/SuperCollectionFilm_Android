package com.phamtantb24.superfilmcollection.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FilmDao {
    private final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    public boolean remove(String id) {

        return false;
    }
}
