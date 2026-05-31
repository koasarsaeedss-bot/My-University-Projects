# Student-Grade-Management-System

🏫 Computer Science | Programming Fundamentals Project
This is my Semester-1 University Project for the **Programming Fundamentals** course in my **Computer Science** degree. It is a console-based Student Grade Management System written in C++ that allows managing student records, tracking subject-wise marks, and automatically calculating grades. The program also generates class-wide statistics, including highest/lowest scores and pass/fail distributions.

✨ Features
✅ Add and manage Students (ID, Name, Roll Number, Subjects)
✅ Dynamic subject entry (up to 10 subjects per student)
✅ Auto-calculate average percentage and grades (A, B, C, D, F)
✅ Search students instantly by ID or Roll Number
✅ Delete student records seamlessly
✅ Generate Class Statistics (Class average, top performer, pass/fail count)

🛠 Technologies Used
* C++ (core language)
* Arrays & Structs for nested data storage (`Student` and `Subject`)
* Console Menu System

📊 Example Output

============================
  STUDENT GRADE MANAGEMENT
============================
1. Add Student
2. View All Students
3. Search Student
4. Delete Student
5. Class Statistics
0. Exit
Enter choice: 1

Enter Roll Number: 25BSCS
Enter Name: KOSAR
Enter Number of Subjects (1-10): 2
Subject 1 Name: Programming_Fundamentals
Subject 1 Marks (0-100): 88
Subject 2 Name: Calculus
Subject 2 Marks (0-100): 74

Student Added Successfully!
ID          : 1
Roll Number : 25BSCS
Name        : KOSAR

============================
  STUDENT GRADE MANAGEMENT
============================
1. Add Student
2. View All Students
3. Search Student
4. Delete Student
5. Class Statistics
0. Exit
Enter choice: 2

--- All Students ---
ID	Roll No		Name		Average		Grade	Status
--------------------------------------------------------------
1	25BSCS	KOSAR		81%		B	PASS

============================
  STUDENT GRADE MANAGEMENT
============================
1. Add Student
2. View All Students
3. Search Student
4. Delete Student
5. Class Statistics
0. Exit
Enter choice: 0
Goodbye!
🎯 Purpose

This project was built in 1st Semester as part of my university learning journey.
It demonstrates understanding of structs, vectors, file handling, and menu-driven programming in C++.
