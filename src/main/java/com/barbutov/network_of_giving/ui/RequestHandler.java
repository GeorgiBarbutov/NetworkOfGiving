package com.barbutov.network_of_giving.ui;

import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface RequestHandler {
    String handleRequest(Authentication authentication, String htmlFileName, String jsFileName) throws IOException;
    String handleRequest(Authentication authentication, String htmlFileName) throws IOException;
    String handleRequest(Authentication authentication, String htmlFileName, String jsFileName, Object[] model) throws IOException;
    String handleRequest(Authentication authentication, String htmlFileName, String jsFileName, Object[][] models,
                         String[] listItemTemplatePath) throws IOException;

}
