package com.topcueser.one2one;

import com.topcueser.one2one.withforeignkeyv2.Student;
import com.topcueser.one2one.withforeignkeyv2.StudentDetail;
import com.topcueser.one2one.withforeignkeyv2.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class StudentRepositoryWithForeignKeyV2Test {

    @Autowired
    StudentRepository studentRepository;

    @Test
    public void testAddStudent() {


        //Hibernate: create table student_details (id bigint generated by default as identity, school_name varchar(200) not null, school_no varchar(200) not null, student_id bigint, primary key (id))
        //Hibernate: create table students (id bigint generated by default as identity, name varchar(200) not null, surname varchar(200) not null, primary key (id))

        Student student = new Student();
        student.setName("Emir");
        student.setSurname("Topcu");

        StudentDetail studentDetail = new StudentDetail();
        studentDetail.setSchoolName("Bahçeşehir Koleji");
        studentDetail.setSchoolNo("123456");

        student.setStudentDetail(studentDetail);
        studentDetail.setStudent(student);

        Student saveStudent = studentRepository.save(student);
        assertThat(saveStudent).isNotNull();

    }
}