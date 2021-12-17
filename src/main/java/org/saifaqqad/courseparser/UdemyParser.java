package org.saifaqqad.courseparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.stream.Collectors;

public class UdemyParser implements CourseParser {
    private static final String VALID_URL_PATTERN =
            "^(?:https?\\://)?(?:www\\.)?udemy\\.com/course/(?<courseName>[a-zA-Z0-9\\-]+)/?.*$";
    private static final String COURSE_TITLE_SELECTOR = "[data-purpose='lead-title']";
    private static final String COURSE_DESC_SELECTOR = "[data-purpose='lead-headline']";
    private static final String COURSE_PUBDATE_SELECTOR = "[data-purpose='last-update-date']";
    private static final String COURSE_IMAGE_SELECTOR = "[data-purpose='introduction-asset'] img";
    private static final String COURSE_AUTHOR_SELECTOR = ".instructor-links--names--7UPZj > a > span";


    @Override
    public Course getCourse(URL courseUrl) {
        courseUrl = validateURL(courseUrl, VALID_URL_PATTERN);
        if (courseUrl == null)
            return null;
        try {
            Document doc = Objects.requireNonNull(this.getDocument(courseUrl));
            String name = getName(doc);
            String description = getDescription(doc);
            LocalDate pubDate = getPublicationDate(doc);
            String publisher = "Udemy";
            String author = getAuthor(doc);
            String imageUrl = getImageUrl(doc);
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

    private String getImageUrl(Document doc) {
        return Objects.requireNonNullElse(doc.selectFirst(COURSE_IMAGE_SELECTOR), new Element("img")).attr("src");
    }

    private String getAuthor(Document doc) {
        return doc.select(COURSE_AUTHOR_SELECTOR)
                .stream()
                .map(Element::text)
                .collect(Collectors.joining(", "));
    }

    private LocalDate getPublicationDate(Document doc) {
        LocalDate publicationDate = null;
        try {
            String dateStr = Objects.requireNonNull(doc.selectFirst(COURSE_PUBDATE_SELECTOR)).child(1).text().replaceAll("[A-Za-z\\s]", "");
            publicationDate = YearMonth.parse(dateStr, DateTimeFormatter.ofPattern("M/yyyy")).atDay(1);
        } catch (NullPointerException | DateTimeParseException ignored) {
        }
        return publicationDate;
    }

    private String getDescription(Document doc) {
        return Objects.requireNonNullElse(doc.selectFirst(COURSE_DESC_SELECTOR), new Element("span")).text();
    }

    private String getName(Document doc) throws NullPointerException {
        return Objects.requireNonNull(doc.selectFirst(COURSE_TITLE_SELECTOR)).text();
    }

    @Override
    public Document getDocument(URL url) {
        try {
            HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
            HttpRequest request = HttpRequest.newBuilder().uri(url.toURI()).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Jsoup.parse(response.body());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            return null;
        }
    }
}
