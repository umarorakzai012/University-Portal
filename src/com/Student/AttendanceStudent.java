package com.Student;

import com.Excel.Attendance;
import com.Excel.Course;
import com.Excel.Info;
import com.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AttendanceStudent extends Frame {
    String id, semesterCurrentlyShowing;
    JComboBox comboBox;
    JButton[] button = new JButton[2];

    AttendanceStudent(String id){
        this.id = id;
        String semester = Info.getSingleInfo(id, "Semester");
        semesterCurrentlyShowing = semester;
        int sem = Integer.parseInt(semester);
        String[][] details = Course.getSingleStudentCourseStatus(id);
        int numOfNotNotEnrolledCourses = 0;
        for(int i = 0; i < details.length; i++){
            if(!details[i][1].equals("NE"))
                numOfNotNotEnrolledCourses++;
        }
        int[] percentage = new int[numOfNotNotEnrolledCourses];
        int index = 0;
        for(int i = 0; i < details.length; i++){
            if(details[i][1].equals("NE"))
                continue;
            if(details[i][1].equals("P")){
                percentage[index] = Attendance.getPassedCourseAttendanceInfo(id, details[i][0]);
                index++;
                continue;
            }
            String[][] attendanceInfo = Attendance.getAttendanceInfo(id, details[i][0]);
            for(int j = 0; j < attendanceInfo.length; j++){
                if(attendanceInfo[j][1].equals("P"))
                    percentage[index] += 1;
            }
            if(attendanceInfo.length != 0) {
                percentage[index] = (percentage[index] * 100) / attendanceInfo.length;
                index++;
            }
        }
        String[] lists = new String[sem];
        for(int i = 0; i < sem; i++){
            lists[i] = "Semester " + (sem - i);
        }
        this.add(comboBox = getComboBox(lists, 100, 25, 175, 25));
        comboBox.addActionListener(this);

        this.add(button[0] = getButton("Change", 300, 25, 100, 25));
        button[0].addActionListener(this);

        index = 0;
        for(int i = 0; i < details.length; i++){
            if(details[i][1].equals("NE"))
                continue;
            this.add(getLabel(details[i][0], 75, (index * 25) + 100, 100, 30));
            this.add(getLabel(percentage[index] + "", 225, (index * 25) + 100, 50, 30));
            index++;
        }
        this.add(button[1] = getButton("Back", 175, (index * 25) + 150, 150, 25));
        button[1].addActionListener(this);

        defaultJFrame(500, 500, "Attendance");
    }

    AttendanceStudent(String id, String semester){
        this.id = id;
        int dataOfSemester = Integer.parseInt(semester);
        int currentStudentsSem = Integer.parseInt(Info.getSingleInfo(id, "Semester"));
        semesterCurrentlyShowing = semester;
        String details[][] = Attendance.getPassedSemesterAttendanceInfo(id, semester);
        String[] lists = new String[currentStudentsSem];
        lists[0] = "Semester " + dataOfSemester;
        for(int i = 0; i < currentStudentsSem - 1; i++){
            if(currentStudentsSem - i == dataOfSemester)
                continue;
            lists[i + 1] = "Semester " + (currentStudentsSem - i);
        }
        this.add(comboBox = getComboBox(lists, 100, 25, 175, 25));
        this.add(button[0] = getButton("Change", 300, 25, 100, 25));
        button[0].addActionListener(this);

        comboBox.addActionListener(this);
        int index = 0;
        for(int i = 0; i < details.length; i++){
            this.add(getLabel(details[i][0], 75, (index * 25) + 100, 100, 30));
            if(details[i][1].equals("")){
                this.add(getLabel("0.0", 225, (index * 25) + 100, 50, 30));
                index++;
                continue;
            }
            this.add(getLabel(details[i][1], 225, (index * 25) + 100, 50, 30));
            index++;
        }
        this.add(button[1] = getButton("Back", 175, (index * 25) + 150, 150, 25));
        button[1].addActionListener(this);

        defaultJFrame(500, 500, "Attendance");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button[0]){
            String selectedSemester = comboBox.getItemAt(comboBox.getSelectedIndex()).toString().split(" ")[1];
            if(selectedSemester.equals(semesterCurrentlyShowing))
                return;
            else if(selectedSemester.equals(Info.getSingleInfo(id, "Semester"))){
                this.dispose();
                new AttendanceStudent(id);
            }
            else{
                this.dispose();
                new AttendanceStudent(id, selectedSemester);
            }
        }else if(e.getSource() == button[1]){
            this.dispose();
            new InfoStudent(id);
        }
    }
}
