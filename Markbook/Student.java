package Markbook;

public class Student {
    String name;
    int number;
    double assignmentMark;
    double testMark;
    double finalProjectMark;
    double average;

    public Student() {
        name = "";
        number = 0;
        assignmentMark = 0;
        testMark = 0;
        finalProjectMark = 0;
        average = 0;
    }

    public String toString() {
        return "Name: " + name + "\nStudent Number: " + number + "\nAssignment Mark: " + assignmentMark + "%"
                + "\nTest Mark: " + testMark + "%" + "\nFinal Project Mark: " + finalProjectMark + "%" + "\nAverage: "
                + average + "%";
    }

    public void determineAverage(double assignmentWeighting, double testWeighting, double finalProjectWeighting) {
        double maxSum = assignmentWeighting + testWeighting + finalProjectWeighting;
        double studentSum = (assignmentWeighting * assignmentMark + testWeighting * testMark
                + finalProjectWeighting * finalProjectMark) / 100;

        average = Math.floor(studentSum / maxSum * 1000 + 0.5) / 10.0;
    }
}
