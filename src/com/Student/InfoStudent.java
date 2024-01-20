package com.Student;

import com.Admin.RegistrationPeriod;
import com.Excel.Info;
import com.GUI.Frame;
import com.StartPoint.LoginScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InfoStudent extends Frame{
    JButton[] buttons;
    String id;
    public InfoStudent(String id){
        this.id = id;
        String[] details = Info.ReadDetails(id);
        int index = 0;
        String[] eachRowText = {
                        String.format("%-15s %-15s", "ID: ", details[index++]),
                        String.format("%-15s %-15s", "Name: ", details[index++]),
                        String.format("%-15s %-15s", "Father Name: ", details[index++]),
                        String.format("%-15s %-15s", "DOB: ", details[index++]),
                        String.format("%-15s %-15s", "CNIC: ", details[index++]),
                        String.format("%-15s %-15s", "Email: ", details[index++]),
                        String.format("%-15s %-15s", "Phone Number: ", details[index++]),
                        String.format("%-15s %-15s", "Semester: ", details[index++])
                };
        String[] options = {"Course Registration", "Attendance", "Marks", "Transcript", "Grade Change Request", "Logout"};
        buttons = new JButton[options.length];
        int i = 0;
        for(i = 0; i < eachRowText.length; i++){
            JLabel label = getLabel(eachRowText[i], 15, i * 25, 400, 35);
            label.setFont(new Font("JetBrains Mono Regular", Font.PLAIN, 13));
            this.add(label);
        }
        for(int j = 0; j < buttons.length; j++){
            this.add(buttons[j] = getButton(options[j], 100, (i + j) * 30, 250, 35));
            buttons[j].addActionListener(this);
        }
        defaultJFrame(500, 500, "Home");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttons[0]){
            if(RegistrationPeriod.getRegistrationPeriod()){
                this.dispose();
                new CourseRegistrationStudent(id);
            }
            else{
                JOptionPane.showMessageDialog(null, "Registration Period is Off", "", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if(e.getSource() == buttons[1]){
            this.dispose();
            new AttendanceStudent(id);
        } else if (e.getSource() == buttons[2]) {
            this.dispose();
            new MarksStudent(id);
        } else if (e.getSource() == buttons[3]) {
            this.dispose();
            new TranscriptStudent(id);
        } else if (e.getSource() == buttons[4]) {
            this.dispose();
            new GradeChangeRequestStudent(id);
        } else if (e.getSource() == buttons[5]){
            this.dispose();
            new LoginScreen();
        }
    }
}
