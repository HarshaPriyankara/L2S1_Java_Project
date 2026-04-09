-- Drop previous database
DROP DATABASE IF EXISTS Faculty_Of_Technology;

-- Create database and use it
CREATE DATABASE IF NOT EXISTS Faculty_Of_Technology;
USE Faculty_Of_Technology;

-- ─────────────────────────────────────────────
--  CORE TABLES
-- ─────────────────────────────────────────────

CREATE TABLE user (
                      User_id       CHAR(6) PRIMARY KEY,
                      Profile_pic   VARCHAR(500),
                      F_name        VARCHAR(50)  NOT NULL,
                      L_name        VARCHAR(50)  NOT NULL,
                      date_of_birth DATE,
                      Address       VARCHAR(100),
                      Email         VARCHAR(100) UNIQUE,
                      Role          VARCHAR(50),
                      Password      VARCHAR(100)
);

CREATE TABLE department (
                            Department_id CHAR(5)     PRIMARY KEY,
                            Dep_name      VARCHAR(50)
);

CREATE TABLE user_contact_number (
                                     User_id  CHAR(6),
                                     Phone_no VARCHAR(50),
                                     FOREIGN KEY (User_id) REFERENCES user(User_id) ON DELETE CASCADE
);

CREATE TABLE admin (
                       Admin_id CHAR(6) PRIMARY KEY,
                       FOREIGN KEY (Admin_id) REFERENCES user(User_id) ON DELETE CASCADE
);

CREATE TABLE dean (
                      Dean_id CHAR(6) PRIMARY KEY,
                      FOREIGN KEY (Dean_id) REFERENCES user(User_id) ON DELETE CASCADE
);

CREATE TABLE technical_officer (
                                   To_id CHAR(6) PRIMARY KEY,
                                   FOREIGN KEY (To_id) REFERENCES user(User_id) ON DELETE CASCADE
);

CREATE TABLE lecturer (
                          Lecturer_id    CHAR(6) PRIMARY KEY,
                          Dep_id         CHAR(5),
                          Specialization VARCHAR(50),
                          FOREIGN KEY (Dep_id)      REFERENCES department(Department_id),
                          FOREIGN KEY (Lecturer_id) REFERENCES user(User_id) ON DELETE CASCADE
);

CREATE TABLE student (
                         Reg_no       CHAR(6) PRIMARY KEY,
                         Batch        VARCHAR(20),
                         Student_type VARCHAR(50),
                         Dep_id       CHAR(5),
                         FOREIGN KEY (Dep_id)  REFERENCES department(Department_id),
                         FOREIGN KEY (Reg_no) REFERENCES user(User_id) ON DELETE CASCADE
);

-- ─────────────────────────────────────────────
--  FIX 1: Removed Timetable_id from course
--          (circular dependency resolved)
-- ─────────────────────────────────────────────

CREATE TABLE course (
                        Course_code        VARCHAR(20) PRIMARY KEY,
                        Course_name        VARCHAR(100),
                        Type               VARCHAR(50),
                        Credits            INT,
                        Lecturer_in_charge CHAR(6),
                        Dep_id             CHAR(5),
                        FOREIGN KEY (Dep_id)             REFERENCES department(Department_id),
                        FOREIGN KEY (Lecturer_in_charge) REFERENCES lecturer(Lecturer_id)
);

-- ─────────────────────────────────────────────
--  FIX 2: Timetable no longer needs course
--          to exist first (no circular issue)
-- ─────────────────────────────────────────────

CREATE TABLE timetable (
                           Timetable_id  CHAR(5)     PRIMARY KEY,
                           Start_time    TIME,
                           End_time      TIME,
                           Day           VARCHAR(20),
                           Session_type  VARCHAR(10) CHECK (Session_type IN ('Theory', 'Practical')),
                           Venue         VARCHAR(100),
                           Department_id CHAR(5),
                           Course_code   VARCHAR(20),
                           FOREIGN KEY (Department_id) REFERENCES department(Department_id),
                           FOREIGN KEY (Course_code)   REFERENCES course(Course_code) ON DELETE CASCADE
);

