package com.selfshelf.project.model.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSearch {
    private String title;
    private String author;
    private Integer yearOfPublish;
    private Integer pages;

    public String getAuthor() {
        return Strings.isNotBlank(author) ? author : null;
    }

    public String getTitle() {
        return Strings.isNotBlank(title) ? title : null;
    }
}

