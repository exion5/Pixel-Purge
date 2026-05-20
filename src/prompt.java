import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class prompt {
    public static File getInputFile() { // checks if the file exists and is readable, if not it will ask the user to try again until it is valid
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

    public static Scanner getInputScanner() { // opens a scanner
        File inputFile = getInputFile();
        try {
            return new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static PrintWriter getPrintWriter() { // opens a print writer to write to the file
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