package org.saifaqqad.courseparser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

public class PacktpubParser implements CourseParser {
    private static final String VALID_URL_PATTERN =
            "^(?:https?\\://)?(?:www\\.)?packtpub\\.com/product/(?<courseName>[a-zA-Z0-9\\-]+)/(?<courseISBN>[0-9\\-]+)/?.*$";
    private static final String COURSE_TITLE_SELECTOR = ".product-info__title";
    private static final String COURSE_DESC_SELECTOR = ".overview__body > p";
    private static final String COURSE_INFO_SELECTOR = ".datalist__item";
    private static final String COURSE_IMAGE_SELECTOR = "img.product-image";
    private static final String COURSE_AUTHOR_SELECTOR = ".product-info__author";

    @Override
    public Course getCourse(URL courseUrl) {
        courseUrl = validateURL(courseUrl, VALID_URL_PATTERN);
        if (courseUrl == null)
            return null;
        try {
            Document doc = Objects.requireNonNull(this.getDocument(courseUrl));
            String name = getName(doc);
            String description = getDescription(doc);
            String Author = getAuthor(doc);
            String imageUrl = getImageUrl(doc);
            String url = courseUrl.toString();
            Object[] publicationInfo = getPublicationInfo(doc);
            String publisher = (String) publicationInfo[1];
            LocalDate publicationDate = (LocalDate) publicationInfo[0];
            return new Course(
                    name,
                    description,
                    Author,
                    publicationDate,
                    publisher,
                    imageUrl,
                    url
            );
        } catch (NullPointerException | NoSuchElementException e) {
            return null;
        }
    }

    private Object[] getPublicationInfo(Document doc) {
        Object[] info = new Object[2];
        doc.select(COURSE_INFO_SELECTOR).forEach(element -> {
            String name = Objects.requireNonNull(element.selectFirst("dt")).text();
            String value = Objects.requireNonNull(element.selectFirst("dd")).text();
            if ("".equals(value))
                return;
            switch (name) {
                case "Publication date:" -> info[0] = YearMonth.parse(value, DateTimeFormatter.ofPattern("MMMM yyyy")).atDay(1);
                case "Publisher" -> info[1] = value;
            }
        });
        return info;
    }

    private String getImageUrl(Document doc) {
        return doc.select(COURSE_IMAGE_SELECTOR).attr("src");
    }

    private String getAuthor(Document doc) throws NullPointerException {
        return Objects.requireNonNull(doc.selectFirst(COURSE_AUTHOR_SELECTOR)).text().replaceFirst("By\\s", "");
    }

    private String getDescription(Document doc) {
        return doc.select(COURSE_DESC_SELECTOR)
                .stream()
                .map(Element::text)
                .filter(txt -> !"".equals(txt))
                .collect(Collectors.joining("\n"));
    }

    private String getName(Document doc) throws NullPointerException {
        return Objects.requireNonNull(doc.selectFirst(COURSE_TITLE_SELECTOR)).text();
    }
}
