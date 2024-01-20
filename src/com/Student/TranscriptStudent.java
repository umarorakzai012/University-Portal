package com.Student;

import com.Excel.Course;
import com.Excel.Info;
import com.Excel.Marks;
import com.Excel.Transcript;
import com.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class TranscriptStudent extends Frame {
    String id, semesterCurrentlyShowing;
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
    final private static String[][] COURSE_NAME = {
            {"PF", "PF Lab", "ICT", "ECC", "ECC Lab", "LA", "Cal", "PST"},
            {"OOP", "OOP Lab", "DLD", "DLD Lab", "DE", "ISL", "CPS", "CPS Lab"},
            {"PA", "PA Lab", "DS", "DS Lab", "Discrete", "PS", "UE-1"},
            {"AI", "AI Lab", "FSE", "Database", "Database Lab", "COAL", "COAL Lab"},
            {"KRR", "ML", "ML Lab", "OS", "OS Lab", "DAA", "TBW"},
            {"ANN", "CN", "CN Lab", "PDC", "AIE-1", "AIE-2"},
            {"FNLP", "CV", "CV Lab", "FYP-1", "AIE-3", "UE-2"},
            {"FYP-2", "IS", "PP", "UE-3", "AIE-4"}
    };
    String[] gradeLetter = {"A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "F"};
    double[] gradePointEquivalent = {4.00, 4.00, 3.66, 3.33, 3.00, 2.66, 2.33, 2.00, 1.66, 1.33, 1.00, 0.00};
    JComboBox comboBox;
    JButton[] button = new JButton[2];
    TranscriptStudent(String id){
        this.id = id;
        String semester = Info.getSingleInfo(id, "Semester");
        semesterCurrentlyShowing = semester;
        int sem = Integer.parseInt(semester);
        String[][] details = Course.getSingleStudentCourseStatus(id);
        ArrayList<String> completeFormattedString = new ArrayList<String>();
        completeFormattedString.add(String.format("%-15s %-10s %-10s %-10s", "Course Name", "CrdHrs", "Grade", "Points"));
        for(int i = 0; i < details.length; i++){
            if(details[i][1].equals("NE"))
                continue;
            else {
                String courseName = details[i][0];
                String crdHrs = COURSE_CREDIT_HOURS[sem - 1][i] + "";
                String grade = "I";
                String points = "0";
                String formatted = String.format("%-15s %-10s %-10s %-10s", courseName, crdHrs, grade, points);
                completeFormattedString.add(formatted);
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

        this.add(getLabel("SGPA: ", 65, 90, 50, 40));
        this.add(getLabel(0 + "", 120, 90, 40, 40));
        this.add(getLabel("CGPA: ", 225, 90, 50, 40));
        this.add(getLabel(0 + "", 280, 90, 40, 40));
        for(int i = 0; i < completeFormattedString.size(); i++){
            this.add(getLabel(completeFormattedString.get(i), 30, 125 + (25 * i), 400, 35));
        }
        this.add(button[1] = getButton("Back", 175, (completeFormattedString.size() * 25) + 175, 150, 25));
        button[1].addActionListener(this);

        defaultJFrame(500, 500, "Transcript");
    }
    TranscriptStudent(String id, String semester){
        this.id = id;
        int dataOfSemester = Integer.parseInt(semester);
        int currentStudentsSem = Integer.parseInt(Info.getSingleInfo(id, "Semester"));
        semesterCurrentlyShowing = semester;
        float[] GPA = Transcript.getGPA(id, semester);
        ArrayList<String> completeFormattedString = new ArrayList<String>();

        completeFormattedString.add(String.format("%-15s %-10s %-10s %-10s", "Course Name", "CrdHrs", "Grade", "Points"));
        for(int i = 0; i < COURSE_NAME[dataOfSemester - 1].length; i++){
            float grandTotal = Marks.getPassedCourseMarks(id, COURSE_NAME[dataOfSemester - 1][i], semester);
            int index = gradeAndPointIndex(grandTotal);
            completeFormattedString.add(
                    String.format(
                            "%-15s %-10s %-10s %-10s",
                            COURSE_NAME[dataOfSemester - 1][i],
                            COURSE_CREDIT_HOURS[dataOfSemester - 1][i],
                            gradeLetter[index],
                            gradePointEquivalent[index]
                    )
            );
        }

        String[] lists = new String[currentStudentsSem];
        lists[0] = "Semester " + dataOfSemester;
        for(int i = 0; i < currentStudentsSem - 1; i++){
            if(currentStudentsSem - i == dataOfSemester)
                continue;
            lists[i + 1] = "Semester " + (currentStudentsSem - i);
        }
        this.add(comboBox = getComboBox(lists, 100, 25, 175, 25));
        comboBox.addActionListener(this);

        this.add(button[0] = getButton("Change", 300, 25, 100, 25));
        button[0].addActionListener(this);

        this.add(getLabel("SGPA: ", 65, 90, 50, 40));
        this.add(getLabel(GPA[0] + "", 120, 90, 40, 40));
        this.add(getLabel("CGPA: ", 225, 90, 50, 40));
        this.add(getLabel(GPA[1] + "", 280, 90, 40, 40));
        for(int i = 0; i < completeFormattedString.size(); i++){
            this.add(getLabel(completeFormattedString.get(i), 30, 125 + (25 * i), 400, 35));
        }
        this.add(button[1] = getButton("Back", 175, (completeFormattedString.size() * 25) + 175, 150, 25));
        button[1].addActionListener(this);

        defaultJFrame(500, 500, "Transcript");
    }
    private int gradeAndPointIndex(float marks){
        for(int i = 0; i < 10; i++){
            if(marks < (90 - 4 * i) && marks >= (86 - 4 * i))
                return (i + 1);
        }
        if(marks >= 90)
            return 0;
        else
            return 11;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button[0]){
            String selectedSemester = comboBox.getItemAt(comboBox.getSelectedIndex()).toString().split(" ")[1];
            if(selectedSemester.equals(semesterCurrentlyShowing))
                return;
            else if(selectedSemester.equals(Info.getSingleInfo(id, "Semester"))){
                this.dispose();
                new TranscriptStudent(id);
            }
            else{
                this.dispose();
                new TranscriptStudent(id, selectedSemester);
            }
        }else if(e.getSource() == button[1]){
            this.dispose();
            new InfoStudent(id);
        }
    }
}
