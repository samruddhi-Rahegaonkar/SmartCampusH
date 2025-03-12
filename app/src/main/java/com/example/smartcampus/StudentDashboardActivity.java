package com.example.smartcampus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentDashboardActivity extends AppCompatActivity {

    private Button btnLogout;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private LinearLayout btnEvent, btnAssignment, btnAttendance, btnNotice;
    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize UI components
        btnLogout = findViewById(R.id.btnLogout);
        btnEvent = findViewById(R.id.btnEvent);
        btnAssignment = findViewById(R.id.btnAssignment);
        btnAttendance = findViewById(R.id.btnAttendance);
        btnNotice = findViewById(R.id.btnNotice);
        welcomeTextView = findViewById(R.id.welcomeTextView);

        // Set welcome message dynamically (replace with user's name if available)
        String userName = "Student"; // You can fetch actual name from Firebase if needed
        welcomeTextView.setText("Welcome, " + userName);

        // Logout button click event
        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Event button click event
        btnEvent.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, EventActivity.class);
            startActivity(intent);
        });

        // Assignment button click event
        btnAssignment.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, ViewAssignmentsActivity.class);
            startActivity(intent);
        });

        // Attendance button click event
        btnAttendance.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, ViewAttendanceActivity.class);
            startActivity(intent);
        });

        // Notice button click event
        btnNotice.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, NoticeFetchActivity.class);
            startActivity(intent);
        });
    }
}
