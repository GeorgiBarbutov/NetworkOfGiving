package com.barbutov.network_of_giving.ui;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Component
public class RequestHandlerImpl implements RequestHandler {
    private static final String TEMPLATE_PATH = "src\\main\\resources\\templates\\template.html";
    private static final String LOGGED_IN_NAVIGATION_PATH = "src\\main\\resources\\templates\\loggedInNavigation.html";
    private static final String LOGGED_OUT_NAVIGATION_PATH = "src\\main\\resources\\templates\\loggedOutNavigation.html";
    private static final String TEMPLATE_FILES_PATH = "src\\main\\resources\\templates\\";
    private static final String HTML_FILES_PATH = "src\\main\\resources\\static\\";
    private static final String JS_FILES_PATH = "src\\main\\resources\\js\\";

    public RequestHandlerImpl() {
    }

    @Override
    public String handleRequest(Authentication authentication, String htmlFileName, String jsFileName) throws IOException {
        Object[] model = new Object[0];

        return handleRequest(authentication, htmlFileName, jsFileName, model, null);
    }

    @Override
    public String handleRequest(Authentication authentication, String htmlFileName) throws IOException {
        return handleRequest(authentication, htmlFileName, "");
    }

    @Override
    public String handleRequest(Authentication authentication, String htmlFileName, String jsFileName, Object[] model) throws IOException {
        return handleRequest(authentication, htmlFileName, jsFileName, model, null);
    }

    @Override
    public String handleRequest(Authentication authentication, String htmlFileName, String jsFileName, Object[] model,
                                String listItemTemplatePath) throws IOException {
        String template = getFile(TEMPLATE_PATH);
        String navigation = getNavigation(authentication);
        String content = getContent(htmlFileName, model, listItemTemplatePath);
        String script = getScript(jsFileName);

        return template.replace("{{script}}", "<script>" + script + "</script>")
                .replace("{{navigation}}", navigation)
                .replace("{{content}}", content);
    }

    private String getFile(String fileLocation) throws IOException {
        Path path = Paths.get(fileLocation);

        return Files.readString(path);
    }

    private String getNavigation(Authentication authentication) throws IOException {
        String navigation;
        if(authentication != null){
            navigation = getFile(LOGGED_IN_NAVIGATION_PATH);
        } else {
            navigation = getFile(LOGGED_OUT_NAVIGATION_PATH);
        }

        return navigation;
    }

    private String getContent(String htmlFileName, Object[] model, String listItemTemplatePath) throws IOException {
        String content = getFile(HTML_FILES_PATH + htmlFileName + ".html");

        if(listItemTemplatePath != null) {
            String listItemTemplate = getFile(TEMPLATE_FILES_PATH + listItemTemplatePath + ".html");

            content = content.replace("{{list}}", listItemTemplate.repeat(model.length));
        }

        if(model.length != 0) {
            for (Object o : model) {
                Method[] methods = Arrays.stream(o.getClass().getDeclaredMethods())
                        .filter(m -> m.getName().startsWith("get"))
                        .toArray(Method[]::new);
                for (Method method : methods) {
                    try {
                        String methodName = method.getName().substring(3);
                        char replacementChar = methodName.toLowerCase().charAt(0);
                        String field = replacementChar + methodName.substring(1);

                        content = content.replaceFirst("\\{\\{" + field + "\\}\\}", method.invoke(o).toString());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return content;
    }

    private String getScript(String jsFileName) throws IOException {
        String script;
        if(jsFileName == null || jsFileName.equals("")){
            script = "";
        } else {
            script = getFile(JS_FILES_PATH + jsFileName + ".js");
        }

        return script;
    }
}
