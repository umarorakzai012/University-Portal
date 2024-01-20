package com.Student;

import com.Excel.Course;
import com.Excel.Info;
import com.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CourseRegistrationStudent extends Frame{
    final private static int[][] COURSE_CREDIT_HOURS = {
            {3, 1, 1, 2, 1, 3, 3, 3},
            {3, 1, 3, 1, 3, 3, 2, 1},
            {3, 1, 3, 1, 3, 3, 3},
            {3, 1, 3, 3, 1, 3, 1},
            {3, 3, 1, 3, 1, 3, 3},
            {3, 3, 1, 3, 3, 3},
            {3, 3, 1, 3, 3, 3},
            {3, 3, 3, 3, 3}
    };
    JCheckBox[] checkBoxes;
    JButton[] button = new JButton[2];
    String id;
    String[][] details;
    CourseRegistrationStudent(String id){
        this.id = id;
        String semester = Info.getSingleInfo(id, "Semester");
        int sem = Integer.parseInt(semester) - 1;
        details = Course.getSingleStudentCourseStatus(id);
        checkBoxes = new JCheckBox[details.length];
        String[] eachRowText = new String[details.length + 1];
        eachRowText[0] = String.format("%-15s %-15s %-10s", "Course Name", "Credit Hours", "Status");
        for(int i = 1; i < details.length + 1; i++){
            eachRowText[i] = String.format("%-15s %-15s %-10s", details[i - 1][0], COURSE_CREDIT_HOURS[sem][i - 1], details[i - 1][1]);
        }
        for(int i = 0; i < eachRowText.length; i++){
            JLabel label = getLabel(eachRowText[i], 15, i * 25, 400, 35);
            this.add(label);
        }
        for(int j = 0; j < checkBoxes.length; j++){
            checkBoxes[j] = getCheckBox(350, (j + 1) * 25, 100, 35);
            if(details[j][1].equals("E") || details[j][1].equals("P")){
                continue;
            }
            if(j == 0){
                this.add(getLabel("Register?", 350, j * 25, 100, 35));
            }
            this.add(checkBoxes[j]);
        }
        this.add(button[0] = getButton("Register", 250, (checkBoxes.length + 3) * 25, 175, 35));
        button[0].addActionListener(this);
        this.add(button[1] = getButton("Back", 50, (checkBoxes.length + 3) * 25, 175, 35));
        button[1].addActionListener(this);
        defaultJFrame(500, 500, "Course Status");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button[0]){
            this.dispose();
            for(int i = 0; i < checkBoxes.length; i++){
                if(checkBoxes[i].isSelected()){
                    Course.Course_Regis(id, details[i][0]);
                }
            }
            new CourseRegistrationStudent(id);
        }
        if(e.getSource() == button[1]){
            this.dispose();
            new InfoStudent(id);
        }
    }
}
