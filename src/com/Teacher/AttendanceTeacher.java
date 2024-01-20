package com.Teacher;

import com.Excel.Attendance;
import com.Excel.Course;
import com.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AttendanceTeacher extends Frame {
    String teacherID, courseName;
    JComboBox[] comboBoxes;
    JButton[] buttons = new JButton[2];
    JCheckBox checkBox;
    String[] studentIds;
    AttendanceTeacher(String teacherID, String courseName){
        this.teacherID = teacherID;
        this.courseName = courseName;
        studentIds = Course.getAllIDsRegisteredForCourse(courseName);
        comboBoxes = new JComboBox[studentIds.length];
        String[] list = {"-", "P", "A"};

        int gap = 0;
        this.add(getLabel("Student id", 60, (gap * 25) + 50, 100, 30));
        this.add(getLabel("Attendance", 200, (gap * 25) + 50, 100, 30));
        this.add(getLabel("Set 'A' as Default", 300, (gap * 25) + 15, 150, 20));
        this.add(checkBox = getCheckBox(350, (gap * 25) + 50, 50, 20));
        gap++;
        for(int i = 0; i < studentIds.length; i++, gap++){
            this.add(getLabel(studentIds[i], 60, (gap * 25) + 50, 50, 30));
            this.add(comboBoxes[i] = getComboBox(list, 200, (gap * 25) + 50, 50, 30));
            comboBoxes[i].addActionListener(this);
        }

        this.add(buttons[0] = getButton("Save", 60, (gap * 25) + 100, 100, 40));
        buttons[0].addActionListener(this);

        this.add(buttons[1] = getButton("Back", 200, (gap * 25) + 100, 100, 40));
        buttons[1].addActionListener(this);

        defaultJFrame(500, 500, "Updating Attendance");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttons[0]){
            this.dispose();
            String date = JOptionPane.showInputDialog(null, "Date");

            Attendance.addDate(courseName, date);
            String defaultAttendance;
            if(checkBox.isSelected())
                defaultAttendance = "A";
            else
                defaultAttendance = "P";
            for(int i = 0; i < studentIds.length; i++){
                String selectedAttendance = comboBoxes[i].getItemAt(comboBoxes[i].getSelectedIndex()).toString();
                if(selectedAttendance.equals("-"))
                    selectedAttendance = defaultAttendance;
                Attendance.addAttendance(studentIds[i], courseName, date, selectedAttendance);
            }
            new TeacherStarting(teacherID);
        }
        else if(e.getSource() == buttons[1]){
            this.dispose();
            new TeacherStarting(teacherID);
        }
    }
}
