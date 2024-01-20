package com.Excel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.io.IOException;

public class Attendance extends Excel{
    final private static String[] EXCEL_NAME = {
            System.getProperty("user.dir") + "\\ExcelFiles\\Attendance\\Semester1.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Attendance\\Semester2.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Attendance\\Semester3.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Attendance\\Semester4.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Attendance\\Semester5.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Attendance\\Semester6.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Attendance\\Semester7.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Attendance\\Semester8.xls",
            System.getProperty("user.dir") + "\\ExcelFiles\\Attendance\\coursePassed.xls"
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

    public static String[][] getPassedSemesterAttendanceInfo(String id, String semester){
        int lastExcelFile = EXCEL_NAME.length - 1;
        Workbook workbook = getReadAbleWorkbook(EXCEL_NAME[lastExcelFile]);
        Sheet sheet = workbook.getSheet("Semester" + semester);
        int total_row = sheet.getRows();
        int foundIdOnRow = 0;
        String[][] details = new String[sheet.getColumns() - 1][2];
        for(int i = 1; i < total_row; i++){
            if(sheet.getCell(0, i).getContents().equals(id)){
                foundIdOnRow = i;
                break;
            }
        }
        for(int i = 0; i < details.length; i++){
            details[i][0] = sheet.getCell(i + 1, 0).getContents();
            details[i][1] = sheet.getCell(i + 1, foundIdOnRow).getContents();
        }
        Close(workbook, EXCEL_NAME[lastExcelFile]);
        return details;
    }
    public static int getPassedCourseAttendanceInfo(String id, String courseName){
        String semester = Info.getSingleInfo(id, "Semester");
        int sem = Integer.parseInt(semester);
        int lastExcelFile = EXCEL_NAME.length - 1;
        Workbook workbook = getReadAbleWorkbook(EXCEL_NAME[lastExcelFile]);
        Sheet sheet = workbook.getSheet(sem - 1);
        int total_row = sheet.getRows();
        int foundIdOnRow = 0, foundCourseOnColumn = 0;
        for(int i = 1; i < total_row; i++){
            if(sheet.getCell(0, i).getContents().equals(id)){
                foundIdOnRow = i;
                break;
            }
        }
        for(int i = 1; i < SHEET_NAME[sem - 1].length; i++){
            if(sheet.getCell(i, 0).getContents().equals(courseName)){
                foundCourseOnColumn = i;
                break;
            }
        }
        int percentage = Integer.parseInt(sheet.getCell(foundCourseOnColumn, foundIdOnRow).getContents());
        Close(workbook, EXCEL_NAME[lastExcelFile]);
        return percentage;
    }

    public static String removeAll(String courseName){
        int semester = 0, sheetNumber = 0;
        for(int i = 0; i < SHEET_NAME.length; i++){
            for(int j = 0; j < SHEET_NAME[i].length; j++){
                if(courseName.equals(SHEET_NAME[i][j])){
                    semester = i;
                    sheetNumber = j;
                    break;
                }
            }
            if(courseName.equals(SHEET_NAME[semester][sheetNumber]))
                break;
        }
        if(!courseName.equals(SHEET_NAME[semester][sheetNumber])){
            return "could not find course " + courseName;
        }
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME[semester]);
        WritableSheet writableSheet = writableWorkbook.getSheet(sheetNumber);

        int totalRows = writableSheet.getRows();
        int totalColumns = writableSheet.getColumns();

        for(int i = 1; i < totalRows; i++){
            writableSheet.removeRow(1);
        }
        for(int i = 1; i < totalColumns; i++){
            writableSheet.removeColumn(1);
        }

        WriteAndClose(writableWorkbook, EXCEL_NAME[semester]);
        return "Successful";
    }
    static String coursePassed(String id, String courseName){
        String semester = Info.getSingleInfo(id, "Semester");
        int sem = Integer.parseInt(semester), foundIdOnRow = 0;
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME[sem - 1]);
        WritableSheet writableSheet = writableWorkbook.getSheet(courseName);

