package com.example.smartcampus;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.*;
import java.util.*;

public class ViewAttendanceActivity extends AppCompatActivity {

    private Spinner spinnerYear;
    private Button btnPickDate;
    private TextView tvSelectedDate;
    private RecyclerView recyclerViewAttendance;
    private FirebaseFirestore db;
    private AttendanceAdapter attendanceAdapter;
    private String selectedDate;

    private final Map<String, String> yearMap = new HashMap<String, String>() {{
        put("1st year", "1st year");
        put("2nd year", "2nd year");
        put("3rd year", "3rd year");
        put("Final year", "final year");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        // Initialize UI elements
        spinnerYear = findViewById(R.id.spinnerYear);
        btnPickDate = findViewById(R.id.btnPickDate);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        recyclerViewAttendance = findViewById(R.id.recyclerViewAttendance);
        db = FirebaseFirestore.getInstance();

        // Setup RecyclerView
        recyclerViewAttendance.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAttendance.setHasFixedSize(true);
        attendanceAdapter = new AttendanceAdapter(new ArrayList<>());
        recyclerViewAttendance.setAdapter(attendanceAdapter);

        // Setup Spinner (Dropdown)
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                Arrays.asList("1st year", "2nd year", "3rd year", "Final year"));
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);

        // Date picker button
        btnPickDate.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            // Ensure date formatting (YYYY-MM-DD)
            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            tvSelectedDate.setText(selectedDate);
            loadAttendance();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void loadAttendance() {
        String selectedYear = spinnerYear.getSelectedItem().toString();
        String year = yearMap.get(selectedYear);

        if (selectedDate == null || year == null) {
            Toast.makeText(this, "Please select year and date!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("attendance").document(year).collection("dates")
                .document(selectedDate).collection("studentList")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "No attendance records found!", Toast.LENGTH_SHORT).show();
                        attendanceAdapter.setAttendanceList(new ArrayList<>());
                        return;
                    }

                    List<Attendance> attendanceList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String studentName = doc.getId();
                        String status = doc.getString("status");
                        attendanceList.add(new Attendance(studentName, status != null ? status : "N/A"));
                    }
                    attendanceAdapter.setAttendanceList(attendanceList);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load attendance!", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error loading attendance: " + e.getMessage(), e);
                });
    }
}
