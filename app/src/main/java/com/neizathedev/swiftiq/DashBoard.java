package com.neizathedev.swiftiq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class DashBoard extends AppCompatActivity {

    private TextView usernameTextView, emailTextView, FirstNameTextView, lastNameTextView, omangTextView, genderTextView, DOBTextView, phoneNumberTextView, documentIDTextView, countryTextView, physicalAddressTextView, postalAddressTextView;
    private Button logOutBtn;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore mFirestore;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        // Initialize views
        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        FirstNameTextView = findViewById(R.id.FirstNameTextView);
        lastNameTextView = findViewById(R.id.lastNameTextView);
        omangTextView = findViewById(R.id.omangTextView);
        genderTextView = findViewById(R.id.genderTextView);
        DOBTextView = findViewById(R.id.DOBTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        documentIDTextView = findViewById(R.id.documentIDTextView);
        countryTextView = findViewById(R.id.countryTextView);
        physicalAddressTextView = findViewById(R.id.physicalAddressTextView);
        postalAddressTextView = findViewById(R.id.postalAddressTextView);
        logOutBtn = findViewById(R.id.logOutBtn);

        //Firebase Instance
        firebaseAuth = FirebaseAuth.getInstance();
        email = firebaseAuth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();

        //Check If User is logged In
        if (email == null) {
            Intent intent = new Intent(DashBoard.this, LoginScreenActivity.class);
            startActivity(intent);
            finish(); // Finish the current activity to prevent going back to the dashboard if the user is not logged in
            return; // Exit the onCreate method
        }

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DashBoard.this, LoginScreenActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity to prevent going back to the dashboard after logging out
            }
        });

        //Fetch user data from Firestore
        DocumentReference userRef = mFirestore.collection("user").document(email);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Retrieve user data from the document
                    String username = documentSnapshot.getString("username");
                    String email = documentSnapshot.getString("email");
                    String firstName = documentSnapshot.getString("firstname");
                    String lastName = documentSnapshot.getString("lastname");
                    String omang = documentSnapshot.getString("omang");
                    String gender = documentSnapshot.getString("gender");
                    String dob = documentSnapshot.getString("dob");
                    String phoneNumber = documentSnapshot.getString("phone_number");
                    String docID = documentSnapshot.getString("documentId");
                    String country = documentSnapshot.getString("country");
                    String physicalAddress = documentSnapshot.getString("physical_address");
                    String postalAddress = documentSnapshot.getString("postal_address");

                    // Update TextViews with the retrieved data
                    usernameTextView.setText(username);
                    emailTextView.setText(email);
                    FirstNameTextView.setText(firstName);
                    lastNameTextView.setText(lastName);
                    omangTextView.setText(omang);
                    genderTextView.setText(gender);
                    DOBTextView.setText(dob);
                    phoneNumberTextView.setText(phoneNumber);
                    documentIDTextView.setText(docID);
                    countryTextView.setText(country);
                    physicalAddressTextView.setText(physicalAddress);
                    postalAddressTextView.setText(postalAddress);
                } else {
                    // User document doesn't exist
                    Toast.makeText(DashBoard.this, "User document not found.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error occurred while fetching user data
                Toast.makeText(DashBoard.this, "Failed to fetch user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
