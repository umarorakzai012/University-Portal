package com.Excel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.io.IOException;



public class Transcript extends Excel{
    final private static String EXCEL_NAME = System.getProperty("user.dir") + "\\ExcelFiles\\Transcript\\Transcript.xls";
    final private static String[] COLUMN_NAME = {
        "ID", "Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8"
    };
    final private static int TOTAL_COLUMNS = COLUMN_NAME.length;
    final private static int[][] COURSE_CREDIT_HOURS = {
            {3, 1, 1, 2, 1, 3, 3, 3},
            {3, 1, 3, 1, 3, 3, 2, 1},
            {3, 1, 3, 1, 3, 3, 3},
            {3, 1, 3, 3, 1, 3, 1},
            {3, 3, 1, 3, 1, 3, 3},
            {3, 3, 1, 3, 3, 3},
            {3, 3, 1, 3, 3, 3},
            {3, 3, 3, 3, 3}
    };
    final private static String[][] COURSE_NAME = {
            {"PF", "PF Lab", "ICT", "ECC", "ECC Lab", "LA", "Cal", "PST"},
            {"OOP", "OOP Lab", "DLD", "DLD Lab", "DE", "ISL", "CPS", "CPS Lab"},
            {"PA", "PA Lab", "DS", "DS Lab", "Discrete", "PS", "UE-1"},
            {"AI", "AI Lab", "FSE", "Database", "Database Lab", "COAL", "COAL Lab"},
            {"KRR", "ML", "ML Lab", "OS", "OS Lab", "DAA", "TBW"},
            {"ANN", "CN", "CN Lab", "PDC", "AIE-1", "AIE-2"},
            {"FNLP", "CV", "CV Lab", "FYP-1", "AIE-3", "UE-2"},
            {"FYP-2", "IS", "PP", "UE-3", "AIE-4"}
    };
    final private static double[] gradePointEquivalent = {4.00, 4.00, 3.66, 3.33, 3.00, 2.66, 2.33, 2.00, 1.66, 1.33, 1.00, 0.00};
    final private static int[] totalCreditHours = {17, 17, 17, 15, 17, 16, 16, 15};

    public static float[] getGPA(String id, String semester){
        int sem = Integer.parseInt(semester);
        float[] GPA = new float[2];

        Workbook workbook = getReadAbleWorkbook(EXCEL_NAME);
        Sheet[] sheets = getReadAbleSheets(workbook, 2);

        int foundIdOnRow = 0;
        int totalRows = sheets[0].getRows();

        for(int i = 1; i < totalRows; i++){
            if(sheets[0].getCell(0, i).getContents().equals(id)){
                foundIdOnRow = i;
                break;
            }
        }

        GPA[0] = Float.parseFloat(sheets[0].getCell(sem, foundIdOnRow).getContents());
        GPA[1] = Float.parseFloat(sheets[1].getCell(sem, foundIdOnRow).getContents());

        Close(workbook, EXCEL_NAME);
        return GPA;
    }
    static void addStudent(String id){
        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME);
        WritableSheet[] writableSheets = getEditAbleSheets(writableWorkbook, 2);

        int totalRows = writableSheets[0].getRows();

        writeToSheet(writableSheets[0], id, totalRows, 0);
        writeToSheet(writableSheets[1], id, totalRows, 0);

        WriteAndClose(writableWorkbook, EXCEL_NAME);
    }
    static void increaseSemesterByOne(String id){
        String semester = Info.getSingleInfo(id, "Semester");
        int sem = Integer.parseInt(semester) - 1;

        WritableWorkbook writableWorkbook = getEditAbleWorkbook(EXCEL_NAME);
        WritableSheet[] writableSheets = getEditAbleSheets(writableWorkbook, 2);

        float SGPA = 0;
        for(int i = 0; i < COURSE_NAME[sem - 1].length; i++){
            float grandTotal = Marks.getPassedCourseMarks(id, COURSE_NAME[sem - 1][i], sem + "");
            int index = gradeAndPointIndex(grandTotal);
            SGPA += (COURSE_CREDIT_HOURS[sem - 1][i] * gradePointEquivalent[index]);
        }
        SGPA /= totalCreditHours[sem - 1];
        int foundIdOnRow = 0;
        int totalRows = writableSheets[0].getRows();
        for(int i = 1; i < totalRows; i++){
            if(writableSheets[0].getCell(0, i).getContents().equals(id)){
                foundIdOnRow = i;
                break;
            }
        }
        writeToSheet(writableSheets[0], SGPA, foundIdOnRow, sem);

        if(sem == 1){
            writeToSheet(writableSheets[1], SGPA, foundIdOnRow, sem);
            WriteAndClose(writableWorkbook, EXCEL_NAME);
            return;
        }

        int totalCrs = totalCreditHours[sem - 1];
        float CGPA = SGPA * totalCrs;
        for(int i = 1; i < sem; i++){
            float previousSemesterSGPA = Float.parseFloat(writableSheets[0].getCell(i, 0).getContents());
            CGPA += (previousSemesterSGPA * totalCreditHours[i - 1]);
            totalCrs += totalCreditHours[i - 1];
        }
        CGPA /= totalCrs;

        writeToSheet(writableSheets[1], CGPA, foundIdOnRow, sem);

        WriteAndClose(writableWorkbook, EXCEL_NAME);
    }
    private static int gradeAndPointIndex(float marks){
        for(int i = 0; i < 10; i++){
            if(marks < (90 - 4 * i) && marks >= (86 - 4 * i))
                return (i + 1);
        }
        if(marks >= 90)
            return 0;
        else
            return 11;
    }
    static void createExcel() throws IOException {
        WritableWorkbook writableWorkbook = Workbook.createWorkbook(new File(EXCEL_NAME));

        WritableSheet writableSheet1 = writableWorkbook.createSheet("SGPA", 0);
        WritableSheet writableSheet2 = writableWorkbook.createSheet("CGPA", 1);

        for(int i = 0; i < TOTAL_COLUMNS; i++){
            writeToSheet(writableSheet1, COLUMN_NAME[i], 0, i);
            writeToSheet(writableSheet2, COLUMN_NAME[i], 0, i);
        }

        WriteAndClose(writableWorkbook, EXCEL_NAME);
    }
}
