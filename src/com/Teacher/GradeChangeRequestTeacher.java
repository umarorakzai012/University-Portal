package com.Teacher;

import com.Excel.Course;
import com.Excel.GradeChangeRequest;
import com.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class GradeChangeRequestTeacher extends Frame {
    String teacherID, courseName;
    ArrayList<String> gradeChangeRequest = new ArrayList<>(), stuIds = new ArrayList<>();
    JButton[] buttons;
    GradeChangeRequestTeacher(String teacherId, String courseName){
        this.teacherID = teacherId;
        this.courseName = courseName;

        String[] studentIds = Course.getAllIDsRegisteredForCourse(courseName);

        for(int i = 0; i < studentIds.length; i++){
            String request = GradeChangeRequest.getRequest(studentIds[i], courseName);
            if(request.equals("-"))
                continue;
            stuIds.add(studentIds[i]);
            gradeChangeRequest.add(request);
        }
        buttons = new JButton[gradeChangeRequest.size() + 1];

        for (int i = 0; i < gradeChangeRequest.size(); i++) {
            this.add(buttons[i] = getButton(stuIds.get(i), 200, (i * 30) + 50, 100, 25));
            buttons[i].addActionListener(this);
        }

        this.add(buttons[buttons.length - 1] = getButton("Back", 200, (gradeChangeRequest.size() * 30) + 75, 100, 25));
        buttons[buttons.length - 1].addActionListener(this);

        defaultJFrame(500, 500, "Grade Change Request");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() != buttons[buttons.length - 1]){
            for(int i = 0; i < gradeChangeRequest.size(); i++){
                if(e.getSource() == buttons[i]){
                    JOptionPane.showMessageDialog(null, gradeChangeRequest.get(i), "", JOptionPane.INFORMATION_MESSAGE);
                    GradeChangeRequest.setRequest(stuIds.get(i), "-", courseName);
                    this.dispose();
                    new GradeChangeRequestTeacher(teacherID, courseName);
                }
            }
        }
        else{
            this.dispose();
            new TeacherStarting(teacherID);
        }
    }
}
