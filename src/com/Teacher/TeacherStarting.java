package com.Teacher;

import com.Admin.RegistrationPeriod;
import com.Excel.Attendance;
import com.Excel.Info;
import com.GUI.Frame;
import com.StartPoint.LoginScreen;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TeacherStarting extends Frame {
    JButton[] button = new JButton[4];
    String id, courseName;
    public TeacherStarting(String id){
        this.id = id;
        String course = Info.getSingleInfo(id, "Semester");
        this.courseName = course;

        this.add(button[0] = getButton("Marks", 140, 70, 200, 35));
        button[0].addActionListener(this);

        this.add(button[1] = getButton("Attendance", 140, 140, 200, 35));
        button[1].addActionListener(this);

        this.add(button[2] = getButton("Grade Change Request", 140, 210, 200, 35));
        button[2].addActionListener(this);

        this.add(button[3] = getButton("Logout", 140, 280, 200, 35));
        button[3].addActionListener(this);

        defaultJFrame(500, 500, "Teacher Portal");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button[0]){
            if(!RegistrationPeriod.getRegistrationPeriod()){
                this.dispose();
                new MarksTeacher(id, courseName);
            }
            else{
                JOptionPane.showMessageDialog(null, "Registration Period is On", "", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if(e.getSource() == button[1]){
            if(RegistrationPeriod.getRegistrationPeriod()){
                JOptionPane.showMessageDialog(null, "Registration Period is On", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int result = JOptionPane.showConfirmDialog(null,"For All Students?");
            if(result == JOptionPane.YES_OPTION){
                this.dispose();
                new AttendanceTeacher(id, courseName);
            }
            else if(result == JOptionPane.NO_OPTION){
                String studentID = JOptionPane.showInputDialog(null, "ID");
                String date = JOptionPane.showInputDialog(null, "DATE");
                String atten;
                int attendance = JOptionPane.showConfirmDialog(null, "Present?", "", JOptionPane.YES_NO_OPTION);
                if(attendance == JOptionPane.YES_OPTION)
                    atten = "P";
                else
                    atten = "A";
                String returnString  = Attendance.addAttendance(studentID, courseName, date, atten);
                if(!returnString.equals("Successful")){
                    JOptionPane.showMessageDialog(null, returnString, "", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if(e.getSource() == button[2]){
            this.dispose();
            new GradeChangeRequestTeacher(id, courseName);
        }
        else{
            this.dispose();
            new LoginScreen();
        }
    }
}
