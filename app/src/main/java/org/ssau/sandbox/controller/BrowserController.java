package org.ssau.sandbox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * BrowserController
 */
 @Controller
public class BrowserController {

  @GetMapping("login")
  public String login(){
    return "login";
  }

	
}
