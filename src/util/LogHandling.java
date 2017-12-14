package util;


import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.*;

/**
 * Created by Tim on 20.09.2017.
 */
public class LogHandling {
    static final String fileName = "logger.txt";
    static int indexOfNewElement = 2;
    static BufferedWriter writer = null;
    static LocalDateTime date;
    static String printMessage;

    public static void logOnFile(Level level, String message) {

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String methodName = stackTraceElements[indexOfNewElement].getMethodName();
        String className = stackTraceElements[indexOfNewElement].getClassName();

        tryWriteOnFile(level, message, methodName, className, getDate());
    }

    private static void tryWriteOnFile(Level level, String message, String methodName, String className, String date) {

        try {
            if (writer == null) {
                writer = new BufferedWriter(new FileWriter(fileName));
            }
            printMessage= date + " " + className + " method: " + methodName + "\n" + level.toString() + ": " + message + "\n";
            writer.write(printMessage);
            System.out.println(printMessage);

        } catch (IOException e) {
        }
    }

    public static String getDate() {

        date = LocalDateTime.now();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String text = date.format(fmt);

        return text; //2016/11/16 12:08:43
    }


    public static void closeResources() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
