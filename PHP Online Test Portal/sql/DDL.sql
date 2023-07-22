CREATE SCHEMA `infm114` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;

-- TABLE: USERS 
CREATE TABLE infm114.users (
    username VARCHAR(25) PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);

INSERT INTO infm114.users(username, password) VALUES('rossinka','81dc9bdb52d04dc20036dbd8313ed055');

-- TABLE: TESTS 
CREATE TABLE infm114.tests (
    test_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    duration INT NOT NULL,
    active TINYINT(1),
    user VARCHAR(25) NOT NULL
);

ALTER TABLE infm114.tests
        ADD CONSTRAINT fk_test_user
        FOREIGN KEY (user)
        REFERENCES infm114.users(username)
        ON DELETE CASCADE;

-- TABLE: QUESTIONS 
CREATE TABLE infm114.questions (
    question_id INT NOT NULL,
    text VARCHAR(255) NOT NULL,
    test_id INT NOT NULL,
    PRIMARY KEY (test_id, question_id)
);

ALTER TABLE infm114.questions
        ADD CONSTRAINT fk_test_question
        FOREIGN KEY (test_id)
        REFERENCES infm114.tests(test_id)
        ON DELETE CASCADE;

CREATE INDEX index_question_id ON infm114.questions (question_id);

-- TABLE: ANSWERS 
CREATE TABLE infm114.answers (
    answer_id INT NOT NULL,
    text VARCHAR(255) NOT NULL,
    correct TINYINT(1),
    question_id INT NOT NULL,
    test_id INT NOT NULL,
    PRIMARY KEY (answer_id, question_id, test_id)
);

ALTER TABLE infm114.answers
        ADD CONSTRAINT fk_question_answer
        FOREIGN KEY (question_id)
        REFERENCES infm114.questions(question_id)
        ON DELETE CASCADE;

ALTER TABLE infm114.answers
        ADD CONSTRAINT fk_test_answer
        FOREIGN KEY (test_id)
        REFERENCES infm114.tests(test_id)
        ON DELETE CASCADE;

CREATE INDEX index_answer_id ON infm114.answers (answer_id);

-- TABLE: STUDENTS 
CREATE TABLE infm114.students (
    student_id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- TABLE: STUDENTS 
CREATE TABLE infm114.student_answers (
    student_id INT,
    question_id INT,
    answer_id INT,
    test_id INT,
    PRIMARY KEY (student_id, answer_id, question_id, test_id)
);

ALTER TABLE infm114.student_answers
        ADD CONSTRAINT fk_student_answer
        FOREIGN KEY (student_id)
        REFERENCES infm114.students(student_id)
        ON DELETE CASCADE;

ALTER TABLE infm114.student_answers
        ADD CONSTRAINT fk_answer_answer
        FOREIGN KEY (answer_id)
        REFERENCES infm114.answers(answer_id)
        ON DELETE CASCADE;

ALTER TABLE infm114.student_answers
        ADD CONSTRAINT fk_question_student_answer
        FOREIGN KEY (question_id)
        REFERENCES infm114.questions(question_id)
        ON DELETE CASCADE;

ALTER TABLE infm114.student_answers
        ADD CONSTRAINT fk_test_s_answer
        FOREIGN KEY (test_id)
        REFERENCES infm114.tests(test_id)
        ON DELETE CASCADE;