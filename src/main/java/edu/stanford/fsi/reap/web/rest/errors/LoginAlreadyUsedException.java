package edu.stanford.fsi.reap.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LoginAlreadyUsedException extends BadRequestAlertException {

  public LoginAlreadyUsedException(String login) {
    super("账户名称: " + login + " 已经存在", "userManagement", "userexists", "userExists", initContext(login));
  }

  private static Map<String, Object> initContext(String login) {
    Map<String, Object> context = new HashMap<>();
    context.put("login", login);
    return context;
  }
}
