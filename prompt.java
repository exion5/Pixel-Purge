import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class prompt {
    private static Scanner consoleScanner = new Scanner(System.in);

    // a. Ensures file exists and is readable
    public static File getInputFile() {
        File file;
        boolean isValid = false;
        do {
            String filename = "Registration.txt";
            file = new File(filename);
            if (file.exists() && file.canRead()) {
                isValid = true;
            } else {
                System.out.println("File not found or not readable. Try again.");
            }
        } while (!isValid);
        return file;
    }

    // b. Uses getInputFile to open a Scanner
    public static Scanner getInputScanner() {
        File inputFile = getInputFile();
        try {
            return new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            // Should not happen since getInputFile validates existence
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static PrintWriter getPrintWriter() {
        String filename = "Registration.txt";
        try {
            FileWriter fw = new FileWriter(filename, true);
            return new PrintWriter(fw);
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
            return null;
        }
    }
}