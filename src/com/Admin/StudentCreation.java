package com.Admin;

import com.Excel.Info;
import com.Excel.LoginAuth;
import com.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class StudentCreation extends Frame {
    JTextField[] textFields = new JTextField[6];
    JButton[] button = new JButton[2];
    String studentId;
    public StudentCreation(){
        String[] fields = {
                String.format("%25s", "Name"),
                String.format("%25s", "Father Name"),
                String.format("%25s", "Date Of Birth"),
                String.format("%25s", "CNIC"),
                String.format("%25s", "Email"),
                String.format("%25s", "Phone Number")
        };

        for(int i = 0; i < fields.length; i++){
            this.add(getLabel(fields[i], 25, (i * 35) + 50, 200, 25));
            this.add(textFields[i] = getTextField(250, (i * 35) + 50, 100, 25));
        }

        this.add(button[0] = getButton("Create", 250, (fields.length * 35) + 75, 100, 35));
        button[0].addActionListener(this);

        this.add(button[1] = getButton("Back", 125, (fields.length * 35) + 75, 100, 35));
        button[1].addActionListener(this);

        defaultJFrame(500, 500, "Student Creation");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button[0]){
            this.dispose();
            SavingAndGettingId savingAndGettingId = new SavingAndGettingId();
            studentId = savingAndGettingId.getStudentID();
            savingAndGettingId.save();
            Info.SaveDetails(
                    studentId,
                    textFields[0].getText(),
                    textFields[1].getText(),
                    textFields[2].getText(),
                    textFields[3].getText(),
                    textFields[4].getText(),
                    textFields[5].getText(),
                    "1"
            );
            LoginAuth.signUp(studentId.toLowerCase(), studentId.toLowerCase(), studentId);
            new AdminStarting();
        }
        else{
            this.dispose();
            new AdminStarting();
        }
    }
}