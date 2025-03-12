package com.example.smartcampus;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SubmitAssignmentActivity extends AppCompatActivity {

    private EditText linkEditText;
    private Button submitButton;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_assignment);

        // Initialize views
        linkEditText = findViewById(R.id.linkEditText);
        submitButton = findViewById(R.id.submitButton);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Submitting assignment...");
        progressDialog.setCancelable(false);

        // Submit link
        submitButton.setOnClickListener(v -> {
            String link = linkEditText.getText().toString().trim();
            if (isValidLink(link)) {
                submitLinkToFirestore(link);
            } else {
                Toast.makeText(this, "Please enter a valid link!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Validate link (basic check for HTTP/HTTPS URLs)
    private boolean isValidLink(String link) {
        return !TextUtils.isEmpty(link) && (link.startsWith("http://") || link.startsWith("https://"));
    }

    // Submit link to Firestore
    private void submitLinkToFirestore(String link) {
        progressDialog.show();

        Map<String, Object> submissionData = new HashMap<>();
        submissionData.put("link", link);
        submissionData.put("timestamp", System.currentTimeMillis());

        db.collection("submittedAssignments")
                .add(submissionData)
                .addOnSuccessListener(documentReference -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Assignment link submitted successfully!", Toast.LENGTH_SHORT).show();
                    linkEditText.setText(""); // Clear input field
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to submit assignment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
