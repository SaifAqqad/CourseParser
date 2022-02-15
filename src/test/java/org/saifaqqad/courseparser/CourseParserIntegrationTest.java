package org.saifaqqad.courseparser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CourseParserIntegrationTest {
    @Test
    void udemy() {
        String courseURL = "https://www.udemy.com/course/learning-python-for-data-analysis-and-visualization/";
        CourseParser.Course course = CourseParser.getByURL(courseURL);
        assertNotNull(course);
        assertEquals("Learning Python for Data Analysis and Visualization", course.name());
        assertEquals("Learn python and how to use it to analyze,visualize and present data. Includes tons of sample code and hours of video!", course.description());
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
        assertEquals("JavaScript is the popular programming language which powers web pages and web applications. If you are new to programming or just new to the language, this course will get you started coding in JavaScript.", course.description());
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
        assertEquals("""
                This latest edition is extensively revised to accommodate all the latest features that come with C# 10 and .NET 6.
                You will learn object-oriented programming, writing, testing, and debugging functions, implementing interfaces, and inheriting classes. The book covers the .NET APIs for performing tasks like managing and querying data, monitoring and improving performance, and working with the filesystem, async streams, serialization, and encryption. It provides examples of cross-platform apps you can build and deploy, such as websites and services using ASP.NET Core.
                The best application for learning the C# language constructs and many of the .NET libraries does not distract with unnecessary application code. Hence, the C# and .NET topics covered in Chapters 1 to 12 feature console applications. In Chapters 13 to 17, having mastered the basics, you will build practical applications and services using ASP.NET Core, the Model-View-Controller (MVC) pattern, and Blazor.
                There are also two new online chapters on using .NET MAUI to build cross-platform apps and building services using a variety of technologies, including Web API, OData, gRPC, GraphQL, SignalR, and Azure Functions.""", course.description());
        assertEquals("Mark J. Price", course.author());
        assertEquals(courseURL, course.url());
        assertEquals("Packt", course.publisher());
        assertEquals("https://static.packt-cdn.com/products/9781801077361/cover/smaller", course.imageUrl().split("\\?")[0]);
    }

}
