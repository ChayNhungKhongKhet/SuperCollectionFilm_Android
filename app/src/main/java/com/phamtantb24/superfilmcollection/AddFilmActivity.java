package com.phamtantb24.superfilmcollection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.phamtantb24.superfilmcollection.databinding.ActivityAddFilmBinding;
import com.phamtantb24.superfilmcollection.model.Film;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddFilmActivity extends AppCompatActivity {
    private ActivityAddFilmBinding binding;
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Films");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFilmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.progressBar.setVisibility(View.INVISIBLE);

        binding.imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        binding.buttonUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.editTextTitle.getText().toString().trim();
                String releaseDay = binding.editTextReleaseDay.getText().toString().trim();
                String director = binding.editTextDirector.getText().toString().trim();
                String starring = binding.textViewStarring.getText().toString().trim();
                String language = binding.editTextLanguage.getText().toString().trim();
                String country = binding.editTextCountry.getText().toString().trim();
                String runningTime = binding.editTextRunningTime.getText().toString().trim();
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
                    Film film = new Film(title, releaseDay, listActors, director, language, imageUri.toString(), Integer.parseInt(runningTime), country);
                    uploadToFirebase(film, imageUri);
                }
            }
        });

        binding.backHomeFromAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            binding.imageUpload.setImageURI(imageUri);
        }
    }

    private void uploadToFirebase(Film filmUpload, Uri imgUri) {
        final StorageReference fileRef = reference.child(imgUri.getLastPathSegment() + "." + getFileExtension(imgUri));
        fileRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        filmUpload.setBanner(uri.toString());
                        myRef.child(filmUpload.getUuid()).setValue(filmUpload).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Add Film Successfully !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        ).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Add Film Failure !", Toast.LENGTH_SHORT).show();
                            }
                        });
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        binding.imageUpload.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
                        resetText();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                binding.progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    private void resetText() {
        binding.editTextTitle.setText("");
        binding.editTextReleaseDay.setText("");
        binding.editTextDirector.setText("");
        binding.textViewStarring.setText("");
        binding.editTextLanguage.setText("");
        binding.editTextCountry.setText("");
        binding.editTextRunningTime.setText("");
    }

}