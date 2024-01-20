package com.Admin;

import java.io.*;

class ID implements Serializable {
    String teacherId;
    String studentId;

    public ID(String teacherId, String studentId) {
        this.teacherId = teacherId;
        this.studentId = studentId;
    }
}

public class SavingAndGettingId{
    private final static String filename = System.getProperty("user.dir") + "\\src\\com\\Admin\\ids.txt";
    ID id;
    SavingAndGettingId(){
        File file =  new File(filename);
        try{
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            id = (ID) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException ioe){
            System.out.println("Something went wrong");
        } catch (ClassNotFoundException cnfe){
            System.out.println("Something else went wrong");
        }
    }

    String getStudentID(){
        String studentId = id.studentId;
        id.studentId = (Integer.parseInt(id.studentId) + 1) + "";
        return "S" + studentId;
    }

    String getTeacherID(){
        String teacherID = id.teacherId;
        id.teacherId = (Integer.parseInt(id.teacherId) + 1) + "";
        return "T" + teacherID;
    }

    void save(){
        File file =  new File(filename);
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(id);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException ioe){
            System.out.println("Something went wrong");
        }
    }

    public static void createNew(){
        File file =  new File(filename);
        ID id = new ID("1000", "1000");
        try{
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(id);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException ioe){
            System.out.println("Something went wrong");
        }
    }
}