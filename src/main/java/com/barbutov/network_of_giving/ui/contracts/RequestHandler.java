package com.barbutov.network_of_giving.ui.contracts;

import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface RequestHandler {
    String handleRequest(Authentication authentication, String htmlFileName) throws IOException;
    String handleRequest(Authentication authentication, String htmlFileName, String cssFileName) throws IOException;
    String handleRequest(Authentication authentication, String htmlFileName, String jsFileName, String cssFileName) throws IOException;
    String handleRequest(Authentication authentication, String htmlFileName, String jsFileName, String cssFileName,
                         Object[] model) throws IOException;
    String handleRequest(Authentication authentication, String htmlFileName, String jsFileName, String cssFileName,
                         Object[][] models, String[] listItemTemplatePath) throws IOException;

}
