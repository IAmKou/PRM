Create database jlia;
use jlia;

CREATE TABLE Role ( 
role_id int primary key ,
role_name varchar(50)
);

CREATE TABLE User (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id int,
    foreign key (role_id) references Role(role_id)
);

-- Create Type table
CREATE TABLE Type (
    type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_name VARCHAR(255) NOT NULL
);

-- Create Quiz table
CREATE TABLE Quiz (
    quiz_id INT PRIMARY KEY AUTO_INCREMENT,
    quiz_name VARCHAR(255) NOT NULL,
    type_id INT,
    creator_id INT,
    FOREIGN KEY (type_id) REFERENCES Type(type_id),
    FOREIGN KEY (creator_id) REFERENCES User(user_id)
);

-- Create report_type tabel
CREATE TABLE Report_type ( 
type_id int primary key,
text varchar(50)
);

-- Create Report table
CREATE TABLE Report (
    report_id INT PRIMARY KEY AUTO_INCREMENT,
    time DATETIME NOT NULL,
    reporter INT,
    quiz_id INT,
    report_text INT,
    FOREIGN KEY (reporter) REFERENCES User(user_id),
    FOREIGN KEY (quiz_id) REFERENCES Quiz(quiz_id),
    foreign key (report_text) references Report_type(type_id)
);

-- Create Class table
CREATE TABLE Class (
    class_id INT PRIMARY KEY AUTO_INCREMENT,
    instructor_id INT,
    class_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (instructor_id) REFERENCES User(user_id)
);

-- Create Question table
CREATE TABLE Question (
    question_id INT PRIMARY KEY AUTO_INCREMENT,
    question_text TEXT NOT NULL,
    quiz_id INT,
    FOREIGN KEY (quiz_id) REFERENCES Quiz(quiz_id)
);

-- Create Answer table
CREATE TABLE Answer (
    answer_id INT PRIMARY KEY AUTO_INCREMENT,
    answer_text TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL,
    question_id INT,
    FOREIGN KEY (question_id) REFERENCES Question(question_id)
);

-- Create Results table
CREATE TABLE Results (
    result_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    quiz_id INT,
    exam_id INT,
    score DECIMAL(5, 2) NOT NULL,
    result_date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (quiz_id) REFERENCES Quiz(quiz_id),
    FOREIGN KEY (exam_id) REFERENCES Exam(exam_id)
);

-- Create Exam table
CREATE TABLE Exam (
    exam_id INT PRIMARY KEY AUTO_INCREMENT,
    quiz_id INT,
    type VARCHAR(50) NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES Quiz(quiz_id)
);
