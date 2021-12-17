package org.saifaqqad.courseparser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PluralsightParser implements CourseParser {
    private static final String VALID_URL_PATTERN =
            "^(?:https?\\://)?(?:www\\.)?pluralsight\\.com/courses/(?<courseName>[a-zA-Z0-9\\-]+)/?.*$";
    private static final String COURSE_TITLE_SELECTOR = ".title.section h1";
    private static final String COURSE_DESC_SELECTOR = ".text.parbase.section .text-component";
    private static final String COURSE_INFO_SELECTOR = "#course-description-tile-info div.course-info__row";
    private static final String COURSE_IMAGE_SELECTOR = "#course-page-hero";
    private static final String COURSE_AUTHOR_SELECTOR = ".title--alternate > a";

    @Override
    public Course getCourse(URL courseUrl) {
        courseUrl = validateURL(courseUrl, VALID_URL_PATTERN);
        if (courseUrl == null)
            return null;
        try {
            Document doc = Objects.requireNonNull(this.getDocument(courseUrl));
            String name = getName(doc);
            String publisher = "Pluralsight";
            String description = getDescription(doc);
            String imageUrl = getImageUrl(doc);
            String author = getAuthor(doc);
            LocalDate pubDate = getPublicationDate(doc);
            String url = courseUrl.toString();
            return new Course(
                    name,
                    description,
                    author,
                    pubDate,
                    publisher,
                    imageUrl,
                    url
            );
        } catch (NullPointerException e) {
            return null;
        }
    }

    private LocalDate getPublicationDate(Document doc) {
        String dateString = doc.select(COURSE_INFO_SELECTOR)
                .stream()
                .filter(element -> element.child(0).text().contains("Updated"))
                .map(element -> element.child(1).text())
                .limit(1)
                .collect(Collectors.joining(""));
        if (dateString.length() > 0) {
            try {
                return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MMM d, yyyy"));
            } catch (DateTimeParseException ignored) {
            }
        }
        return null;
    }

    private String getAuthor(Document doc) throws NullPointerException {
        return Objects.requireNonNull(doc.selectFirst(COURSE_AUTHOR_SELECTOR)).text();
    }

    private String getImageUrl(Document doc) {
        Map<String, String> styles = getStyles(doc.selectFirst(COURSE_IMAGE_SELECTOR));
        if (styles != null)
            return styles.get("background-image")
                    .replace("url('", "")
                    .replace("')", "");
        return null;
    }

    private String getDescription(Document doc) {
        return Objects.requireNonNullElse(doc.selectFirst(COURSE_DESC_SELECTOR), new Element("span")).text();
    }

    private String getName(Document doc) throws NullPointerException {
        return Objects.requireNonNull(doc.selectFirst(COURSE_TITLE_SELECTOR)).text();
    }
}
