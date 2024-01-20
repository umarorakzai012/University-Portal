package com.Excel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.*;

import java.io.File;
import java.io.IOException;

public class Info extends Excel{

    final private static String EXCEL_NAME = System.getProperty("user.dir") + "\\ExcelFiles\\Info\\Info.xls";
    final private static int TOTAL_COLUMNS = 8;

    public static void increaseSemesterByOne(String id){
        String semester = Info.getSingleInfo(id, "Semester");
        int sem = Integer.parseInt(semester);
        Info.changeSingleInfo(id, "Semester", (sem + 1) + "");
        Course.increaseSemesterByOne(id);
        GradeChangeRequest.increaseSemesterByOne(id);
        Transcript.increaseSemesterByOne(id);
    }
    public static String[] allID(){
        Workbook workbook = getReadAbleWorkbook(EXCEL_NAME);
        Sheet sheet = workbook.getSheet(0);
        int total_rows = sheet.getRows();
        String[] id = new String[total_rows - 1];
        for(int i = 1; i < total_rows; i++){
            id[i - 1] = sheet.getCell(0, i).getContents();
        }
        Close(workbook, EXCEL_NAME);
        return id;
    }

    public static String[] ReadDetails(String id){
        Workbook workbook = getReadAbleWorkbook(EXCEL_NAME);
        Sheet sheet = workbook.getSheet(0);
        int total_row = sheet.getRows();
        String[] details = new String[TOTAL_COLUMNS];
        for(int i = 1; i < total_row; i++){
            if(id.equals(sheet.getCell(0, i).getContents())){
                for(int j = 0; j < TOTAL_COLUMNS; j++){
                    details[j] = sheet.getCell(j, i).getContents();
                }
                Close(workbook, EXCEL_NAME);
                return details;
            }
        }
        Close(workbook, EXCEL_NAME);
        return new String[]{"Did not Found"};
    }

    public static String getSingleInfo(String id, String info_of){
        Workbook workbook = getReadAbleWorkbook(EXCEL_NAME);

        Sheet sheet = workbook.getSheet(0);
        int total_rows = sheet.getRows();
        for(int i = 1; i < total_rows; i++){
            if(id.equals(sheet.getCell(0, i).getContents())){
                for(int j = 1; j < TOTAL_COLUMNS; j++){
                    if(sheet.getCell(j, 0).getContents().equals(info_of)){
                        String content =  sheet.getCell(j, i).getContents();
                        Close(workbook, EXCEL_NAME);
                        return content;
                    }
                }
                Close(workbook, EXCEL_NAME);
                return "Could not find " + info_of + " column";
            }
        }

        Close(workbook, EXCEL_NAME);
        return "Could not find " + id;
    }

    public static String changeSingleInfo(String id, String changeColumnName, String changedInfo){
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME);
        WritableSheet writableSheet = writableWorkbook.getSheet(0);

        int total_rows = writableSheet.getRows();
        for(int i = 1; i < total_rows; i++){
            if(id.equals(writableSheet.getCell(0, i).getContents())){
                for(int j = 1; j < TOTAL_COLUMNS; j++){
                    if(writableSheet.getCell(j, 0).getContents().equals(changeColumnName)){
                        writeToSheet(writableSheet, changedInfo, i, j);
                        WriteAndClose(writableWorkbook, EXCEL_NAME);
                        return "Successfully Changed Info";
                    }
                }
                WriteAndClose(writableWorkbook, EXCEL_NAME);
                return "Could not find " + changeColumnName + " column";
            }
        }

        WriteAndClose(writableWorkbook, EXCEL_NAME);
        return "Could not find " + id;
    }

    public static void SaveDetails(String id, String name, String Fname, String DOB, String CNIC, String email, String phone_number, String semester) {
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME);

        WritableSheet writableSheet = writableWorkbook.getSheet(0);

        String[] data = {id, name, Fname, DOB, CNIC, email, phone_number, semester};
        int total_row = writableSheet.getRows();
        for(int i = 0; i < TOTAL_COLUMNS; i++){
            writeToSheet(writableSheet, data[i], total_row, i);
        }

        WriteAndClose(writableWorkbook, EXCEL_NAME);

        if(id.charAt(0) != 'T'){
            Course.addStudent(id);
            GradeChangeRequest.addStudent(id);
            Transcript.addStudent(id);
        }
    }

    static void createExcel() throws IOException {
        WritableWorkbook writableWorkbook = Workbook.createWorkbook(new File(EXCEL_NAME));
        WritableSheet writableSheet1 = writableWorkbook.createSheet("Info", 0);
        WritableSheet writableSheet2 = writableWorkbook.createSheet("Length", 1);

        String[] columns = {"ID", "Name", "Father Name", "DOB", "CNIC", "Email", "Phone Number", "Semester"};
        for(int i = 0; i < columns.length; i++){
            writeToSheet(writableSheet1, columns[i], 0, i);
        }

        writeToSheet(writableSheet2, 1, 0, 0);

        WriteAndClose(writableWorkbook, EXCEL_NAME);
    }
}