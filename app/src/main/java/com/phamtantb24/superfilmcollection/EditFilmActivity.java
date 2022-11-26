package com.phamtantb24.superfilmcollection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.phamtantb24.superfilmcollection.databinding.ActivityEditFilmBinding;
import com.phamtantb24.superfilmcollection.model.Film;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditFilmActivity extends AppCompatActivity {

    private ActivityEditFilmBinding binding;
    private final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Films");
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditFilmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Film film = null;

        if (getIntent() != null && getIntent().hasExtra("filmEdit")) {
            film = (Film)getIntent().getSerializableExtra("filmEdit");
            binding.updateTitle.setText(film.getTitle());
            binding.updateReleaseDay.setText(film.getReleaseDay());
            binding.updateDirector.setText(film.getDirector());
            binding.updateStarring.setText(film.getActors().toString().replace("[","")
                    .replace("]",""));
            binding.updateLanguage.setText(film.getLanguage());
            binding.updateCountry.setText(film.getCountry());
            binding.updateRunningTime.setText(String.valueOf(film.getRunningTime()));
            binding.progressBarUpdate.setVisibility(View.INVISIBLE);
            Glide
                    .with(getApplicationContext())
                    .load(film.getBanner())
                    .centerCrop()
                    .into(binding.imageUpdate);

            binding.backDetailFilm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        binding.imageUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        Film finalFilm = film;
        binding.buttonUpdateFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.updateTitle.getText().toString().trim();
                String releaseDay = binding.updateReleaseDay.getText().toString().trim();
                String director = binding.updateDirector.getText().toString().trim();
                String starring = binding.updateStarring.getText().toString().trim();
                String language = binding.updateLanguage.getText().toString().trim();
                String country = binding.updateCountry.getText().toString().trim();
                String runningTime = binding.updateRunningTime.getText().toString().trim();
                if (title.isEmpty() || releaseDay.isEmpty() || starring.isEmpty()
                        || director.isEmpty() || language.isEmpty()
                        || country.isEmpty() || runningTime.isEmpty())
                    Toast.makeText(getApplicationContext(), "Please fill all fields !", Toast.LENGTH_SHORT).show();
                else if (imageUri == null)
                    Toast.makeText(getApplicationContext(), "Please choose picture !", Toast.LENGTH_SHORT).show();
                else {
                    List<String> listActors = new ArrayList<>(Arrays.asList(
                            starring.split(",")
                    ));
                    Film film1 = new Film(finalFilm.getUuid(),title,releaseDay,listActors,director,language,finalFilm.getBanner(),Integer.parseInt(runningTime),country);
                    uploadToFirebase(film1,imageUri);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            binding.imageUpdate.setImageURI(imageUri);
        }
    }

    private void uploadToFirebase(Film filmUpload, Uri imgUri) {
        final StorageReference fileRef = reference.child(imgUri.getLastPathSegment()+"." + getFileExtension(imgUri));
        fileRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        filmUpload.setBanner(uri.toString());
                        Map<String, Object> filmUpdate = filmUpload.toMap();
                        myRef.child(filmUpload.getUuid()).updateChildren(filmUpdate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(),"Update Succeed !",Toast.LENGTH_SHORT)
                                                .show();
                                        android.app.AlertDialog.Builder builder1 = new AlertDialog.Builder(EditFilmActivity.this);
                                        builder1.setMessage("Edit Succeed, You want back to Home ?");
                                        builder1.setCancelable(true);
                                        builder1.setPositiveButton(
                                                "Yes",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        finish();
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
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"Update Failure !",Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                        binding.progressBarUpdate.setVisibility(View.INVISIBLE);
                        binding.imageUpdate.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                binding.progressBarUpdate.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.progressBarUpdate.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}