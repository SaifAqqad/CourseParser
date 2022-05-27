package org.saifaqqad.courseparser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseParserIntegrationTest {
    @Test
    void udemy() {
        String courseURL = "https://www.udemy.com/course/learning-python-for-data-analysis-and-visualization/";
        CourseParser.Course course = CourseParser.getByURL(courseURL);
        assertNotNull(course);
        assertEquals("Learning Python for Data Analysis and Visualization", course.name());
        assertEquals(
            "Learn python and how to use it to analyze,visualize and present data. Includes tons of sample code and hours of video!",
            course.description());
        assertEquals("Jose Portilla", course.author());
        assertEquals(courseURL, course.url());
        assertEquals("Udemy", course.publisher());
        assertEquals("https://img-c.udemycdn.com/course/240x135/396876_cc92_7.jpg", course.imageUrl().split("\\?")[0]);
    }

    @Test
    void pluralsight() {
        String courseURL = "https://www.pluralsight.com/courses/javascript-getting-started";
        CourseParser.Course course = CourseParser.getByURL(courseURL);
        assertNotNull(course);
        assertEquals("JavaScript: Getting Started", course.name());
        assertEquals(
            "JavaScript is the popular programming language which powers web pages and web applications. If you are new to programming or just new to the language, this course will get you started coding in JavaScript.",
            course.description());
        assertEquals("Mark Zamoyta", course.author());
        assertEquals(courseURL, course.url());
        assertEquals("Pluralsight", course.publisher());
        assertNull(course.imageUrl());
    }

    @Test
    void packtpub() {
        String courseURL = "https://www.packtpub.com/product/c-10-and-net-6-modern-cross-platform-development-sixth-edition/9781801077361";
        CourseParser.Course course = CourseParser.getByURL(courseURL);
        assertNotNull(course);
        assertEquals("C# 10 and .NET 6 \u2013 Modern Cross-Platform Development - Sixth Edition", course.name());
        assertEquals(
            """
                Extensively revised to accommodate all the latest features that come with C# 10 and .NET 6, this latest edition of our comprehensive guide will get you coding in C# with confidence.
                You’ll learn object-oriented programming, writing, testing, and debugging functions, implementing interfaces, and inheriting classes. The book covers the .NET APIs for performing tasks like managing and querying data, monitoring and improving performance, and working with the filesystem, async streams, and serialization. You’ll build and deploy cross-platform apps, such as websites and services using ASP.NET Core.
                Instead of distracting you with unnecessary application code, the first twelve chapters will teach you about C# language constructs and many of the .NET libraries through simple console applications. In later chapters, having mastered the basics, you’ll then build practical applications and services using ASP.NET Core, the Model-View-Controller (MVC) pattern, and Blazor.""",
            course.description());
        assertEquals("Mark J. Price", course.author());
        assertEquals(courseURL, course.url());
        assertEquals("Packt", course.publisher());
        assertEquals(
            "https://static.packt-cdn.com/products/9781801077361/cover/smaller",
            course.imageUrl().split("\\?")[0]);
    }

    @Test
    void oreilly() {
        String courseURL = "https://www.oreilly.com/videos/the-complete-java/9781801075190/";
        CourseParser.Course course = CourseParser.getByURL(courseURL);
        assertNotNull(course);
        assertEquals("The Complete Java Developer Course: From Beginner to Master", course.name());
        assertEquals(
            """
                Learn the tips and tricks to become a proficient Java programmer and master the fundamentals of the language
                About This Video
                Master the fundamentals of Java programming, no matter your current level of coding skillsLearn the theory behind the code and put it to practice right awaySolidify your skills as a Java developer by building engaging portfolio projectsIn Detail
                Be it websites, mobile apps, or desktop software, Java remains one of the most popular programming languages around. With over 7.6 million developers using it worldwide, the surge of new programming languages has not dampened the demand for this 25+-year-old language. With an average Java developer in the US earning over $104,000 a year, Java programming skills are still highly sought after.
                This course is designed to teach you these skills from scratch, starting with JDK installation followed by the creation of your first program. Then, as you progress through the sections, you’ll start learning the core Java concepts such as control statements, arrays, strings, methods, objects, and more. In each of these sections, you’ll be building a unique, exciting project, thus ensuring you’re not just learning the theory but also practicing what you learned. Average of three, Mad Libs clone, and tic-tac-toe are just some of the projects you’ll be building in this course. To make sure you’ve mastered each line of code, there are practical hands-on coding challenges in every lecture.
                By the end of this course, you will have mastered the best tips, tricks, and theory behind the Java programming language. The hands-on nature of this course will ensure that you can readily apply your skills to real-world projects.
                Who this book is for
                If you are a beginner coder and are new to Java, this course is for you. Experienced Java programmers who want to keep their skills sharp or developers looking to upskill themselves can find great value in this course.""",
            course.description());
        assertEquals("Packt Publishing", course.publisher());
        assertEquals("Codestars by Rob Percival, John P. Baugh", course.author());
        assertEquals(courseURL, course.url());
        assertEquals("https://learning.oreilly.com/library/cover/9781801075190/250w/", course.imageUrl());
    }

}
