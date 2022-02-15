package org.saifaqqad.courseparser;

import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CourseParserTest {
    public CourseParser defaultCourseParser = (courseUrl) -> null;

    @Test
    void validateURL() {
        String validCourseUrl = "https://www.udemy.com/course/learning-python-for-data-analysis-and-visualization/";
        String invalidCourseUrl = "udemy.com/courses/learning-python-for-data-analysis-and-visualization/";
        String Pattern = UdemyParser.VALID_URL_PATTERN;
        assertNotNull(defaultCourseParser.validateURL(validCourseUrl, Pattern));
        assertNull(defaultCourseParser.validateURL(invalidCourseUrl, Pattern));
    }

    @Test
    void getStyles() {
        Element element = new Element("div");
        element.attr("style", "height:55px;width:20rem;color:#ffffff;");
        Map<String, String> expected = Map.of(
                "height", "55px",
                "width", "20rem",
                "color", "#ffffff"
        );
        Map<String, String> actual = defaultCourseParser.getStyles(element);
        assertTrue(expected.keySet().containsAll(actual.keySet()));
        assertTrue(actual.keySet().containsAll(expected.keySet()));
    }

    @Test
    void nonNullOrFail() {
        String testString = "test string";
        assertEquals(testString, defaultCourseParser.nonNullOrFail(testString, "failure message"));
        assertThrows(CourseParser.CourseParserException.class, () -> defaultCourseParser.nonNullOrFail(null, "failure message"));
    }

    @Test
    void getByURL() {
        assertThrows(CourseParser.CourseParserException.class, () -> CourseParser.getByURL("https://www.amazon.com/dp/1617293563/"));
        assertDoesNotThrow(() -> {
            CourseParser.getByURL("https://www.udemy.com/course/learning-python-for-data-analysis-and-visualization/");
        });
        assertDoesNotThrow(() -> {
            CourseParser.getByURL("https://www.packtpub.com/product/c-10-and-net-6-modern-cross-platform-development-sixth-edition/9781801077361");
        });
        assertDoesNotThrow(() -> {
            CourseParser.getByURL("www.pluralsight.com/courses/javascript-getting-started");
        });
    }

}
