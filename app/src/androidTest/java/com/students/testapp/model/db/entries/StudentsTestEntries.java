package com.students.testapp.model.db.entries;

import com.students.testapp.model.entity.Student;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public enum StudentsTestEntries {
    STUDENT_1(1,"33855b8f-49c0-49bd-bd98-60512c053435", "Ivan1", "Petrov-1", 856742400000L),
    STUDENT_2(2,"be0c7703-0b08-40ee-b18e-bd96038e9a92", "Ivan2", "Petrov-2", 856828800000L),
    STUDENT_3(3,"ffb50c2a-421f-4087-a9fb-84b688f708dd", "Ivan3", "Petrov-3", 856915200000L),
    STUDENT_4(4,"de5c6c21-9a54-4c8f-8412-58a733c24d2e", "Ivan4", "Petrov-4", 857001600000L),
    STUDENT_5(5,"4430d019-c4b9-4e41-8bb9-9d7b8a6601a4", "Ivan5", "Petrov-5", 857088000000L),
    STUDENT_6(6,"19866f71-2ac6-4272-9372-60f26343718f", "Ivan6", "Petrov-6", 857174400000L);

    private long id;
    private String tokenId;
    private String firstName;
    private String lastName;
    private long birthday;

    StudentsTestEntries(long id, String tokenId, String firstName, String lastName, long birthday) {
        this.id = id;
        this.tokenId = tokenId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
    }

    public Student getStudentInstance(){
        return new Student.Builder()
                .setStudentId(this.id)
                .setStudentTokenId(this.tokenId)
                .setFirstName(this.firstName)
                .setLastName(this.lastName)
                .setBirthday(this.birthday)
                .build();
    }
}
