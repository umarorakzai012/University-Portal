package com.Student;

import com.Excel.Course;
import com.Excel.Info;
import com.Excel.Marks;
import com.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MarksStudent extends Frame {
    String id, semesterCurrentlyShowing;
    JComboBox comboBox;
    JButton[] button = new JButton[2];
    final private static String columnNameFormatted = String.format(
            "%-12s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s",
            "Course Name",
            "A1",
            "A2",
            "A3",
            "Q1",
            "Q2",
            "Q3",
            "S1",
            "S2",
            "F"
    );
    MarksStudent(String id){

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
        String[] marksFormatted = new String[numOfNotNotEnrolledCourses + 1];
        marksFormatted[0] = columnNameFormatted;
        int index = 0;
        for(int i = 0; i < details.length; i++){
            if(details[i][1].equals("NE"))
                continue;
            String[] marksInfo = Marks.getMarksInfo(id, details[i][0]);
            marksFormatted[index + 1] = String.format("%-12s ", details[i][0]);
            for(int j = 0; j < marksInfo.length; j++){
                marksFormatted[index + 1] += String.format("%-5s ", marksInfo[j]);
            }
            index++;
        }
        String[] lists = new String[sem];
        for(int i = 0; i < sem; i++){
            lists[i] = "Semester " + (sem - i);
        }
        this.add(comboBox = getComboBox(lists, 150, 25, 175, 25));
        comboBox.addActionListener(this);

        this.add(button[0] = getButton("Change", 350, 25, 100, 25));
        button[0].addActionListener(this);

        index = 0;
        this.add(getLabel(marksFormatted[index], 65, (index * 25) + 100, 600, 30));
        index++;
        for(int i = 0; i < details.length; i++){
            if(details[i][1].equals("NE"))
                continue;
            this.add(getLabel(marksFormatted[index], 65, (index * 25) + 100, 600, 30));
            index++;
        }
        this.add(button[1] = getButton("Back", 225, (index * 25) + 150, 150, 25));
        button[1].addActionListener(this);

        defaultJFrame(650, 500, "Marks");
    }
    MarksStudent(String id, String semester){
        this.id = id;
        int dataOfSemester = Integer.parseInt(semester);
        int currentStudentsSem = Integer.parseInt(Info.getSingleInfo(id, "Semester"));
        semesterCurrentlyShowing = semester;
        String[] lists = new String[currentStudentsSem];
        lists[0] = "Semester " + dataOfSemester;
        for(int i = 0; i < currentStudentsSem - 1; i++){
            if(currentStudentsSem - i == dataOfSemester)
                continue;
            lists[i + 1] = "Semester " + (currentStudentsSem - i);
        }
        this.add(comboBox = getComboBox(lists, 150, 25, 175, 25));
        comboBox.addActionListener(this);

        this.add(button[0] = getButton("Change", 350, 25, 100, 25));
        button[0].addActionListener(this);

        int index = 0;
        String[] marksFormatted = Marks.getPassedSemesterMarks(id, semester);
        this.add(getLabel(columnNameFormatted, 65, (index * 25) + 100, 600, 30));
        index++;
        for(int i = 0; i < marksFormatted.length; i++){
            this.add(getLabel(marksFormatted[i], 65, (index * 25) + 100, 600, 30));
            index++;
        }
        this.add(button[1] = getButton("Back", 225, (index * 25) + 150, 150, 25));
        button[1].addActionListener(this);

        defaultJFrame(650, 500, "Marks");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button[0]){
            String selectedSemester = comboBox.getItemAt(comboBox.getSelectedIndex()).toString().split(" ")[1];
            if(selectedSemester.equals(semesterCurrentlyShowing))
                return;
            else if(selectedSemester.equals(Info.getSingleInfo(id, "Semester"))){
                this.dispose();
                new MarksStudent(id);
            }
            else{
                this.dispose();
                new MarksStudent(id, selectedSemester);

            }
        }else if(e.getSource() == button[1]){
            this.dispose();
            new InfoStudent(id);
        }
    }
}
