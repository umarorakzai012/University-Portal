package com.Excel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.*;

import java.io.IOException;
import java.util.ArrayList;

public class Course extends Excel{
    final private static String EXCEL_NAME = System.getProperty("user.dir") + "\\ExcelFiles\\Course\\Course.xls";
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

    public static String[] getAllIDsRegisteredForCourse(String courseName){
        int sheetNumber = 0, columnNumber = 0;
        for(int i = 0; i < COLUMN_NAME.length; i++){
            for(int j = 1; j < COLUMN_NAME[i].length; j++){
                if(courseName.equals(COLUMN_NAME[i][j])){
                    sheetNumber = i;
                    columnNumber = j;
                    break;
                }
            }
            if(columnNumber != 0)
                break;
        }
        Workbook workbook = getReadAbleWorkbook(EXCEL_NAME);
        Sheet sheet = workbook.getSheet(sheetNumber);
        ArrayList<String> id = new ArrayList<>();
        int totalRows = sheet.getRows();
        for(int i = 1; i < totalRows; i++){
            if(sheet.getCell(columnNumber, i).getContents().equals("E")){
                id.add(sheet.getCell(0, i).getContents());
            }
        }
        Close(workbook, EXCEL_NAME);
        return id.toArray(new String[id.size()]);
    }

    public static String[][] getSingleStudentCourseStatus(String id){
        Workbook workbook = getReadAbleWorkbook(EXCEL_NAME);
        Sheet sheet = workbook.getSheet("Semester" + Info.getSingleInfo(id, "Semester"));
        int total_rows = sheet.getRows();
        for(int i = 1; i < total_rows; i++){
            if(sheet.getCell(0, i).getContents().equals(id)){
                String[][] courseStatus = new String[sheet.getColumns() - 1][2];
                for(int j = 1; j < courseStatus.length + 1; j++){
                    courseStatus[j - 1][0] = sheet.getCell(j, 0).getContents();
                    courseStatus[j - 1][1] = sheet.getCell(j, i).getContents();
                }
                Close(workbook, EXCEL_NAME);
                return courseStatus;
            }
        }
        Close(workbook, EXCEL_NAME);
        return null;
    }

    public static String courseFailed(String id, String courseName){
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME);

        int sem = Integer.parseInt(Info.getSingleInfo(id, "Semester"));

        WritableSheet writableSheet = writableWorkbook.getSheet(sem - 1);
        int total_rows = writableSheet.getRows();

        for(int i = 1; i < total_rows; i++){
            if(writableSheet.getCell(0, i).getContents().equals(id)){
                for(int j = 1; j < TOTAL_COLUMNS[sem - 1]; j++){
                    if(writableSheet.getCell(j, 0).getContents().equals(courseName)){
                        writeToSheet(writableSheet, "NE", i, j);
                        WriteAndClose(writableWorkbook, EXCEL_NAME);
                        Marks.courseFailed(id, courseName);
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
    public static String coursePassed(String id, String courseName){
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME);

        int sem = Integer.parseInt(Info.getSingleInfo(id, "Semester"));

        WritableSheet writableSheet = writableWorkbook.getSheet(sem - 1);
        int total_rows = writableSheet.getRows();

        for(int i = 1; i < total_rows; i++){
            if(writableSheet.getCell(0, i).getContents().equals(id)){
                for(int j = 1; j < TOTAL_COLUMNS[sem - 1]; j++){
                    if(writableSheet.getCell(j, 0).getContents().equals(courseName)){
                        writeToSheet(writableSheet, "P", i, j);
                        WriteAndClose(writableWorkbook, EXCEL_NAME);
                        Attendance.coursePassed(id, courseName);
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
    public static String Course_Regis(String id, String course_name){
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME);

        int sem = Integer.parseInt(Info.getSingleInfo(id, "Semester"));

        WritableSheet writableSheet = writableWorkbook.getSheet(sem - 1);
        int total_rows = writableSheet.getRows();

        for(int i = 1; i < total_rows; i++){
            if(writableSheet.getCell(0, i).getContents().equals(id)){
                for(int j = 1; j < TOTAL_COLUMNS[sem - 1]; j++){
                    if(COLUMN_NAME[sem - 1][j].equals(course_name)){
                        writeToSheet(writableSheet, "E", i, j);
                        WriteAndClose(writableWorkbook, EXCEL_NAME);
                        Marks.addStudent(id, course_name);
                        Attendance.addStudent(id, course_name);
                        return "Successful";
                    }
                }
                WriteAndClose(writableWorkbook, EXCEL_NAME);
                return "Could not find " + course_name;
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
        WritableSheet writableSheet = writableWorkbook.getSheet("Semester" + sem);
        int total_rows = writableSheet.getRows();

        for(int i = 1; i < total_rows; i++){
            if(id.equals(writableSheet.getCell(0, i).getContents())){
                foundOn = i;
                break;
            }
        }
        writableSheet.removeRow(foundOn);
        WriteAndClose(writableWorkbook, EXCEL_NAME);
        for(int i = 1; i < TOTAL_COLUMNS[sem - 1]; i++){
            Marks.coursePassed(id, COLUMN_NAME[sem - 1][i]);
        }
        addStudent(id);
        return "Successful";
    }
    static void addStudent(String id){
        String semester = Info.getSingleInfo(id, "Semester");
        int sem = Integer.parseInt(semester);

        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME);

        WritableSheet writableSheet = writableWorkbook.getSheet(sem - 1);
        int total_row = writableSheet.getRows();

        writeToSheet(writableSheet, id, total_row, 0);
        for(int i = 1; i < TOTAL_COLUMNS[sem - 1]; i++){
            writeToSheet(writableSheet, "NE", total_row, i);
        }

        WriteAndClose(writableWorkbook, EXCEL_NAME);
    }
    static void createExcel() throws IOException {
        Excel.createExcel(NUMBER_OF_SHEETS, EXCEL_NAME, COLUMN_NAME, TOTAL_COLUMNS);
    }
}
