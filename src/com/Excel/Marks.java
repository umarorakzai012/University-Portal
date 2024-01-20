package com.Excel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.io.IOException;

public class Marks extends Excel{
    final private static String[] EXCEL_NAME_ENROLLED = {
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\Semester1.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\Semester2.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\Semester3.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\Semester4.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\Semester5.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\Semester6.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\Semester7.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\Semester8.xls"
    };

    final private static String[] EXCEL_NAME_PASSED = {
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\passedSemester1.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\passedSemester2.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\passedSemester3.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\passedSemester4.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\passedSemester5.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\passedSemester6.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\passedSemester7.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Marks\\passedSemester8.xls"
    };
    final private static String[][] SHEET_NAME = {
            {"PF", "PF Lab", "ICT", "ECC", "ECC Lab", "LA", "Cal", "PST", "length"},
            {"OOP", "OOP Lab", "DLD", "DLD Lab", "DE", "ISL", "CPS", "CPS Lab", "length"},
            {"PA", "PA Lab", "DS", "DS Lab", "Discrete", "PS", "UE-1", "length"},
            {"AI", "AI Lab", "FSE", "Database", "Database Lab", "COAL", "COAL Lab", "length"},
            {"KRR", "ML", "ML Lab", "OS", "OS Lab", "DAA", "TBW", "length"},
            {"ANN", "CN", "CN Lab", "PDC", "AIE-1", "AIE-2", "length"},
            {"FNLP", "CV", "CV Lab", "FYP-1", "AIE-3", "UE-2", "length"},
            {"FYP-2", "IS", "PP", "UE-3", "AIE-4", "length"},
            {"Semester1", "Semester2", "Semester3", "Semester4", "Semester5", "Semester6", "Semester7", "Semester8", "length"}
    };
    final private static int[] NUMBER_OF_SHEETS = {9, 9, 8, 8, 8, 7, 7, 6, 9};
    final private static String[] COLUMNS_NAME = {"ID", "Assignment 1", "Assignment 2", "Assignment 3", "Quiz 1", "Quiz 2", "Quiz 3", "Sessional 1", "Sessional 2", "Final"};
    final private static int TOTAL_COLUMNS = COLUMNS_NAME.length;

    public static String[] getPassedSemesterMarks(String id, String semester){
        int sem = Integer.parseInt(semester);
        Workbook workbook = getReadAbleWorkbook(EXCEL_NAME_PASSED[sem - 1]);
        Sheet[] sheets = getReadAbleSheets(workbook, NUMBER_OF_SHEETS[sem - 1] - 1);
        String[] formattedString = new String[sheets.length];
        for(int i = 0; i < sheets.length; i++){
            formattedString[i] = String.format("%-12s ", sheets[i].getName());
            int totalRows = sheets[i].getRows();
            for(int j = 0; j < totalRows; j++){
                if(sheets[i].getCell(0, j).getContents().equals(id)){
                    for(int k = 1; k < COLUMNS_NAME.length; k++){
                        String marks = sheets[i].getCell(k, j).getContents();
                        if(marks.equals(""))
                            formattedString[i] += String.format("%-5s ", "0");
                        else
                            formattedString[i] += String.format("%-5s ", marks);
                    }
                    break;
                }
            }
        }
        Close(workbook, EXCEL_NAME_PASSED[sem - 1]);
        return formattedString;
    }

    public static float getPassedCourseMarks(String id, String courseName, String semester){
        int sem = Integer.parseInt(semester), foundIdOnRow = 0;
        Workbook workbook = getReadAbleWorkbook(EXCEL_NAME_PASSED[sem - 1]);
        Sheet sheet = workbook.getSheet(courseName);

        int total_rows = sheet.getRows();
        for(int i = 1; i < total_rows; i++){
            if(id.equals(sheet.getCell(0, i).getContents())){
                foundIdOnRow = i;
                break;
            }
        }
        float grandTotal = 0;
        for(int i = 1; i < TOTAL_COLUMNS; i++){
            String marks = sheet.getCell(i, foundIdOnRow).getContents();
            if(marks.equals(""))
                grandTotal += 0;
            else
                grandTotal += Float.parseFloat(marks);
        }
        Close(workbook, EXCEL_NAME_PASSED[sem - 1]);
        return grandTotal;
    }

