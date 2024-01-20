package com.Student;

import com.Excel.Course;
import com.Excel.GradeChangeRequest;
import com.Excel.Info;
import com.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class GradeChangeRequestStudent extends Frame {
    JButton[] buttons;
    ArrayList<String> enrolledCourses = new ArrayList<String>();
    String id;
    GradeChangeRequestStudent(String id){
        this.id = id;
        String semester = Info.getSingleInfo(id, "Semester");
        int sem = Integer.parseInt(semester);
        String[][] details = Course.getSingleStudentCourseStatus(id);
        for(int i = 0; i < details.length; i++){
            if(details[i][1].equals("E"))
                enrolledCourses.add(details[i][0]);
        }
        buttons = new JButton[enrolledCourses.size() + 1];
        int index = 0;
        for(int i = 0; i < enrolledCourses.size(); i++){
            this.add(buttons[index] = getButton(enrolledCourses.get(i), 175, 25 + (index * 35), 100, 25));
            buttons[index].addActionListener(this);
            index++;
        }
        this.add(buttons[index] = getButton("Back", 175, 35 + (index * 40), 100, 30));
        buttons[index].addActionListener(this);

        defaultJFrame(500, 500, "Grade Change Request");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        for(int i = 0; i < buttons.length - 1; i++) {
            if (e.getSource() == buttons[i]) {
                String courseName = enrolledCourses.get(i);
                String request = JOptionPane.showInputDialog(this, "Enter Request");
                if(request.equals(""))
                    request = "-";
                GradeChangeRequest.setRequest(id, request, courseName);
            }
        }
        if(e.getSource() == buttons[buttons.length - 1]) {
            this.dispose();
            new InfoStudent(id);
        }
    }
}
