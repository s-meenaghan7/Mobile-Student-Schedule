# Mobile-Student-Schedule
Android app written in Java for university students to store information about their schedule.

# About
This basic scheduling tool was implemented for use by university students to store information regarding their schedules.
The application utilizes MVVM architecture. Data is stored locally in a SQLite database implemented using the Room framework (https://developer.android.com/reference/androidx/room/package-summary).

Students can input information regarding any given term (i.e. a semester) including the start and end dates of the term and associated courses.
Courses have information such as start and end dates, associated assessments, a given status, designated course mentor, and the ability to add notes to each course.
