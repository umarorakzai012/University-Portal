package com.Teacher;

import com.Excel.Course;
import com.Excel.Marks;
import com.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MarksTeacher extends Frame {
    String teacherId, courseName;
    final private static String[] BUTTON_NAME = {
            "Assignment 1",
            "Assignment 2",
            "Assignment 3",
            "Quiz 1",
            "Quiz 2",
            "Quiz 3",
            "Sessional 1",
            "Sessional 2",
            "Final",
            "Back"
    };

    JButton[] button = new JButton[BUTTON_NAME.length];
    String[] studentIds;
    MarksTeacher(String id, String courseName){
        this.teacherId = id;
        this.courseName = courseName;

        studentIds = Course.getAllIDsRegisteredForCourse(courseName);

        for(int i = 0; i < BUTTON_NAME.length; i++){
            this.add(button[i] = getButton(BUTTON_NAME[i], 200, (i * 40) + 30, 120, 30));
            button[i].addActionListener(this);
        }

        defaultJFrame(500, 500, "Marks Portal");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() != button[BUTTON_NAME.length - 1]){
            int result = JOptionPane.showConfirmDialog(null,"For All Students?");
            for(int i = 0; i < button.length - 1; i++){
                if(e.getSource() == button[i]){
                    if(result == JOptionPane.YES_OPTION){
                        this.dispose();
                        new AddMarksForAllStudents(teacherId, courseName, BUTTON_NAME[i]);
                    } else if (result == JOptionPane.NO_OPTION) {
                        String marks = JOptionPane.showInputDialog(null,"Weightage");
                        if(marks.equals(""))
                            marks = "0";
                        float weightage = Float.parseFloat(marks);
                        String id = JOptionPane.showInputDialog(null,"ID");
                        boolean found = false;
                        for(int j = 0; j < studentIds.length; j++){
                            if(studentIds[j].equals(id)) {
                                Marks.addMarks(id, courseName, weightage, BUTTON_NAME[i]);
                                found = true;
                                break;
                            }
                        }
                        if(!found)
                            JOptionPane.showMessageDialog(null, "No such Id found", "", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }
        else{
            this.dispose();
            new TeacherStarting(teacherId);
        }
    }
}
