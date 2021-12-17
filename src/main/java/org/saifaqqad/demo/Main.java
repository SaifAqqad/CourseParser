package org.saifaqqad.demo;

import org.saifaqqad.courseparser.CourseParser;

public class Main {
    public static void main(String[] args) {
        System.out.println(CourseParser.getByURL("https://www.pluralsight.com/courses/modern-java-big-picture"));
    }
}