CREATE TABLE MARK (
                      Mark_id     INT AUTO_INCREMENT PRIMARY KEY,
                      Reg_no      CHAR(6),
                      Course_code VARCHAR(20),
                      Marks_type  VARCHAR(50) CHECK (Marks_type IN (
                                                                    'Quiz_1', 'Quiz_2', 'Quiz_3',
                                                                    'Assignment_1', 'Assignment_2',
                                                                    'Mini_project',
                                                                    'Mid_practical', 'Mid_theory',
                                                                    'End_practical', 'End_theory')),
                      Marks_value DECIMAL(5,2) CHECK (Marks_value BETWEEN 0 AND 100),
                      FOREIGN KEY (Reg_no)      REFERENCES student(Reg_no)  ON DELETE CASCADE,
                      FOREIGN KEY (Course_code) REFERENCES course(Course_code) ON DELETE CASCADE
);
CREATE TABLE medical_record (
                                Medical_id   INT AUTO_INCREMENT PRIMARY KEY,
                                Reg_no       CHAR(6) NOT NULL,
                                Session_date DATE NOT NULL,
                                Reason       VARCHAR(255),

                                Session_type VARCHAR(50) DEFAULT 'NormalDay'
                                    CHECK (Session_type IN ('NormalDay','Exam')),

                                Exam_course  VARCHAR(20) DEFAULT NULL,
                                Approved     BOOLEAN DEFAULT FALSE,

                                FOREIGN KEY (Reg_no)
                                    REFERENCES student(Reg_no) ON DELETE CASCADE,

                                FOREIGN KEY (Exam_course)
                                    REFERENCES course(Course_code) ON DELETE SET NULL
);

CREATE TABLE attendance (
                            Attendance_id INT PRIMARY KEY AUTO_INCREMENT,
                            Reg_no        CHAR(6),
                            Course_code   VARCHAR(20),
                            Session_date  DATE,
                            Session_type  VARCHAR(10) CHECK (Session_type IN ('Theory', 'Practical')),
                            Session_hours DECIMAL(2,1) NOT NULL,
                            Status        VARCHAR(10)  CHECK (Status IN ('Present', 'Absent', 'Medical')),
                            Medical_id    INT,
                            FOREIGN KEY (Medical_id)  REFERENCES medical_record(Medical_id) ON DELETE SET NULL,
                            FOREIGN KEY (Reg_no)      REFERENCES student(Reg_no)  ON DELETE CASCADE,
                            FOREIGN KEY (Course_code) REFERENCES course(Course_code) ON DELETE CASCADE
);

CREATE TABLE course_material (
                                 Material_id CHAR(5)      PRIMARY KEY,
                                 Type        VARCHAR(50),
                                 Uploaded_at DATE,
                                 File_URL    VARCHAR(500),
                                 Uploaded_by CHAR(6),
                                 Title       VARCHAR(100),
                                 Course_code VARCHAR(20),
                                 FOREIGN KEY (Uploaded_by) REFERENCES lecturer(Lecturer_id) ON DELETE SET NULL,
                                 FOREIGN KEY (Course_code) REFERENCES course(Course_code)   ON DELETE CASCADE
);

-- ─────────────────────────────────────────────
--  FIX 4: Created_by now references user(User_id)
--          instead of storing a plain name string
-- ─────────────────────────────────────────────

CREATE TABLE notice (
                        Notice_id     CHAR(5)     PRIMARY KEY,
                        Target_role   VARCHAR(50),
                        Created_date  DATE,
                        Created_by    CHAR(6),                        -- FK to user instead of VARCHAR name
                        Title         VARCHAR(100),
                        Content       VARCHAR(500),
                        Department_id CHAR(5),
                        Lecturer_id   CHAR(6),
                        FOREIGN KEY (Created_by)    REFERENCES user(User_id)         ON DELETE SET NULL,
                        FOREIGN KEY (Lecturer_id)   REFERENCES lecturer(Lecturer_id) ON DELETE SET NULL,
                        FOREIGN KEY (Department_id) REFERENCES department(Department_id) ON DELETE CASCADE
);

CREATE TABLE enrollment (
                            Reg_no      CHAR(6),
                            Course_code VARCHAR(20),
                            PRIMARY KEY (Reg_no, Course_code),
                            FOREIGN KEY (Reg_no)      REFERENCES student(Reg_no)   ON DELETE CASCADE,
                            FOREIGN KEY (Course_code) REFERENCES course(Course_code) ON DELETE CASCADE
);
