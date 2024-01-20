package com.Admin;

import com.GUI.Frame;
import com.StartPoint.LoginScreen;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AdminStarting extends Frame {
    JButton[] buttons = new JButton[4];
    public AdminStarting(){

        this.add(buttons[0] = getButton("Student", 175, 25, 150, 35));
        buttons[0].addActionListener(this);

        this.add(buttons[1] = getButton("Teacher", 175, 85, 150, 35));
        buttons[1].addActionListener(this);

        this.add(buttons[2] = getButton("Registration Period", 175, 145, 150, 35));
        buttons[2].addActionListener(this);

        this.add(buttons[3] = getButton("Logout", 175, 205, 150, 35));
        buttons[3].addActionListener(this);

        defaultJFrame(500, 500, "Admin Portal");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttons[0]){
            this.dispose();
            new StudentCreation();
        }
        else if(e.getSource() == buttons[1]){
            this.dispose();
            new TeacherCreation();
        }
        else if(e.getSource() == buttons[2]){
            this.dispose();
            new RegistrationPeriod();
        }
        else{
            this.dispose();
            new LoginScreen();
        }
    }
}