        if(writableSheet == null) {
            WriteAndClose(writableWorkbook, EXCEL_NAME[sem - 1]);
            return "Could not Find Course " + courseName;
        }
        int total_columns = writableSheet.getColumns();
        int total_rows = writableSheet.getRows();
        for(int i = 1; i < total_rows; i++){
            if(id.equals(writableSheet.getCell(0, i).getContents())){
                foundIdOnRow = i;
                break;
            }
        }
        int attendancePercentage = 0, presentCount = 0, absentCount = 0;
        for(int i = 1; i < total_columns; i++){
            if(writableSheet.getCell(i, foundIdOnRow).getContents().equals("P"))
                presentCount++;
            else
                absentCount++;
        }
        attendancePercentage = (presentCount * 100) / (presentCount + absentCount);
        WritableWorkbook writableWorkbook1 = getEditAbleWorkbook(EXCEL_NAME[EXCEL_NAME.length - 1]);
        WritableSheet writableSheet1 = writableWorkbook1.getSheet(sem - 1);
        int totalrows_old = writableSheet1.getRows();

        boolean alreadyThere = false;
        int totalColumn = writableSheet1.getColumns();
        for (int j = 1; j < totalColumn; j++) {
            if(courseName.equals(writableSheet1.getCell(j, 0).getContents())){
                for(int i = 1; i < totalrows_old; i++){
                    if(id.equals(writableSheet1.getCell(0, i).getContents())){
                        alreadyThere = true;
                        writeToSheet(writableSheet1, attendancePercentage, i, j);
                        break;
                    }
                }
                if(!alreadyThere){
                    writeToSheet(writableSheet1, id, totalrows_old, 0);
                    writeToSheet(writableSheet1, attendancePercentage, totalrows_old, j);
                    alreadyThere = true;
                }
            }
            if(alreadyThere)
                break;
        }

        writableSheet.removeRow(foundIdOnRow);

