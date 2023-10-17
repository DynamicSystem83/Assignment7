package edu.jhu.assignment7;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Course
{
    String courseNumber;
    String courseTitle;

    public Course(String courseNumber, String courseTitle)
    {
        super();
        System.out.println("Course constuctor with args");
        this.courseNumber = courseNumber;
        this.courseTitle = courseTitle;
    }

    public String getCourseNumber()
    {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber)
    {
        this.courseNumber = courseNumber;
    }

    public String getCourseTitle()
    {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle)
    {
        this.courseTitle = courseTitle;
    }
}