    static String courseFailed(String id, String courseName){
        String semester = Info.getSingleInfo(id, "Semester");
        int sem = Integer.parseInt(semester);
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME_ENROLLED[sem - 1]);
        WritableSheet writableSheet = writableWorkbook.getSheet(courseName);

        if(writableSheet == null) {
            return "Could not find Course " + courseName;
        }
        int total_rows = writableSheet.getRows();
        for(int i = 1; i < total_rows; i++){
            if(id.equals(writableSheet.getCell(0, i).getContents())){
                writableSheet.removeRow(i);
                WriteAndClose(writableWorkbook, EXCEL_NAME_ENROLLED[sem - 1]);
                return "Successful";
            }
        }
        return "Could not find ID" + id;
    }
    static String coursePassed(String id, String courseName){
        String semester = Info.getSingleInfo(id, "Semester");
        int sem = Integer.parseInt(semester) - 1;
        String[] details = new String[COLUMNS_NAME.length - 1];
        {
            int foundIdOnRow = 0;
            WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME_ENROLLED[sem - 1]);
            WritableSheet writableSheet = writableWorkbook.getSheet(courseName);
            if(writableSheet == null) {
                WriteAndClose(writableWorkbook, EXCEL_NAME_ENROLLED[sem - 1]);
                return "Could not Find Course " + courseName;
            }
            int total_rows = writableSheet.getRows();
            for(int i = 1; i < total_rows; i++){
                if(id.equals(writableSheet.getCell(0, i).getContents())){
                    foundIdOnRow = i;
                    break;
                }
            }
            for(int i = 1; i < TOTAL_COLUMNS; i++){
                details[i - 1] = writableSheet.getCell(i, foundIdOnRow).getContents();
            }
            writableSheet.removeRow(foundIdOnRow);
            WriteAndClose(writableWorkbook, EXCEL_NAME_ENROLLED[sem - 1]);
        }
        {
            WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME_PASSED[sem - 1]);
            WritableSheet writableSheet = writableWorkbook.getSheet(courseName);
            int totalRows = writableSheet.getRows();
            writeToSheet(writableSheet, id, totalRows, 0);
            for(int i = 1; i < TOTAL_COLUMNS; i++){
                writeToSheet(writableSheet, details[i - 1], totalRows, i);
            }
            WriteAndClose(writableWorkbook, EXCEL_NAME_PASSED[sem - 1]);
        }
        return "Successful";
    }
    public static String[] getMarksInfo(String id, String courseName){
        String semester = Info.getSingleInfo(id, "Semester");
        int sem = Integer.parseInt(semester), foundIdOnRow = 0;
        Workbook workbook = getReadAbleWorkbook(EXCEL_NAME_ENROLLED[sem - 1]);
        Sheet sheet = workbook.getSheet(courseName);

        if(sheet == null){
            return null;
        }
        int total_rows = sheet.getRows();
        String[] details = new String[TOTAL_COLUMNS - 1];
        for(int i = 1; i < total_rows; i++){
            if(id.equals(sheet.getCell(0, i).getContents())){
                foundIdOnRow = i;
                break;
            }
        }
        if(foundIdOnRow == 0){
            return null;
        }
        for(int i = 1; i < TOTAL_COLUMNS; i++){
            String marks = sheet.getCell(i, foundIdOnRow).getContents();
            if(marks.equals(""))
                details[i - 1] = "0";
            else
                details[i - 1] = marks;
        }
        Close(workbook, EXCEL_NAME_ENROLLED[sem - 1]);
        return details;
    }
    public static String addMarks(String id, String courseName, float weightage, String marksFor){
        String semester = Info.getSingleInfo(id, "Semester");
        int sem = Integer.parseInt(semester);
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME_ENROLLED[sem - 1]);
        WritableSheet writableSheet = writableWorkbook.getSheet(courseName);
        if(writableSheet == null){
            return "Could not Find Course " + courseName;
        }
        int total_rows = writableSheet.getRows();
        int foundOnMarksForColumn = 0, foundIdOnRow = 0;
        for(int i = 1; i < TOTAL_COLUMNS; i++){
            if(marksFor.equals(COLUMNS_NAME[i])){
                foundOnMarksForColumn = i;
                break;
            }
        }
        for(int i = 1; i < total_rows; i++){
            if(id.equals(writableSheet.getCell(0, i).getContents())){
                foundIdOnRow = i;
                break;
            }
        }
        writeToSheet(writableSheet, weightage, foundIdOnRow, foundOnMarksForColumn);
        WriteAndClose(writableWorkbook, EXCEL_NAME_ENROLLED[sem - 1]);
        return "Successful";
    }
    static String addStudent(String id, String courseName){
        String semester = Info.getSingleInfo(id, "Semester");
        int sem = Integer.parseInt(semester);
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME_ENROLLED[sem - 1]);
        WritableSheet writableSheet = writableWorkbook.getSheet(courseName);

        if(writableSheet == null){
            return "Could not Find Course " + courseName;
        }

        int total_rows = writableSheet.getRows();
        writeToSheet(writableSheet, id, total_rows, 0);

        WriteAndClose(writableWorkbook, EXCEL_NAME_ENROLLED[sem - 1]);
        return "Successful";
    }
    static void createExcel() throws IOException {
        WritableWorkbook[] writableWorkbooksEnrolled = new WritableWorkbook[EXCEL_NAME_ENROLLED.length];
        WritableWorkbook[] writableWorkbooksPassed = new WritableWorkbook[EXCEL_NAME_PASSED.length];
        for(int i = 0; i < EXCEL_NAME_ENROLLED.length; i++){
            writableWorkbooksEnrolled[i] = Workbook.createWorkbook(new File(EXCEL_NAME_ENROLLED[i]));
            writableWorkbooksPassed[i] = Workbook.createWorkbook(new File(EXCEL_NAME_PASSED[i]));
        }

        for(int i = 0; i < EXCEL_NAME_ENROLLED.length; i++){
            WritableSheet writableSheetEnrolled = null;
            WritableSheet writableSheetPassed = null;
            for(int j = 0; j < NUMBER_OF_SHEETS[i]; j++){
                writableSheetEnrolled = writableWorkbooksEnrolled[i].createSheet(SHEET_NAME[i][j], j);
                writableSheetPassed = writableWorkbooksPassed[i].createSheet(SHEET_NAME[i][j], j);
                for(int k = 0; k < COLUMNS_NAME.length; k++){
                    if(j == NUMBER_OF_SHEETS[i] - 1)
                        break;
                    writeToSheet(writableSheetEnrolled, COLUMNS_NAME[k], 0, k);
                    writeToSheet(writableSheetPassed, COLUMNS_NAME[k], 0, k);
                }
            }
        }

        for(int i = 0; i < EXCEL_NAME_ENROLLED.length; i++){
            WritableSheet writableSheetEnrolledLast = writableWorkbooksEnrolled[i].getSheet(NUMBER_OF_SHEETS[i] - 1);
            WritableSheet writableSheetPassedLast = writableWorkbooksPassed[i].getSheet(NUMBER_OF_SHEETS[i] - 1);
            for(int j = 0; j < NUMBER_OF_SHEETS[i] - 1; j++){
                writeToSheet(writableSheetEnrolledLast, 1, 0, j);
                writeToSheet(writableSheetPassedLast, 1, 0, j);
            }
            WriteAndClose(writableWorkbooksEnrolled[i], EXCEL_NAME_ENROLLED[i]);
            WriteAndClose(writableWorkbooksPassed[i], EXCEL_NAME_ENROLLED[i]);
        }
    }
}
