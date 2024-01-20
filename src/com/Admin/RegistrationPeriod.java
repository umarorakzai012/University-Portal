package com.Admin;

import com.Excel.Attendance;
import com.Excel.Course;
import com.Excel.Info;
import com.Excel.Marks;
import com.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class RegistrationPeriod extends Frame {
    JButton[] button = new JButton[2];
    final private static String[] courseName = {
            "PF", "PF Lab", "ICT", "ECC", "ECC Lab", "LA", "Cal", "PST",
            "OOP", "OOP Lab", "DLD", "DLD Lab", "DE", "ISL", "CPS", "CPS Lab",
            "PA", "PA Lab", "DS", "DS Lab", "Discrete", "PS", "UE-1",
            "AI", "AI Lab", "FSE", "Database", "Database Lab", "COAL", "COAL Lab",
            "KRR", "ML", "ML Lab", "OS", "OS Lab", "DAA", "TBW",
            "ANN", "CN", "CN Lab", "PDC", "AIE-1", "AIE-2",
            "FNLP", "CV", "CV Lab", "FYP-1", "AIE-3", "UE-2",
            "FYP-2", "IS", "PP", "UE-3", "AIE-4",
    };
    RegistrationPeriod(){
        this.add(button[0] = getButton("Registration Period", 150, 50, 150, 35));
        button[0].addActionListener(this);

        this.add(button[1] = getButton("Back", 150, 150, 150, 35));
        button[1].addActionListener(this);

        defaultJFrame(500, 500, "Registration Period");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button[0]){
            this.dispose();
            boolean period = getRegistrationPeriod();
            if (period) {
                int result = JOptionPane.showConfirmDialog(null, "Turn Registration Period Off?", "", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    setRegistrationPeriod(false);
                }
            } else {
                int result = JOptionPane.showConfirmDialog(null, "Turn Registration Period On?", "", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    setRegistrationPeriod(true);
                    increaseSemesterByOne();
                }
            }
            new AdminStarting();
        }
        else {
            this.dispose();
            new AdminStarting();
        }
    }
    void increaseSemesterByOne(){
        for(int i = 0; i < courseName.length; i++){
            String[] ids = Course.getAllIDsRegisteredForCourse(courseName[i]);
            for (int j = 0; j < ids.length; j++) {
                String[] marks = Marks.getMarksInfo(ids[j], courseName[i]);
                float weightage = 0f;
                for (int k = 0; k < marks.length; k++) {
                    weightage += Float.parseFloat(marks[k]);
                }
                String[][] attendanceInfo = Attendance.getAttendanceInfo(ids[j], courseName[i]);
                int attendance = 0;
                for (int k = 0; k < attendanceInfo.length; k++) {
                    if(attendanceInfo[k][1].equals("P"))
                        attendance += 1;
                }
                if(attendanceInfo.length != 0){
                    attendance = (attendance * 100) / attendanceInfo.length;
                }
                if(weightage >= 50f && attendance >= 60){
                    Course.coursePassed(ids[j], courseName[i]);
                }
                else{
                    Course.courseFailed(ids[j], courseName[i]);
                }
            }
            Attendance.removeAll(courseName[i]);
        }
        String[] ids = Info.allID();
        for (int i = 0; i < ids.length; i++) {
            if(ids[i].charAt(0) == 'T')
                continue;
            String[][] courseInfo = Course.getSingleStudentCourseStatus(ids[i]);
            boolean increaseSemester = true;
            for (int j = 0; j < courseInfo.length; j++) {
                if(courseInfo[j][1].equals("NE")){
                    increaseSemester = false;
                    break;
                }
            }
            if(increaseSemester){
                Info.increaseSemesterByOne(ids[i]);
            }
        }
    }

    public static void setRegistrationPeriod(boolean period){
        File filename = new File(System.getProperty("user.dir") + "\\src\\com\\Admin\\registrationPeriod.txt");
        try{
            filename.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename));
            bufferedWriter.write(period + "");
            bufferedWriter.close();
        }catch (IOException ioe){
            System.out.println("Something went Wrong, Registration Period");
        }
    }

    public static boolean getRegistrationPeriod(){
        File filename = new File(System.getProperty("user.dir") + "\\src\\com\\Admin\\registrationPeriod.txt");
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            boolean period = Boolean.parseBoolean(bufferedReader.readLine());
            bufferedReader.close();
            return period;
        }catch (IOException ioe){
            System.out.println("Something went Wrong, Registration Period");
        }
        return false;
    }
    public static void create(){
        File filename = new File(System.getProperty("user.dir") + "\\src\\com\\Admin\\registrationPeriod.txt");
        try{
            filename.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename));
            bufferedWriter.write("true");
            bufferedWriter.close();
        }catch (IOException ioe){
            System.out.println("Something went Wrong, Registration Period");
        }
    }
}
