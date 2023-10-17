package edu.jhu.assignment7;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Student
{
    int studentId;
    String firstName;
    String lastName;
	String dateOfBirth;
	String email;

    public Student(int studentId, String firstName, String lastName, String dateOfBirth, String email)
    {
        super();
        System.out.println("Student constuctor with args");
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
    }

    public int getStudentId()
    {
        return studentId;
    }

    public void setStudentId(int studentId)
    {
        this.studentId = studentId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}