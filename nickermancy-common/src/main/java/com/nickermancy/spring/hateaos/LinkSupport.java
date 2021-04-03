package com.nickermancy.spring.hateaos;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;

public class LinkSupport {

    public static Iterable<Link> getNavigationLinks(long total, Pageable pageable) {
        var uriBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
        var pageSize = pageable.getPageSize();
        var numPages = (total / pageSize) + (total % pageSize == 0 ? 0 : 1);
        var firstPage = 0;
        var lastPage = numPages - 1;
        var pageNumber = Math.min(lastPage, Math.max(firstPage, pageable.getPageNumber()));
        var prevPage = pageNumber - 1;
        var nextPage = pageNumber + 1;
        var links = new ArrayList<Link>();
        links.add(pageLink(uriBuilder, firstPage, "first"));
        if (pageable.getPageNumber() > 0) {
            links.add(pageLink(uriBuilder, prevPage, "previous"));
        }
        if (pageable.getPageNumber() < lastPage) {
            links.add(pageLink(uriBuilder, nextPage, "next"));
        }
        links.add(pageLink(uriBuilder, lastPage, "last"));
        return links;
    }

    public static Link pageLink(UriComponentsBuilder uriBuilder, long page, String relation) {
        return Link.of(uriBuilder
                .replaceQueryParam("page", page)
                .build().toUriString(), relation);
    }
}
