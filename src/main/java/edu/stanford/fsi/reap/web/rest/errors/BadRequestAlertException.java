package edu.stanford.fsi.reap.web.rest.errors;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestAlertException extends AbstractThrowableProblem {

  private final String entityName;

  private final String errorKey;

  private final String i18nErrorKey;

  private final Object i18nContext;

  public BadRequestAlertException(String defaultMessage) {
    this(null, defaultMessage, "", "", "", null);
  }

  public BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
    this(null, defaultMessage, entityName, errorKey, "", null);
  }

  public BadRequestAlertException(String defaultMessage, String entityName, String errorKey, String i18nErrorKey, Object i18nContext) {
    this(null, defaultMessage, entityName, errorKey, i18nErrorKey, i18nContext);
  }

  public BadRequestAlertException(
          URI type, String defaultMessage, String entityName, String errorKey, String i18nErrorKey, Object i18nContext) {
    super(
            type,
            "BadRequest",
            Status.BAD_REQUEST,
            defaultMessage,
            null,
            null,
            getAlertParameters(entityName, errorKey));
    this.entityName = entityName;
    this.errorKey = errorKey;
    this.i18nErrorKey = i18nErrorKey;
    this.i18nContext = i18nContext;
  }

  private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("message", "error." + errorKey);
    parameters.put("params", entityName);
    return parameters;
  }
}
