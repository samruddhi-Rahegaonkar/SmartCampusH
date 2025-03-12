package com.example.smartcampus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class TeacherDashboard extends AppCompatActivity {

    private LinearLayout btnMarkAttendance, btnViewAttendance, btnAddStudent, btnCreateEvent,btncreateAssignment,btncreateNotices;
    private Button btnLogout;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        // Linking XML elements to Java code
        btnMarkAttendance = findViewById(R.id.btnMarkAttendance);
        btnViewAttendance = findViewById(R.id.btnViewAttendance);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnCreateEvent = findViewById(R.id.btnCreateEvent);
        btncreateAssignment=findViewById(R.id.btncreateAssignment);
        btnLogout=findViewById(R.id.btnLogout);
        btncreateNotices=findViewById(R.id.btncreateNotices);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Set click listeners for each button
        btnMarkAttendance.setOnClickListener(v ->
                startActivity(new Intent(TeacherDashboard.this, MarkAttendanceActivity.class))
        );

        btnViewAttendance.setOnClickListener(v ->
                startActivity(new Intent(TeacherDashboard.this, ViewAttendanceActivity.class))
        );

        btnAddStudent.setOnClickListener(v ->
                startActivity(new Intent(TeacherDashboard.this, AddStudentActivity.class))
        );

        btnCreateEvent.setOnClickListener(v ->
                startActivity(new Intent(TeacherDashboard.this, CreateEventActivity.class))
        );
        btncreateAssignment.setOnClickListener(v -> {
            startActivity(new Intent(TeacherDashboard.this, AssignmentActivity.class));
        });
        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
        btncreateNotices.setOnClickListener(v-> {
            startActivity(new Intent(TeacherDashboard.this, NoticeActivity.class));
        });
    }
}
