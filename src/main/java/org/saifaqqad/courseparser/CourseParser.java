package org.saifaqqad.courseparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface CourseParser {

    record Course(String name,
                  String description,
                  String author,
                  LocalDate publicationDate,
                  String publisher,
                  String imageUrl,
                  String url) {
    }

    Pattern urlPattern = Pattern.compile("^(?:https?://)?(?:www\\.)?(?<host>[A-Za-z\\-]+)\\..{2,}/?");

    Course getCourse(URL courseUrl);

    static Course getByURL(String courseUrl) {
        try {
            Matcher matcher = urlPattern.matcher(courseUrl);
            String host = matcher.find() ? matcher.group("host") : "";
            CourseParser parser = switch (host) {
                case "packtpub" -> new PacktpubParser();
                case "udemy" -> new UdemyParser();
                case "pluralsight" -> new PluralsightParser();
                default -> throw new NullPointerException();
            };
            return parser.getCourse(courseUrl);
        } catch (NullPointerException e) {
            return null;
        }
    }

    default Course getCourse(String courseUrl) {
        try {
            if (!courseUrl.matches("^\\w+?://.*")) {
                courseUrl = "https://" + courseUrl;
            }
            return this.getCourse(new URL(courseUrl));
        } catch (MalformedURLException e) {
            return null;
        }
    }

    default URL validateURL(URL url, String pattern) {
        String urlString = url.toString();
        if (!urlString.matches(pattern))
            return null;
        return url;
    }

    default Document getDocument(URL url) {
        try {
            return Jsoup.connect(url.toString()).get();
        } catch (IOException e) {
            return null;
        }
    }

    default Map<String, String> getStyles(Element element) {
        Map<String, String> styles = new HashMap<>();
        if (element == null || !element.hasAttr("style"))
            return null;
        String[] styleAttrs = element.attr("style").split(";");
        for (String styleAttr : styleAttrs) {
            String[] style = styleAttr.split(":", 2);
            styles.put(style[0], style[1]);
        }
        return styles;
    }

}
