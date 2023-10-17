package edu.jhu.assignment7;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Registrar
{
    String courseNumber;
    List<Integer> studentIds;

    public Registrar(String courseNumber)
    {
        super();
        System.out.println("Course constuctor with args");
        this.courseNumber = courseNumber;
        this.studentIds = new ArrayList<Integer>();;
    }

    public String getCourseNumber()
    {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber)
    {
        this.courseNumber = courseNumber;
    }

    public List<Integer> getStudentIds()
    {
        return studentIds;
    }

    public void setStudentIds(List<Integer> studentIds)
    {
        this.studentIds = studentIds;
    }
}