        WriteAndClose(writableWorkbook, EXCEL_NAME[sem - 1]);
        WriteAndClose(writableWorkbook1, EXCEL_NAME[EXCEL_NAME.length - 1]);
        return "Successful";
    }

    public static String[][] getAttendanceInfo(String id, String courseName){
        String semester = Info.getSingleInfo(id, "Semester");
        int sem = Integer.parseInt(semester), foundIdOnRow = 0;
        Workbook workbook = getReadAbleWorkbook(EXCEL_NAME[sem - 1]);
        Sheet sheet = workbook.getSheet(courseName);

        if(sheet == null){
            Close(workbook, EXCEL_NAME[sem - 1]);
            return null;
        }
        int total_columns = sheet.getColumns();
        int total_rows = sheet.getRows();
        String details[][] = new String[total_columns - 1][2];
        for(int i = 1; i < total_rows; i++){
            if(id.equals(sheet.getCell(0, i).getContents())){
                foundIdOnRow = i;
                break;
            }
        }
        if(foundIdOnRow == 0){
            Close(workbook, EXCEL_NAME[sem - 1]);
            return null;
        }
        for(int i = 1; i < total_columns; i++){
            details[i - 1][0] = sheet.getCell(i, 0).getContents();
            details[i - 1][1] = sheet.getCell(i, foundIdOnRow).getContents();
        }
        Close(workbook, EXCEL_NAME[sem -1]);
        return details;
    }

    public static String addAttendance(String id, String courseName, String date, String attendance){
        String semester = Info.getSingleInfo(id, "Semester");
        if(semester.equals("Could not find " + id))
            return semester;
        int sem = Integer.parseInt(semester), foundDateOnColumn = 0, foundIdOnRow = 0;
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME[sem - 1]);
        WritableSheet writableSheet = writableWorkbook.getSheet(courseName);

        if(writableSheet == null){
            WriteAndClose(writableWorkbook, EXCEL_NAME[sem - 1]);
            return "Could not find Course " + courseName;
        }
        int total_columns = writableSheet.getColumns();
        int total_rows = writableSheet.getRows();
        for(int i = 1; i < total_columns; i++){
            if(date.equals(writableSheet.getCell(i, 0).getContents())){
                foundDateOnColumn = i;
                break;
            }
        }
        if(foundDateOnColumn == 0){
            WriteAndClose(writableWorkbook, EXCEL_NAME[sem - 1]);
            return "Could not find Date" + date;
        }
        for(int i = 1; i < total_rows; i++){
            if(id.equals(writableSheet.getCell(0, i).getContents())){
                foundIdOnRow = i;
                break;
            }
        }
        if(foundIdOnRow == 0){
            WriteAndClose(writableWorkbook, EXCEL_NAME[sem - 1]);
            return "Could not find ID " + id;
        }
        writeToSheet(writableSheet, attendance, foundIdOnRow, foundDateOnColumn);
        WriteAndClose(writableWorkbook, EXCEL_NAME[sem - 1]);
        return "Successful";
    }

    public static String addDate(String courseName, String date){
        int sem = 0;
        for(int i = 0; i < SHEET_NAME.length - 1; i++){
            for(int j = 0; j < SHEET_NAME[i].length - 1; j++){
                if(courseName.equals(SHEET_NAME[i][j])){
                    sem = i + 1;
                    break;
                }
            }
            if(sem != 0)
                break;
        }
        if(sem == 0)
            return "Could not Find Course " + courseName;
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME[sem - 1]);
        WritableSheet writableSheet = writableWorkbook.getSheet(courseName);

        if(writableSheet == null){
            return "Could not Find Course " + courseName;
        }
        int total_columns = writableSheet.getColumns();
        writeToSheet(writableSheet, date, 0, total_columns);
        WriteAndClose(writableWorkbook, EXCEL_NAME[sem - 1]);
        return "Successful";
    }
    static String addStudent(String id, String courseName){
        String semester = Info.getSingleInfo(id, "Semester");
        int sem = Integer.parseInt(semester);
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME[sem - 1]);
        WritableSheet writableSheet = writableWorkbook.getSheet(courseName);

        if(writableSheet == null){
            return "Could not Find Course " + courseName;
        }
        int total_rows = writableSheet.getRows();
        writeToSheet(writableSheet, id, total_rows, 0);
        WriteAndClose(writableWorkbook, EXCEL_NAME[sem - 1]);
        return "Successful";
    }

    static void createExcel() throws IOException {
        WritableWorkbook[] writableWorkbooks = new WritableWorkbook[EXCEL_NAME.length];
        for(int i = 0; i < EXCEL_NAME.length; i++){
            writableWorkbooks[i] = Workbook.createWorkbook(new File(EXCEL_NAME[i]));
        }
        for(int i = 0; i < NUMBER_OF_SHEETS.length; i++){
            WritableSheet[] writableSheets = new WritableSheet[NUMBER_OF_SHEETS.length - 1];
            for(int j = 0; j < NUMBER_OF_SHEETS[i] - 1; j++){
                writableSheets[j] = writableWorkbooks[i].createSheet(SHEET_NAME[i][j], j);
                writeToSheet(writableSheets[j], "ID", 0, 0);
            }
        }
        for(int i = 0; i < EXCEL_NAME.length - 1; i++){
            WritableSheet writableSheets = writableWorkbooks[i].createSheet(SHEET_NAME[i][SHEET_NAME[i].length - 1], NUMBER_OF_SHEETS[i] - 1);
            for(int j = 0; j < NUMBER_OF_SHEETS[i] - 1; j++){
                writeToSheet(writableSheets, 1, 0, j);
                writeToSheet(writableSheets, 1, 1, j);
            }
        }

        for(int i = 0; i < NUMBER_OF_SHEETS[NUMBER_OF_SHEETS.length - 1] - 1; i++){
            WritableSheet[] writableSheet = getEditAbleSheets(writableWorkbooks[EXCEL_NAME.length - 1], NUMBER_OF_SHEETS[NUMBER_OF_SHEETS.length - 1] - 1);
            for(int j = 1; j < SHEET_NAME[i].length; j++){
                writeToSheet(writableSheet[i], SHEET_NAME[i][j - 1], 0, j);
            }
        }

        WritableSheet writableSheets = writableWorkbooks[EXCEL_NAME.length - 1].createSheet(SHEET_NAME[EXCEL_NAME.length - 1][NUMBER_OF_SHEETS[EXCEL_NAME.length - 1] - 1], NUMBER_OF_SHEETS[EXCEL_NAME.length - 1] - 1);

        for(int i = 0; i < EXCEL_NAME.length; i++){
            if(!(i + 1 == EXCEL_NAME.length))
                writeToSheet(writableSheets, 1, 0, i);
            WriteAndClose(writableWorkbooks[i], EXCEL_NAME[i]);
        }
    }
}