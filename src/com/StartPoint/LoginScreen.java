package com.StartPoint;

import com.Admin.AdminStarting;
import com.Excel.LoginAuth;
import com.GUI.Frame;
import com.Student.InfoStudent;
import com.Teacher.TeacherStarting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginScreen extends Frame {

    JButton[] button = new JButton[2];
    JLabel[] labels = new JLabel[2];
    JTextField textField;
    JPasswordField passwordField = new JPasswordField();

    public LoginScreen(){

        button[0] = getButton("Login", 250, 225, 75, 25);
        button[0].addActionListener(this);

        this.add(button[0]);
        this.add(button[1] = getButton("Exit", 125, 225, 75, 25));
        button[1].addActionListener(this);

        this.add(labels[0] = getLabel("username", 125, 125, 75, 25));
        labels[0].setFont(new Font("JetBrains Mono Regular", Font.PLAIN, 13));
        this.add(textField = getTextField(220, 125, 150, 25));

        this.add(labels[1] = getLabel("password", 125, 175, 75, 25));
        passwordField.setBounds(220, 175, 150, 25);
        this.add(passwordField);


        defaultJFrame(500, 400, "Login Portal");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button[0]) {
            String username = textField.getText();
            if(username.equals("")) return;
            String password = passwordField.getText();
            if(password.equals("")) return;
            String returnMessage = LoginAuth.login(username, password);
            if(returnMessage.equals("username does not match") || returnMessage.equals("password does not match")){
                JOptionPane.showMessageDialog(
                        null,
                        "username or password does not match",
                        "Alert",
                        JOptionPane.WARNING_MESSAGE
                );
            }else {
                this.dispose();
                String id = returnMessage;
                switch (id.charAt(0)){
                    case 'S':
                        new InfoStudent(id);
                        break;
                    case 'T':
                        new TeacherStarting(id);
                        break;
                    case 'A':
                        new AdminStarting();
                        break;
                }
            }
        }
        else if(e.getSource() == button[1]){
            this.dispose();
            System.exit(0);
        }
    }
}