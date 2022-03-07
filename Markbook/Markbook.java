package Markbook;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class Markbook {
    private ArrayList<Student> students;
    private double assignmentWeighting;
    private double testWeighting;
    private double finalProjectWeighting;
    private byte option;
    private Scanner sys;

    public Markbook(double assignmentWeighting, double testWeighting, double finalProjectWeighting) throws IOException {
        students = new ArrayList<Student>();
        this.assignmentWeighting = assignmentWeighting;
        this.testWeighting = testWeighting;
        this.finalProjectWeighting = finalProjectWeighting;
        option = 0;
        sys = new Scanner(System.in);

        Scanner sc = new Scanner(new File("Markbook/studentData.txt"));

        while (sc.hasNext()) {
            Student student = new Student();

            student.name = sc.nextLine();
            student.number = Integer.parseInt(sc.nextLine());
            student.assignmentMark = Double.parseDouble(sc.nextLine());
            student.testMark = Double.parseDouble(sc.nextLine());
            student.finalProjectMark = Double.parseDouble(sc.nextLine());

            student.determineAverage(assignmentWeighting, testWeighting, finalProjectWeighting);

            students.add(student);
        }
        sc.close();
    }

    // ----- UTITILTY METHODS ------
    private void title() {
        System.out.println("\033[H\033[2J");
        System.out.println("----- MARKBOOK -----\n");
    }

    public byte getOption() {
        return option;
    }

    private boolean studentAlreadyEntered(int num) {
        for (Student student : students) {
            if (num == student.number) {
                return true;
            }
        }
        return false;
    }

    private Student selectStudent(String instructions) {
        String errorMessage;
        Student selectedStudent = null;
        do {
            errorMessage = "";
            title();
            for (Student student : students) {
                System.out.println("Name: " + student.name);
                System.out.println("Student Number: " + student.number + "\n");
            }
            System.out.print(instructions);
            String numString = sys.nextLine();
            int num;

            if (!numString.matches("\\d{9}")) {
                errorMessage = "The student number you entered is not valid";
            } else {
                num = Integer.parseInt(numString);

                boolean numExists = false;
                for (Student student : students) {
                    if (student.number == num) {
                        numExists = true;
                        selectedStudent = student;
                    }
                }

                if (!numExists) {
                    errorMessage = "The student number you entered does not exist";
                }
            }

            if (!errorMessage.equals("")) {
                System.out.println(errorMessage);
                System.out.print("Enter anything to try again: ");
                sys.nextLine();
            }

        } while (!errorMessage.equals(""));
        return selectedStudent;
    }

    // ----- "SCREEN" METHODS ------

    public void mainMenu() {
        String errorMessage;
        do {
            errorMessage = "";
            title();
            System.out.println("Here are the following actions you can perform:\n");
            System.out.println("1. Create a new student");
            System.out.println("2. View all student data");
            System.out.println("3. View one student's data");
            System.out.println("4. Edit a student's data");
            System.out.println("5. Exit");
            System.out.print("\nEnter your option (1-5): ");

            try {
                option = Byte.parseByte(sys.nextLine());
                if (option < 1 || option > 5) {
                    option = 0;
                    errorMessage = "\nPlease enter a whole numer between 1 and 5, inclusive";
                }
            } catch (Exception e) {
                errorMessage = "\nPlease enter a whole numer between 1 and 5, inclusive";
            }

            if (!errorMessage.equals("")) {
                System.out.println(errorMessage);
                System.out.print("Enter anything to continue: ");
                sys.nextLine();
            }
        } while (!errorMessage.equals(""));
    }

    public void addStudent() {
        String[] data = { "", "", "", "", "" };
        String[] questions = { "What is the name of your student?: ", "What is their student number?: ",
                "What are their assignment mark?: ",
                "What are their test mark?: ",
                "What is their final project mark?: "
        };
        int i = 0;
        String errorMessage;

        do {
            errorMessage = "";

            title();
            System.out.println("Student Name: " + data[0]);
            System.out.println("Student Number: " + data[1]);
            System.out.println("Assigment Mark: " + data[2]);
            System.out.println("Test Mark: " + data[3]);
            System.out.println("Final Project Mark: " + data[4]);

            System.out.print("\n" + questions[i]);
            data[i] = sys.nextLine();

            if (i == 0) {
                if (!data[0].matches("^\\s*([a-zA-Z]+(\\-[a-zA-Z]+)*\\s+)+([a-zA-Z]+(\\-[a-zA-Z]+)*)*\\s*$")) {
                    errorMessage = "The name of your student must be a valid name.";
                }
            } else if (i == 1) {
                if (!data[1].matches("\\d{9}")) {
                    errorMessage = "Your student's number must be a 9 digit positive number";
                } else if (studentAlreadyEntered(Integer.parseInt(data[1]))) {
                    errorMessage = "You cannot have students with the same student number";
                }
            } else {
                try {
                    data[i] = data[i].trim();
                    double num = Double.parseDouble(data[i]);
                    if (((int) (num * 100)) % 10 != 0 || num < 0 || num > 100) {
                        errorMessage = "Only include marks in valid percentage values (0 to 100, and up to one decimal place)";

                    }
                } catch (Exception e) {
                    errorMessage = "You must enter a valid list of marks";
                }
            }

            if (!errorMessage.equals("")) {
                data[i] = "";
                System.out.println(errorMessage);
                System.out.print("Enter anything to continue: ");
                sys.nextLine();
            } else {
                i++;
            }
        } while (!errorMessage.equals("") || i < 5);

        Student student = new Student();
        student.name = data[0];
        student.number = Integer.parseInt(data[1]);
        student.assignmentMark = Double.parseDouble(data[2]);
        student.testMark = Double.parseDouble(data[3]);
        student.finalProjectMark = Double.parseDouble(data[4]);

        student.determineAverage(assignmentWeighting, testWeighting, finalProjectWeighting);

        students.add(student);

        title();
        System.out.println("Student Name: " + data[0]);
        System.out.println("Student Number: " + data[1]);
        System.out.println("Assigment Mark: " + data[2]);
        System.out.println("Test Mark: " + data[3]);
        System.out.println("Final Project Mark: " + data[4]);

        System.out.println("\nData added successfully!");
        System.out.print("Enter anything to continue: ");
        sys.nextLine();

        option = 0;
    }

    public void viewAll() {
        title();
        double sum = 0;
        int num = 0;
        System.out.println("Student Data:\n");
        for (Student student : students) {
            System.out.println(student + "\n");
            num++;
            sum += student.average;
        }
        if (num != 0) {
            System.out.print("The class average is: " + (((int) (sum / num * 10 + 0.5)) / 10.0) + "%");
        } else {
            System.out.print("The class average is: 0");

        }
        System.out.print("\nEnter anything to go back to main menu: ");
        sys.nextLine();
        option = 0;
    }

    public void viewOne() {
        Student student = selectStudent("Enter the number of the student that you would like to view: ");
        title();
        System.out.println(student);
        System.out.print("\nEnter anything to go back to main menu: ");
        sys.nextLine();
        option = 0;
    }

    public void editData() {
        Student student = selectStudent("Enter the number of the student whose data needs editing: ");
        String[] markTypes = { "Assignment Mark", "Test Mark", "Final Project Mark" };
        String errorMessage;
        byte markChoice;
        do {
            markChoice = 0;
            errorMessage = "";
            title();
            System.out.println(student);
            System.out.println("\nHere are the types of marks you can change");
            System.out.println("1. " + markTypes[0]);
            System.out.println("2. " + markTypes[1]);
            System.out.println("3. " + markTypes[2]);
            System.out.print("Enter your choice: ");

            try {
                markChoice = Byte.parseByte(sys.nextLine());
                if (markChoice < 1 || markChoice > 3) {
                    errorMessage = "Please enter a valid option";
                }
            } catch (Exception e) {
                errorMessage = "Please enter a valid option";
            }

            if (!errorMessage.equals("")) {
                System.out.println(errorMessage);
                System.out.print("Enter anything to continue: ");
                sys.nextLine();
            }
        } while (!errorMessage.equals(""));

        markChoice--;

        String mark;
        double num = 0;
        do {
            mark = "";
            title();
            System.out.println(student);
            System.out.print("\nEnter the new mark for " + student.name + "\'s " + markTypes[markChoice] + ": ");
            mark = sys.nextLine();

            try {
                mark = mark.trim();
                num = Double.parseDouble(mark);
                if (Math.floor(num * 100) % 10 != 0 || num < 0 || num > 100) {
                    errorMessage = "Only include marks in valid percentage values (0 to 100, and up to one decimal place)";

                }
            } catch (Exception e) {
                errorMessage = "You must enter valid a mark";
            }

            if (!errorMessage.equals("")) {
                System.out.println(errorMessage);
                System.out.print("Enter anything to try again: ");
                sys.nextLine();
            }

        } while (!errorMessage.equals(""));

        if (markChoice == 0) {
            student.assignmentMark = num;
        } else if (markChoice == 1) {
            student.testMark = num;
        } else {
            student.finalProjectMark = num;
        }

        student.determineAverage(assignmentWeighting, testWeighting, finalProjectWeighting);

        System.out.println("\nMark changed successfully!");
        System.out.print("Enter anything to go back to main menu: ");
        sys.nextLine();
        option = 0;
    }

    public void exit() throws IOException {
        String choice;
        do {
            choice = "";
            title();
            System.out.println("Thank you for using markbook");
            System.out.print("Would you like to save your changes? (Y/N): ");
            choice = sys.nextLine();

            if (!(choice.equalsIgnoreCase("n") || choice.equalsIgnoreCase("y"))) {
                choice = "";
                System.out.println("Please enter Y or N");
                System.out.print("Enter anything to try again: ");
                sys.nextLine();
            }
        } while (choice.equals(""));

        if (choice.equalsIgnoreCase("Y")) {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Markbook/studentData.txt"));
            for (Student student : students) {
                writer.write(student.name);
                writer.write("\n");
                writer.write(student.number + "");
                writer.write("\n");
                writer.write(student.assignmentMark + "");
                writer.write("\n");
                writer.write(student.testMark + "");
                writer.write("\n");
                writer.write(student.finalProjectMark + "");
                writer.write("\n");
            }
            writer.close();
        }
    }

    public static void main(String[] args) throws IOException {
        Markbook mb = new Markbook(40, 30, 30);

        do {
            byte option = mb.getOption();
            if (option == 0) {
                mb.mainMenu();
            } else if (option == 1) {
                mb.addStudent();
            } else if (option == 2) {
                mb.viewAll();
            } else if (option == 3) {
                mb.viewOne();
            } else if (option == 4) {
                mb.editData();
            }
        } while (mb.getOption() < 5);
        mb.exit();
    }

}
