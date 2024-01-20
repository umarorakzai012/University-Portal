package com.Teacher;

import com.Excel.Course;
import com.Excel.Marks;
import com.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AddMarksForAllStudents extends Frame {
    String courseName, marksFor, id;
    JTextField[] textFields;
    JButton[] buttons = new JButton[2];
    String[] studentIds;
    AddMarksForAllStudents(String id, String courseName, String marksFor){
        this.id = id;
        this.courseName = courseName;
        this.marksFor = marksFor;
        studentIds = Course.getAllIDsRegisteredForCourse(courseName);
        textFields = new JTextField[studentIds.length];

        int gap = 0;
        this.add(getLabel("Student id", 120, (gap * 25) + 50, 100, 30));
        this.add(getLabel("Weightage", 300, (gap * 25) + 50, 100, 30));
        gap++;
        for(int i = 0; i < studentIds.length; i++, gap++){
            this.add(getLabel(studentIds[i], 120, (gap * 25) + 50, 50, 30));
            this.add(textFields[i] = getTextField(300, (gap * 25) + 50, 50, 30));
        }

        this.add(buttons[0] = getButton("Save", 120, (gap * 25) + 100, 100, 40));
        buttons[0].addActionListener(this);

        this.add(buttons[1] = getButton("Back", 300, (gap * 25) + 100, 100, 40));
        buttons[1].addActionListener(this);

        defaultJFrame(500, 500, "Updating Marks");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttons[0]){
            this.dispose();
            for(int i = 0; i < textFields.length; i++){
                String weightage = textFields[i].getText();
                if(weightage.equals(""))
                    Marks.addMarks(studentIds[i], courseName, 0f, marksFor);
                else{
                    try{
                        Marks.addMarks(studentIds[i], courseName, Float.parseFloat(weightage), marksFor);
                    } catch (NumberFormatException nfe){
                        Marks.addMarks(studentIds[i], courseName, 0f, marksFor);
                    }
                }
            }
            new MarksTeacher(id, courseName);
        }
        else{
            this.dispose();
            new MarksTeacher(id, courseName);
        }
    }
}
