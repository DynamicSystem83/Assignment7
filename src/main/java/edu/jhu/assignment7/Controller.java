package edu.jhu.assignment7;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.IllegalArgumentException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.ConstraintViolationException;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Email;

@RestController
@Validated
public class Controller
{
	private List<Student> students = new ArrayList<Student>();
	private final AtomicInteger studentIdCounter = new AtomicInteger();
	private List<Course> courses = new ArrayList<Course>();
	private List<Registrar> registrars = new ArrayList<Registrar>();
    
	public Controller()
	{
		students.add(new Student(studentIdCounter.incrementAndGet(), "John", "Doe", "2000-12-31", "jDoe@gmail.com"));
		students.add(new Student(studentIdCounter.incrementAndGet(), "Jane", "Smith", "2000-2-28", "jSmith@gmail.com"));
		students.add(new Student(studentIdCounter.incrementAndGet(), "Brian", "Anderson", "2000-01-01", "bAnderson@gmail.com"));

        courses.add(new Course("605.789", "Service API Design and Development"));
        courses.add(new Course("605.782", "Web Application Development with Java"));
		
		registrars.add(new Registrar("605.789"));
		registrars.get(0).getStudentIds().add(1);
		registrars.get(0).getStudentIds().add(3);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e)
	{
		return new ResponseEntity<>("Validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e)
	{
		return new ResponseEntity<>("Validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e)
	{
		return new ResponseEntity<>("Validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	}


	@PostMapping(value = "/students")
    public ResponseEntity addStudent(@RequestParam(value="firstName", required=false) String firstName,
									 @RequestParam(value="lastName", required=true) String lastName,
									 @RequestParam(value="dateOfBirth", required=true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
									 @RequestParam(value="email", required=true) @Email String email)
    {
        System.out.println("Create new student using POST (addStudent)");
        students.add(new Student(studentIdCounter.incrementAndGet(), firstName, lastName, dateOfBirth.toString(), email));
        return ResponseEntity.ok(students);
    }

	@GetMapping(value = "/students/{id}")
    public ResponseEntity getStudent(@PathVariable int id)
    {
        System.out.println("GET specific student with id using getStudent");
		for (Student s : students)
		{
			if (s.getStudentId() == id)
			{
				return ResponseEntity.ok(s);
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student (" + id + ") not found");
    }

	@GetMapping(value = "/students")
    public ResponseEntity getAllStudents()
    {
        System.out.println("Get all students");
        return ResponseEntity.ok(students);
    }

	@PutMapping(value = "/students/{id}")
    public ResponseEntity updateStudent(@PathVariable int id,
								  @RequestParam(value="firstName", required=false) String firstName,
								  @RequestParam(value="lastName", required=true) String lastName,
								  @RequestParam(value="dateOfBirth", required=true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
								  @RequestParam(value="email", required=true) @Email String email)
    {   
        System.out.println("Update student using updateStudent");
		for (Student s : students)
		{
			if (s.getStudentId() == id)
			{
				s.setFirstName(firstName);
				s.setLastName(lastName);
				s.setDateOfBirth(dateOfBirth.toString());
				s.setEmail(email);
				return ResponseEntity.ok(students);
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Student (" + id + ") not updated because it was not found");
    }

	@DeleteMapping(value = "/students/{id}")
    public ResponseEntity deleteStudent(@PathVariable int id)
    {
        System.out.println("Delete student with id using deleteStudent");
		for (Student s : students)
		{
			if (s.getStudentId() == id)
			{
				for (Registrar r : registrars)
				{
					if (r.getStudentIds().contains(id))
					{
						return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Student (" + id + ") not deleted because it is registered to a course (" + r.getCourseNumber() + ")");
					}
				}
				students.remove(s);
				return ResponseEntity.ok(students);
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Student (" + id + ") not deleted because it was not found");
    }



	@PostMapping(value = "/courses")
    public ResponseEntity addCourse(@RequestParam(value="courseNumber", required=true) String courseNumber,
									 @RequestParam(value="courseTitle", required=true) String courseTitle)
    {
        System.out.println("Create new course using POST (addCourse)");
        courses.add(new Course(courseNumber, courseTitle));
        return ResponseEntity.ok(courses);
    }

	@GetMapping(value = "/courses/{number}")
    public ResponseEntity getCourse(@PathVariable String number)
    {
        System.out.println("GET specific course with number using getCourse");
		for (Course c : courses)
		{
			if (c.getCourseNumber().equals(number))
			{
				return ResponseEntity.ok(c);
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course (" + number + ") not found");
    }

	@GetMapping(value = "/courses")
    public ResponseEntity getAllCourses()
    {
        System.out.println("Get all courses");
        return ResponseEntity.ok(courses);
    }

	@PutMapping(value = "/courses/{number}")
    public ResponseEntity updateCourse(@PathVariable String number,
									   @RequestParam(value="courseTitle", required=true) String courseTitle)
    {   
        System.out.println("Update course using updateCourse");
		for (Course c : courses)
		{
			if (c.getCourseNumber().equals(number))
			{
				c.setCourseTitle(courseTitle);
				return ResponseEntity.ok(courses);
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Course (" + number + ") not updated because it was not found");
    }

	@DeleteMapping(value = "/courses/{number}")
    public ResponseEntity deleteCourse(@PathVariable String number)
    {
        System.out.println("Delete course with number using deleteCourse");
		for (Course c : courses)
		{
			if (c.getCourseNumber().equals(number))
			{
				for (Registrar r : registrars)
				{
					if (r.getCourseNumber().equals(number) && !r.getStudentIds().isEmpty())
					{
						return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Course (" + number + ") not deleted because it has students registered");
					}
				}
				courses.remove(c);
				return ResponseEntity.ok(courses);
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Course (" + number + ") not deleted because it was not found");
    }



	@GetMapping(value = "/registrars")
    public ResponseEntity getAllRegistrars()
    {
        System.out.println("GET all registrars using getAllRegistrars");
		return ResponseEntity.ok(registrars);
    }

	@GetMapping(value = "/registrars/{number}")
    public ResponseEntity getRegistrar(@PathVariable String number)
    {
        System.out.println("GET specific list of students for a course with number using getRegistrar");
		for (Registrar r : registrars)
		{
			if (r.getCourseNumber().equals(number))
			{
				return ResponseEntity.ok(createStudentList(r.getStudentIds()));
			}
		}
		for (Course c : courses)
		{
			if (c.getCourseNumber().equals(number))
			{
				Registrar r = new Registrar(number);
				registrars.add(r);
				return ResponseEntity.ok(createStudentList(r.getStudentIds()));
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course (" + number + ") was not found");
    }
	
	private List<Student> createStudentList(List<Integer> studentList)
	{
		List<Student> outList = new ArrayList<Student>();
		for (Student s :students)
		{
			if (studentList.contains(s.getStudentId()))
			{
				outList.add(s);
			}
		}
		return outList;
	}

	@PutMapping(value = "/registrars/{number}")
    public ResponseEntity addStudentToCourse(@PathVariable String number,
									   @RequestParam(value="studentId", required=true) int studentId)
    {
        System.out.println("Add student to a course with number using addStudentToCourse");
		for (Registrar r : registrars)
		{
			if (r.getCourseNumber().equals(number))
			{
				if (r.getStudentIds().contains(studentId))
				{
					return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Student (" + studentId + ") already registered for course (" + number + ")");
				}
				else
				{
					if (r.getStudentIds().size() < 15)
					{
						r.getStudentIds().add(studentId);
						return ResponseEntity.ok(r.getStudentIds());
					}
					return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Max number of students registered for course (" + number + ")");
				}
			}
		}
		for (Course c : courses)
		{
			if (c.getCourseNumber().equals(number))
			{
				Registrar r = new Registrar(number);
				r.getStudentIds().add(studentId);
				registrars.add(r);
				return ResponseEntity.ok(r.getStudentIds());
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Did not add student (" + studentId + ") to course (" + number + ") because the course was not found");
    }

	@DeleteMapping(value = "/registrars/{number}")
    public ResponseEntity removeStudentFromCourse(@PathVariable String number,
									   @RequestParam(value="studentId", required=true) int studentId)
    {
        System.out.println("Remove a student from a course with number using removeStudentFromCourse");
		for (Registrar r : registrars)
		{
			if (r.getCourseNumber().equals(number))
			{
				int index = r.getStudentIds().indexOf(studentId);
				if (index != -1)
				{
					r.getStudentIds().remove(index);
					return ResponseEntity.ok(r.getStudentIds());
				}
				return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Student (" + studentId + ") not registered for course (" + number + ")");
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Course (" + number + ") does not have a registrar record");
    }
}