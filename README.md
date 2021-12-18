## CourseParser

### Gradle

Copy the jar to the `libs` folder then add the dependency in `build.gradle`

```groovy
    dependencies {
    implementation files('libs/CourseParser-0.1-all.jar')
}
```

### Usage

```java
import org.saifaqqad.courseparser.CourseParser;
import org.saifaqqad.courseparser.CourseParser.Course;

public class Example {
    public static void main(String[] args) {
        String courseURL = "https://www.pluralsight.com/courses/java-persistence-hibernate-fundamentals";
        Course course = CourseParser.getByURL(courseURL);
        System.out.println(course.name());
    }
}
```