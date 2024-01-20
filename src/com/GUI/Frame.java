package com.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class Frame  extends JFrame implements ActionListener {
    public JTextField getTextField(int x, int y, int width, int height){
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        return textField;
    }
    public JLabel getLabel(String message, int x, int y, int width, int height){
        JLabel labels = new JLabel(message);
        labels.setBounds(x, y, width, height);
        labels.setFont(new Font("JetBrains Mono Regular", Font.PLAIN, 13));
        return labels;
    }
    public void defaultJFrame(int width, int height, String title){
        this.setIconImage(new ImageIcon("flexIcon.png").getImage());
        this.setSize(width, height);
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null);
        setLocationRelativeTo(null);
        this.setVisible(true);
    }
    public JButton getButton(String message, int x, int y, int width, int height){
        JButton button = new JButton(message);
        button.setBounds(x, y, width, height);
        button.setFocusPainted(false);
        return button;
    }

    public JCheckBox getCheckBox(int x, int y, int width, int height){
        JCheckBox checkBox = new JCheckBox();
        checkBox.setBounds(x, y, width, height);
        return checkBox;
    }

    public JComboBox getComboBox(String[] lists, int x, int y, int width, int height){
        JComboBox comboBox = new JComboBox(lists);
        comboBox.setBounds(x, y, width, height);
        return comboBox;
    }
}
