package com.Excel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.IOException;

public class GradeChangeRequest extends Excel{
    final private static String EXCEL_NAME = System.getProperty("user.dir") + "\\ExcelFiles\\GradeChangeRequest\\GradeChangeRequest.xls";
    final private static int NUMBER_OF_SHEETS = 9;
    final private static String[][] COLUMN_NAME = {
            {"ID", "PF", "PF Lab", "ICT", "ECC", "ECC Lab", "LA", "Cal", "PST"},
            {"ID", "OOP", "OOP Lab", "DLD", "DLD Lab", "DE", "ISL", "CPS", "CPS Lab"},
            {"ID", "PA", "PA Lab", "DS", "DS Lab", "Discrete", "PS", "UE-1"},
            {"ID", "AI", "AI Lab", "FSE", "Database", "Database Lab", "COAL", "COAL Lab"},
            {"ID", "KRR", "ML", "ML Lab", "OS", "OS Lab", "DAA", "TBW"},
            {"ID", "ANN", "CN", "CN Lab", "PDC", "AIE-1", "AIE-2"},
            {"ID", "FNLP", "CV", "CV Lab", "FYP-1", "AIE-3", "UE-2"},
            {"ID", "FYP-2", "IS", "PP", "UE-3", "AIE-4"},
    };
    final private static int[] TOTAL_COLUMNS = {9, 9, 8, 8, 8, 7, 7, 6};

    public static String getRequest(String id, String courseName){
        Workbook workbook = getReadAbleWorkbook(EXCEL_NAME);
        int sem = Integer.parseInt(Info.getSingleInfo(id, "Semester"));
        Sheet sheet = workbook.getSheet("Semester" + sem);
        int total_rows = sheet.getRows();

        for(int i = 1; i < total_rows; i++){
            if(id.equals(sheet.getCell(0, i).getContents())){
                for(int j = 1; j < TOTAL_COLUMNS[sem - 1]; j++){
                    if(courseName.equals(sheet.getCell(j, 0).getContents())){
                        String request = sheet.getCell(j, i).getContents();
                        Close(workbook, EXCEL_NAME);
                        return request;
                    }
                }
            }
        }
        Close(workbook, EXCEL_NAME);
        return "Could not find ID: " + id;
    }

    public static String setRequest(String id, String request, String courseName){
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME);

        int sem = Integer.parseInt(Info.getSingleInfo(id, "Semester"));

        WritableSheet writableSheet = writableWorkbook.getSheet(sem - 1);
        int total_rows = writableSheet.getRows();

        for(int i = 1; i < total_rows; i++){
            if(writableSheet.getCell(0, i).getContents().equals(id)){
                for(int j = 1; j < TOTAL_COLUMNS[sem - 1]; j++){
                    if(writableSheet.getCell(j, 0).getContents().equals(courseName)){
                        writeToSheet(writableSheet, request, i, j);
                        WriteAndClose(writableWorkbook, EXCEL_NAME);
                        return "Successful";
                    }
                }
                WriteAndClose(writableWorkbook, EXCEL_NAME);
                return "Could not find " + courseName;
            }
        }

        WriteAndClose(writableWorkbook, EXCEL_NAME);
        return "Could not find " + id;
    }

    static String increaseSemesterByOne(String id){

        String semester = Info.getSingleInfo(id, "Semester");
        if(semester.equals("Could not find " + id)){
            return "ID not found!";
        }

        int sem = Integer.parseInt(semester) - 1, foundOn = 0;
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME);
        WritableSheet writableSheet = writableWorkbook.getSheet(sem - 1);
        int total_rows = writableSheet.getRows();

        for(int i = 1; i < total_rows; i++){
            if(id.equals(writableSheet.getCell(0, i).getContents())){
                foundOn = i;
                break;
            }
        }
        writableSheet.removeRow(foundOn);
        WriteAndClose(writableWorkbook, EXCEL_NAME);
        addStudent(id);
        return "Successful";
    }
    static void addStudent(String id){
        String semester = Info.getSingleInfo(id, "Semester");
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME);

        int sem = Integer.parseInt(semester);
        WritableSheet writableSheet = writableWorkbook.getSheet(sem - 1);
        int total_row = writableSheet.getRows();

        writeToSheet(writableSheet, id, total_row, 0);
        for(int i = 1; i < TOTAL_COLUMNS[sem - 1]; i++){
            writeToSheet(writableSheet, "-", total_row, i);
        }

        WriteAndClose(writableWorkbook, EXCEL_NAME);
    }
    static void createExcel() throws IOException {
        Excel.createExcel(NUMBER_OF_SHEETS, EXCEL_NAME, COLUMN_NAME, TOTAL_COLUMNS);
    }
}
