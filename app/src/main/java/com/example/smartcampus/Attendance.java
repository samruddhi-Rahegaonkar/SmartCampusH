package com.example.smartcampus;

public class Attendance {
    private String studentName;
    private String attendanceStatus;

    // Required empty constructor for Firebase or serialization
    public Attendance() {
    }

    public Attendance(String studentName, String attendanceStatus) {
        this.studentName = studentName;
        this.attendanceStatus = attendanceStatus;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
}
