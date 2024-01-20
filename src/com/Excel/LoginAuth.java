package com.Excel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.*;

import java.io.File;

public class LoginAuth extends Excel{
    final private static String EXCEL_NAME = System.getProperty("user.dir") + "\\ExcelFiles\\LoginPortal\\LoginPortal.xls";
    final private static int NUMBER_OF_SHEETS = 4;

    public static String login(String username, String password){
        int first_char = Character.toUpperCase(username.charAt(0));
        if(first_char >=65 && first_char <= 90){
            first_char -= 65;
        }
        else{
            first_char = 26;
        }

        Workbook workbook = getReadAbleWorkbook(EXCEL_NAME);

        Sheet[] sheets = getReadAbleSheets(workbook, NUMBER_OF_SHEETS);
        int found_on_row = 0;
        boolean found = false;
        int total_rows = Integer.parseInt(workbook.getSheet(NUMBER_OF_SHEETS - 1).getCell(first_char, 0).getContents());
        for(int i = 0; i < total_rows; i++){
            String content = sheets[0].getCell(first_char, i).getContents();
            if(content.equals(username)) {
                found = true;
                found_on_row = i;
                break;
            }
        }

        if(found){
            String content = sheets[1].getCell(first_char, found_on_row).getContents();
            if(content.equals(password)){
                String id = sheets[2].getCell(first_char, found_on_row).getContents();
                Close(workbook, EXCEL_NAME);
                return id;
            }
            else{
                Close(workbook, EXCEL_NAME);
                return "password does not match";
            }
        }
        Close(workbook, EXCEL_NAME);
        return "username does not match";
    }

    public static String signUp(String username, String password, String id) {
        if(!login(username, "").equals("username does not match")){
            return "Username already exists";
        }

        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME);

        String[] details = {username, password, id};
        WritableSheet[] writableSheets = getEditAbleSheets(writableWorkbook, NUMBER_OF_SHEETS);

        int first_char = Character.toUpperCase(username.charAt(0));
        if(first_char >=65 && first_char <= 90)
            first_char -= 65;
        else
            first_char = 26;

        int total_rows = Integer.parseInt(writableSheets[NUMBER_OF_SHEETS - 1].getCell(first_char, 0).getContents());

        for(int i = 0; i < NUMBER_OF_SHEETS - 1; i++){
            writeToSheet(writableSheets[i], details[i], total_rows, first_char);
        }

        writeToSheet(writableSheets[NUMBER_OF_SHEETS - 1], total_rows + 1, 0, first_char);

        WriteAndClose(writableWorkbook, EXCEL_NAME);
        return "Successful";
    }
    static void createExcel(){
        WritableWorkbook writableWorkbook;

        try{
            writableWorkbook = Workbook.createWorkbook(new File(EXCEL_NAME));
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Could not create Excel " + EXCEL_NAME + "!");
            return;
        }

        writableWorkbook.createSheet("username", 0);
        writableWorkbook.createSheet("password", 1);
        writableWorkbook.createSheet("id", 2);
        WritableSheet length = writableWorkbook.createSheet("length", NUMBER_OF_SHEETS - 1);

        for(int i = 0; i < 27; i++){
            writeToSheet(length, 0, 0, i);
        }
        WriteAndClose(writableWorkbook, EXCEL_NAME);
    }
}
