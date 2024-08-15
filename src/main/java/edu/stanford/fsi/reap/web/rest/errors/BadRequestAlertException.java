package edu.stanford.fsi.reap.web.rest.errors;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestAlertException extends AbstractThrowableProblem {

  private final String entityName;

  private final String errorKey;

  public BadRequestAlertException(String defaultMessage) {
    this(null, defaultMessage, "", "");
  }

  public BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
    this(null, defaultMessage, entityName, errorKey);
  }

  public BadRequestAlertException(
      URI type, String defaultMessage, String entityName, String errorKey) {
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
  }

  public String getEntityName() {
    return entityName;
  }

  public String getErrorKey() {
    return errorKey;
  }

  private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("message", "error." + errorKey);
    parameters.put("params", entityName);
    return parameters;
  }
}
