package com.Excel;

import com.Admin.RegistrationPeriod;
import com.Admin.SavingAndGettingId;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.io.IOException;

class Excel {
    static Sheet[] getReadAbleSheets(Workbook workbook, int number_of_sheets) {
        Sheet[] sheets = new Sheet[number_of_sheets];
        for(int i = 0; i < number_of_sheets; i++){
            sheets[i] = workbook.getSheet(i);
        }
        return sheets;
    }

    static WritableSheet[] getEditAbleSheets(WritableWorkbook writableWorkbook, int number_of_sheets) {
        WritableSheet[] writableSheets = new WritableSheet[number_of_sheets];
        for(int i = 0; i < number_of_sheets; i++){
            writableSheets[i] = writableWorkbook.getSheet(i);
        }
        return writableSheets;
    }

    static Workbook getReadAbleWorkbook(String filename){
        Workbook workbook;
        try {
            workbook = Workbook.getWorkbook(new File(filename));
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Problem with opening " + filename + "!");
            return null;
        }
        return workbook;
    }

    static WritableWorkbook getEditAbleWorkbook(String filename) {
        Workbook workbook;
        try {
            workbook = Workbook.getWorkbook(new File(filename));
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Problem with opening " + filename + "!");
            return null;
        }

        WritableWorkbook writableWorkbook;

        try{
            writableWorkbook = Workbook.createWorkbook(new File(filename), workbook);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Problem with opening " + filename + " AS WriteAbleWorkBook!");
            workbook.close();
            return null;
        }
        return writableWorkbook;
    }

    static void writeToSheet(WritableSheet writableSheets, String data, int row, int col){
        try{
            WritableCell cell = new Label(col, row, data);
            writableSheets.addCell(cell);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Could not Write to Sheet " + writableSheets.getName() + "!");
        }
    }
    static void writeToSheet(WritableSheet writableSheets, float data, int row, int col){
        try{
            WritableCell cell = new Number(col, row, data);
            writableSheets.addCell(cell);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Could not Write to Sheet " + writableSheets.getName() + "!");
        }
    }

    static void WriteAndClose(WritableWorkbook writableWorkbook, String EXCEL_NAME){
        try {
            writableWorkbook.write();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Could not Write to Excel " + EXCEL_NAME + "!");
        }
        try {
            writableWorkbook.close();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Could not close Excel " + EXCEL_NAME + "!");
        }
    }

    static void Close(Workbook workbook, String name){
        try {
            workbook.close();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Could not close Excel " + name + "!");
        }
    }

    static void createExcel(int NUMBER_OF_SHEETS, String EXCEL_NAME, String[][] COLUMN_NAME, int[] TOTAL_COLUMNS) throws IOException {
        WritableWorkbook writableWorkbook = Workbook.createWorkbook(new File(EXCEL_NAME));
        WritableSheet[] writableSheet = new WritableSheet[NUMBER_OF_SHEETS];

        for(int i = 0; i < NUMBER_OF_SHEETS - 1; i++){
            writableSheet[i] = writableWorkbook.createSheet("Semester" + (i + 1), i);
        }
        writableSheet[NUMBER_OF_SHEETS - 1] = writableWorkbook.createSheet("length", NUMBER_OF_SHEETS - 1);

        for(int i = 0; i < NUMBER_OF_SHEETS - 1; i++){
            for(int j = 0; j < TOTAL_COLUMNS[i]; j++){
                writeToSheet(writableSheet[i], COLUMN_NAME[i][j], 0, j);
            }
            writeToSheet(writableSheet[NUMBER_OF_SHEETS - 1], 1, 0, i);
        }

        WriteAndClose(writableWorkbook, EXCEL_NAME);
    }

    public static void createAll() throws IOException {
        Info.createExcel();
        Course.createExcel();
        GradeChangeRequest.createExcel();
        Marks.createExcel();
        Attendance.createExcel();
        LoginAuth.createExcel();
        Transcript.createExcel();
        SavingAndGettingId.createNew();
        RegistrationPeriod.create();
    }
}
