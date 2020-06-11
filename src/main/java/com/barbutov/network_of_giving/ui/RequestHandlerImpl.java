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
    private static final String NAVIGATION_SCRIPT = "navigation";
    private static final String TEMPLATE_FILES_PATH = "src\\main\\resources\\templates\\";
    private static final String HTML_FILES_PATH = "src\\main\\resources\\static\\";
    private static final String JS_FILES_PATH = "src\\main\\resources\\js\\";

    public RequestHandlerImpl() {
    }

    @Override
    public String handleRequest(Authentication authentication, String htmlFileName, String jsFileName) throws IOException {
        Object[][] model = new Object[0][];

        return handleRequest(authentication, htmlFileName, jsFileName, model, null);
    }

    @Override
    public String handleRequest(Authentication authentication, String htmlFileName) throws IOException {
        return handleRequest(authentication, htmlFileName, "");
    }

    @Override
    public String handleRequest(Authentication authentication, String htmlFileName, String jsFileName, Object[] model) throws IOException {
        Object[][] genericModel = new Object[1][];
        genericModel[0] = model;

        return handleRequest(authentication, htmlFileName, jsFileName, genericModel, null);
    }

    //authentication required for navigation bar, htmlFileName is the html file to be used, jsFileName is the jsFileToBeUsed,
    //models -> array of models to be used, models have to be POJO classes with getters for the wanted properties,
    // listItemTemplatePath -> paths for templates to be inserted in {{list}}
    @Override
    public String handleRequest(Authentication authentication, String htmlFileName, String jsFileName, Object[][] model,
                                String[] listItemTemplatePath) throws IOException {
        String template = getFile(TEMPLATE_PATH);
        String navigation = getNavigation(authentication);

        String content = getContent(htmlFileName, model, listItemTemplatePath);
        String navigationScript = getScript(NAVIGATION_SCRIPT);
        String script = getScript(jsFileName);

        return template.replace("{{script}}", "<script>" + script + "</script>")
                .replace("{{navigationScript}}", "<script>" + navigationScript + "</script>")
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

    //Replace {{list}} with items from a given template, {{{list}}} -> repeat the replacement several locations, then replace all models
    private String getContent(String htmlFileName, Object[][] models, String[] listItemTemplatePath) throws IOException {
        String content = getFile(HTML_FILES_PATH + htmlFileName + ".html");

        if(listItemTemplatePath != null && listItemTemplatePath.length > 0) {
            for (int i = 0; i < listItemTemplatePath.length; i++) {
                String listItemTemplate = getFile(TEMPLATE_FILES_PATH + listItemTemplatePath[i] + ".html");

                content = content.replaceAll("\\{\\{\\{list}}}", listItemTemplate.repeat(models[i].length));
                content = content.replaceFirst("\\{\\{list}}", listItemTemplate.repeat(models[i].length));
            }
        }
        for (Object[] model : models) {
            if (model.length != 0) {
                for (Object modelProperty : model) {
                    content = replaceFields(content, modelProperty);
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

    //replace every field of a model in {{field}}, {{{field}}} -> repeat the same field in several locations
    private String replaceFields(String content, Object modelProperty){
        Method[] methods = Arrays.stream(modelProperty.getClass().getDeclaredMethods())
                .filter(m -> m.getName().startsWith("get"))
                .toArray(Method[]::new);
        for (Method method : methods) {
            try {
                String methodName = method.getName().substring(3);
                char replacementChar = methodName.toLowerCase().charAt(0);
                String field = replacementChar + methodName.substring(1);

                content = content.replaceAll("\\{\\{\\{" + field + "}}}", method.invoke(modelProperty).toString());
                content = content.replaceFirst("\\{\\{" + field + "}}", method.invoke(modelProperty).toString());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return content;
    }
}
