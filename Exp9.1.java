// Course.java
public class Course {
private String courseName; private String duration;

public Course(String courseName, String duration) { this.courseName = courseName;
this.duration = duration;
}
public String getCourseName() { return courseName; } public String getDuration() { return duration; }
@Override
public String toString() {
return "Course: " + courseName + ", Duration: " + duration;
}
}
// Student.java
public class Student { private String name; private Course course;
public Student(String name, Course course) { this.name = name;
this.course = course;
}
public void showDetails() { System.out.println("Student: " + name); System.out.println(course);
}
}// AppConfig.java
import org.springframework.context.annotation.*;
@Configuration
public class AppConfig { @Bean
public Course course() {
return new Course("Java", "3 months");
}
@Bean
public Student student() {
return new Student("Jagadish", course());
}
}// MainApp.java
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
public class MainApp {
public static void main(String[] args) { ApplicationContext context = new
AnnotationConfigApplicationContext(AppConfig.class); Student student = context.getBean(Student.class); student.showDetails();
}
}

