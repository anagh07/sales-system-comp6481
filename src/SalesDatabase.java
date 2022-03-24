import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class SalesDatabase {
    static ArrayList<Sales> salesArr = new ArrayList<>();
    private static int userChoice = 0;
    static Scanner inputScanner = new Scanner(System.in);
    static String workingDir = System.getProperty("user.dir");

    public static void main(String[] args) {
        while (userChoice != 3) {
            userInteract();
            switch (userChoice) {
                case 1:
                    File currDir = new File(new File(workingDir, "src"), "Data");
                    writeDirLog(currDir);
                    break;

                case 2:
                    try {
                        processFiles();
                        writeToOutput();
                    } catch (InvalidFileException err) {
                        System.out.println(err.getMessage());
                    }
                    break;

                case 3:
                    break;

                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

    private static void writeToOutput() {
        if (salesArr.size() > 0) {
            try (FileWriter fw = new FileWriter("output.txt")) {
                salesArr.forEach(sales -> {
                    try {
                        fw.write(sales.toString() + '\n');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void processFiles() throws InvalidFileException {
        File logFile = new File(workingDir, "log.txt");
        if (!logFile.exists() || !logFile.isFile()) {
            throw new InvalidFileException("Error: log file does not exist.");
        }
        try (BufferedReader logReader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = logReader.readLine()) != null) {
                line = line.replaceAll("directory:", "");
                line = line.replaceAll("file:", "");
                File tempPath = new File(line.trim());
                if (!tempPath.exists()) throw new InvalidFileException("Error: Input file named " + line + "cannot be" +
                        " found");
                if (tempPath.isFile()) {
                    System.out.println(tempPath.getAbsolutePath());
                    BufferedReader dataReader = new BufferedReader(new FileReader(tempPath));
                    displayFileContents(dataReader);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds new record to sales list.
     *
     * @param sale New record to be added.
     */
    private static void addRecords(Sales sale) {
        salesArr.add(sale);
    }

    /**
     * Use sequential search to find the sales record from database.
     *
     * @param order_ID Record id to be searched.
     * @return returns the sales object if found, else returns null.
     */
    private static Sales sequentialSearch(long order_ID) {
        for (int i = 0; i < salesArr.size(); i++) {
            if (salesArr.get(i).equals(order_ID)) {
                System.out.println("Steps to find order_id " + order_ID + " was " + (i + 1));
                return salesArr.get(i);
            }
        }
        return null;
    }

    private static Sales binarySalesSearch(long order_ID) {
        // sort the salesarray
        salesArr.sort(new Comparator<Sales>() {
            @Override
            public int compare(Sales o1, Sales o2) {
                return (int) (o1.getOrder_ID() - o2.getOrder_ID());
            }
        });
        // implement binary search

        return null;
    }

    /**
     * Method prints contents of the file input stream passed to it to console.
     *
     * @param bfr Input stream, buffered reader.
     * @throws IOException
     */
    private static void displayFileContents(BufferedReader bfr) throws IOException {
        String line;
        while ((line = bfr.readLine()) != null) {
            System.out.println(line);
            String[] fields = line.split("\t+");
            Sales newSale = sequentialSearch(Long.parseLong(fields[4]));
            if (newSale == null) {
                newSale = new Sales(fields);
                addRecords(newSale);
            } else {
                System.out.println("# Duplicate record: " + newSale.toString());
//                throw new DuplicateRecordException("Error: duplicate record -> " + newSale.toString());
            }
        }
    }

    /**
     * Creates writer object and calls method to list directories
     *
     * @param dir Path to the directory on which all subdirectories will be generated
     */
    private static void writeDirLog(File dir) {
        // Try with resources to close the writer object after use
        try (FileWriter log = new FileWriter("log.txt")) {
            listDirs(dir, log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recursively lists all sub-directories and files and writes them to the passed writer object.
     *
     * @param dir Path to the directory
     * @param log Writer object that will be used to write list to file.
     * @throws IOException Thrown if error occurs while writing to file.
     */
    private static void listDirs(File dir, FileWriter log) throws IOException {
        File[] subFiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });
        for (File subFile : subFiles) {
            log.write("\tfile:" + subFile.getAbsolutePath() + "\n");
//            System.out.println("\tfile:" + subFile.getAbsolutePath());
        }
        File[] subFolders = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        for (File subFolder : subFolders) {
            log.write("directory:" + subFolder.getAbsolutePath() + "\n");
//            System.out.println("directory:" + subFolder.getAbsolutePath());
            listDirs(subFolder, log);
        }
    }

    /**
     * Print options and take input from user.
     */
    private static void userInteract() {
        System.out.println(
                "###########\n" +
                        "<1> List files\n" +
                        "<2> Process files\n" +
                        "<3> Exit"
        );
        System.out.print("# Enter you choice:\t>");
        userChoice = inputScanner.nextInt();
    }
}

/**
 * Exception thrown when the log file or the file paths in the log file are not valid.
 */
class InvalidFileException extends Exception {

    public InvalidFileException(String message) {
        super(message);
    }

    public InvalidFileException() {
        this("Error: Input file named XXX cannot be found");
    }

}

/**
 * Exception thrown when a directory is empty (has no file/subdirectory).
 */
class EmptyFolderException extends Exception {

    public EmptyFolderException(String message) {
        super(message);
    }

    public EmptyFolderException() {
        this("Error: Folder is empty.");
    }
}