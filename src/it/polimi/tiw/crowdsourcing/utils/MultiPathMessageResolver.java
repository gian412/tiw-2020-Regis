package it.polimi.tiw.crowdsourcing.utils;

import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.ServletContextTemplateResource;
import javax.servlet.ServletContext;
import java.util.Locale;
import java.util.Map;

public class MultiPathMessageResolver extends StandardMessageResolver {

    private ServletContext context;
    private String directory;

    /*public MultiPathMessageResolver(ServletContext context, String path) {
        super();
        this.context = context;
        this.directory = path;
    }

    @Override
    protected Map<String, String> resolveMessagesForTemplate(String template, ITemplateResource templateResource,
                                                             Locale locale) {
        System.out.println(template);
        String regex = "(.*)(/[^/]*$)";
        String file = template.replaceFirst(regex, "$2");
        System.out.println(file);
        String finalPath = "/WEB-INF/" + directory + file;
        //String finalPath = "/WEB-INF/" + directory + "/messages.html";
        System.out.println(finalPath);
        templateResource = new ServletContextTemplateResource(context, finalPath, null);
        return super.resolveMessagesForTemplate(finalPath, templateResource, locale);
    }*/

}