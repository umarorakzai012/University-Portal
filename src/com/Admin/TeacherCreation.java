package com.Admin;

import com.Excel.Info;
import com.Excel.LoginAuth;
import com.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TeacherCreation extends Frame {
    JTextField[] textFields = new JTextField[6];
    JButton[] button = new JButton[2];
    JComboBox comboBox;
    String teacherID;
    final private static String[] list = {
            "PF", "PF Lab", "ICT", "ECC", "ECC Lab", "LA", "Cal", "PST",
            "OOP", "OOP Lab", "DLD", "DLD Lab", "DE", "ISL", "CPS", "CPS Lab",
            "PA", "PA Lab", "DS", "DS Lab", "Discrete", "PS", "UE-1",
            "AI", "AI Lab", "FSE", "Database", "Database Lab", "COAL", "COAL Lab",
            "KRR", "ML", "ML Lab", "OS", "OS Lab", "DAA", "TBW",
            "ANN", "CN", "CN Lab", "PDC", "AIE-1", "AIE-2",
            "FNLP", "CV", "CV Lab", "FYP-1", "AIE-3", "UE-2",
            "FYP-2", "IS", "PP", "UE-3", "AIE-4",
    };
    TeacherCreation(){
        String[] fields = {
                String.format("%25s", "Name"),
                String.format("%25s", "Father Name"),
                String.format("%25s", "Date Of Birth"),
                String.format("%25s", "CNIC"),
                String.format("%25s", "Email"),
                String.format("%25s", "Phone Number"),
                String.format("%25s", "Course")
        };

        for(int i = 0; i < fields.length - 1; i++){
            this.add(getLabel(fields[i], 25, (i * 35) + 50, 200, 25));
            this.add(textFields[i] = getTextField(250, (i * 35) + 50, 100, 25));
        }

        this.add(getLabel(fields[fields.length - 1], 25, ((fields.length - 1) * 35) + 50, 200, 25));
        this.add(comboBox = getComboBox(list, 250, ((fields.length - 1) * 35) + 50, 100, 25));

        this.add(button[0] = getButton("Create", 250, ((fields.length) * 35) + 75, 100, 35));
        button[0].addActionListener(this);

        this.add(button[1] = getButton("Back", 125, ((fields.length) * 35) + 75, 100, 35));
        button[1].addActionListener(this);

        defaultJFrame(500, 500, "Teacher Creation");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button[0]){
            this.dispose();
            SavingAndGettingId savingAndGettingId = new SavingAndGettingId();
            teacherID = savingAndGettingId.getTeacherID();
            savingAndGettingId.save();
            Info.SaveDetails(
                    teacherID,
                    textFields[0].getText(),
                    textFields[1].getText(),
                    textFields[2].getText(),
                    textFields[3].getText(),
                    textFields[4].getText(),
                    textFields[5].getText(),
                    comboBox.getItemAt(comboBox.getSelectedIndex()).toString()
            );
            LoginAuth.signUp(teacherID.toLowerCase(), teacherID.toLowerCase(), teacherID);
            new AdminStarting();
        }
        else{
            this.dispose();
            new AdminStarting();
        }
    }
}
