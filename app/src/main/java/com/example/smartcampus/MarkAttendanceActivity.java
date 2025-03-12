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
import java.text.SimpleDateFormat;
import java.util.*;

public class MarkAttendanceActivity extends AppCompatActivity {

    private Spinner spinnerYear;
    private Button btnPickDate, btnSubmitAttendance;
    private TextView tvSelectedDate;
    private RecyclerView recyclerViewStudents;
    private FirebaseFirestore db;
    private StudentAdapter studentAdapter;
    private String selectedDate;

    private final Map<String, String> yearMap = new HashMap<String, String>() {{
        put("1st year", "1st year");
        put("2nd year", "2nd year");
        put("3rd year", "3rd year");
        put("Final year", "Final year");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        spinnerYear = findViewById(R.id.spinnerYear);
        btnPickDate = findViewById(R.id.btnPickDate);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnSubmitAttendance = findViewById(R.id.btnSubmitAttendance);
        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        db = FirebaseFirestore.getInstance();

        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentAdapter(new ArrayList<>());
        recyclerViewStudents.setAdapter(studentAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                Arrays.asList("1st year", "2nd year", "3rd year", "Final year"));
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = spinnerYear.getSelectedItem().toString();
                loadStudents(yearMap.get(selectedYear));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnPickDate.setOnClickListener(v -> showDatePicker());
        btnSubmitAttendance.setOnClickListener(v -> submitAttendance());
    }

    private void loadStudents(String year) {
        if (year == null) {
            Toast.makeText(this, "Invalid year selected!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("students").document(year).collection("studentList")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots == null || queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "No students found for " + year, Toast.LENGTH_SHORT).show();
                        studentAdapter.setStudents(new ArrayList<>());
                        return;
                    }

                    List<Student> students = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Log.d("Firestore", "Student document: " + doc.getData());
                        String studentName = doc.getString("name");
                        if (studentName != null) {
                            String studentId = doc.getId();
                            students.add(new Student(studentId, studentName, false));
                        }
                    }

                    studentAdapter.setStudents(students);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load students: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error loading students", e);
                });
    }

    private void submitAttendance() {
        String selectedYear = spinnerYear.getSelectedItem().toString();
        String year = yearMap.get(selectedYear);

        if (selectedDate == null) {
            Toast.makeText(this, "Please pick a date!", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference attendanceRef = db.collection("attendance").document(year)
                .collection("dates").document(selectedDate).collection("studentList");

        for (Student student : studentAdapter.getStudents()) {
            Map<String, Object> attendance = new HashMap<>();
            attendance.put("status", student.isPresent() ? "present" : "absent");

            attendanceRef.document(student.getId()).set(attendance)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Attendance marked for " + student.getName()))
                    .addOnFailureListener(e -> Log.e("Firestore", "Failed to mark attendance", e));
        }

        Toast.makeText(this, "Attendance submitted for " + selectedYear + " on " + selectedDate, Toast.LENGTH_SHORT).show();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            // Ensure single-digit months and days don't have leading zeros
            String formattedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            selectedDate = formattedDate;
            tvSelectedDate.setText(formattedDate);
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}
