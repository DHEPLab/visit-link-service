package edu.stanford.fsi.reap.web.rest.errors;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import javax.servlet.http.HttpServletRequest;

@Getter
@Slf4j
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestAlertException extends AbstractThrowableProblem {

    private final String entityName;
    private final String errorKey;
    private final Object[] params;

    private static final MessageSource messageSource;

    static {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("messages");
        source.setDefaultEncoding("UTF-8");
        messageSource = source;
    }

    public BadRequestAlertException(String defaultMessage) {
        this(defaultMessage, new Object[]{});
    }

    public BadRequestAlertException(String defaultMessage, Object... params) {
        this(null, defaultMessage, "", "", params);
    }

    public BadRequestAlertException(URI type, String defaultMessage, String entityName, String errorKey, Object... params) {
        super(
                type,
                "BadRequest",
                Status.BAD_REQUEST,
                getTranslatedMessage(defaultMessage, params),
                null,
                null,
                getAlertParameters(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
        this.params = params;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }

    private static String getTranslatedMessage(String defaultMessage, Object... params) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String lang = request.getParameter("lang");
        Locale locale = "zh".equals(lang) ? Locale.CHINESE : Locale.ENGLISH;
        log.info("Locale: {}", locale);

        String translatedMessage = messageSource.getMessage(defaultMessage, params, locale);

        log.info("Translated message: {}", translatedMessage);
        return translatedMessage;
    }
}