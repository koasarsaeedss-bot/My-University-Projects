#include<iostream>
#include<string>
using namespace std;

struct Subject {
    string name;
    float marks;
};

struct Student {
    int id;
    string rollNumber;
    string name;
    int numSubjects;
    Subject subjects[10];
};

Student students[100];
int totalStudents = 0;
int nextId = 1;

float getPercentage(Student s) {
    float total = 0;
    for(int i = 0; i < s.numSubjects; i++)
        total += s.subjects[i].marks;
    return total / s.numSubjects;
}

string getGrade(float avg) {
    if(avg >= 90) return "A";
    if(avg >= 80) return "B";
    if(avg >= 70) return "C";
    if(avg >= 60) return "D";
    return "F";
}

void addStudent() {
    Student s;
    s.id = nextId++;

    cout << "\nEnter Roll Number: ";
    cin >> s.rollNumber;

    cout << "Enter Name: ";
    cin >> s.name;

    cout << "Enter Number of Subjects (1-10): ";
    cin >> s.numSubjects;

    for(int i = 0; i < s.numSubjects; i++) {
        cout << "Subject " << i+1 << " Name: ";
        cin >> s.subjects[i].name;
        cout << "Subject " << i+1 << " Marks (0-100): ";
        cin >> s.subjects[i].marks;
    }

    students[totalStudents] = s;
    totalStudents++;

    cout << "\nStudent Added Successfully!" << endl;
    cout << "ID          : " << s.id << endl;
    cout << "Roll Number : " << s.rollNumber << endl;
    cout << "Name        : " << s.name << endl;
}

void viewAllStudents() {
    if(totalStudents == 0) {
        cout << "\nNo students found.\n";
        return;
    }

    cout << "\n--- All Students ---\n";
    cout << "ID\tRoll No\t\tName\t\tAverage\t\tGrade\tStatus\n";
    cout << "--------------------------------------------------------------\n";

    for(int i = 0; i < totalStudents; i++) {
        float avg = getPercentage(students[i]);
        cout << students[i].id << "\t"
             << students[i].rollNumber << "\t\t"
             << students[i].name << "\t\t"
             << avg << "%\t\t"
             << getGrade(avg) << "\t"
             << (avg >= 50 ? "PASS" : "FAIL") << endl;
    }
}

void searchStudent() {
    int choice;
    cout << "\nSearch by:\n";
    cout << "1. ID\n";
    cout << "2. Roll Number\n";
    cout << "Enter choice: ";
    cin >> choice;

    int found = -1;

    if(choice == 1) {
        int id;
        cout << "Enter Student ID: ";
        cin >> id;
        for(int i = 0; i < totalStudents; i++) {
            if(students[i].id == id) { found = i; break; }
        }
    } else {
        string roll;
        cout << "Enter Roll Number: ";
        cin >> roll;
        for(int i = 0; i < totalStudents; i++) {
            if(students[i].rollNumber == roll) { found = i; break; }
        }
    }

    if(found == -1) {
        cout << "Student not found.\n";
        return;
    }

    Student s = students[found];
    float avg = getPercentage(s);

    cout << "\n--- Student Report Card ---\n";
    cout << "ID          : " << s.id << endl;
    cout << "Roll Number : " << s.rollNumber << endl;
    cout << "Name        : " << s.name << endl;
    cout << "\nSubject\t\tMarks\tGrade\n";
    cout << "--------------------------------\n";
    for(int j = 0; j < s.numSubjects; j++) {
        cout << s.subjects[j].name << "\t\t"
             << s.subjects[j].marks << "\t"
             << getGrade(s.subjects[j].marks) << endl;
    }
    cout << "--------------------------------\n";
    cout << "Average : " << avg << "%" << endl;
    cout << "Grade   : " << getGrade(avg) << endl;
    cout << "Status  : " << (avg >= 50 ? "PASS" : "FAIL") << endl;
}

void deleteStudent() {
    int choice;
    cout << "\nDelete by:\n";
    cout << "1. ID\n";
    cout << "2. Roll Number\n";
    cout << "Enter choice: ";
    cin >> choice;

    int found = -1;

    if(choice == 1) {
        int id;
        cout << "Enter Student ID: ";
        cin >> id;
        for(int i = 0; i < totalStudents; i++) {
            if(students[i].id == id) { found = i; break; }
        }
    } else {
        string roll;
        cout << "Enter Roll Number: ";
        cin >> roll;
        for(int i = 0; i < totalStudents; i++) {
            if(students[i].rollNumber == roll) { found = i; break; }
        }
    }

    if(found == -1) {
        cout << "Student not found.\n";
        return;
    }

    cout << "Student " << students[found].name
         << " (Roll: " << students[found].rollNumber << ") deleted.\n";

    for(int i = found; i < totalStudents - 1; i++)
        students[i] = students[i+1];
    totalStudents--;
}

void classStats() {
    if(totalStudents == 0) {
        cout << "\nNo students found.\n";
        return;
    }

    float total = 0, highest = 0, lowest = 100;
    int pass = 0, fail = 0;
    string topName, topRoll;

    for(int i = 0; i < totalStudents; i++) {
        float avg = getPercentage(students[i]);
        total += avg;
        if(avg > highest) {
            highest = avg;
            topName = students[i].name;
            topRoll = students[i].rollNumber;
        }
        if(avg < lowest) lowest = avg;
        if(avg >= 50) pass++; else fail++;
    }

    cout << "\n--- Class Statistics ---\n";
    cout << "Total Students : " << totalStudents << endl;
    cout << "Class Average  : " << total / totalStudents << "%" << endl;
    cout << "Highest Score  : " << highest << "% (" << topName << " | Roll: " << topRoll << ")" << endl;
    cout << "Lowest Score   : " << lowest << "%" << endl;
    cout << "Passing        : " << pass << endl;
    cout << "Failing        : " << fail << endl;
}

int main() {
    int choice;

    do {
        cout << "\n============================\n";
        cout << "  STUDENT GRADE MANAGEMENT\n";
        cout << "============================\n";
        cout << "1. Add Student\n";
        cout << "2. View All Students\n";
        cout << "3. Search Student\n";
        cout << "4. Delete Student\n";
        cout << "5. Class Statistics\n";
        cout << "0. Exit\n";
        cout << "Enter choice: ";
        cin >> choice;

        switch(choice) {
            case 1: addStudent();      break;
            case 2: viewAllStudents(); break;
            case 3: searchStudent();   break;
            case 4: deleteStudent();   break;
            case 5: classStats();      break;
            case 0: cout << "Goodbye!\n"; break;
            default: cout << "Invalid choice.\n";
        }

    } while(choice != 0);

    return 0;
}
