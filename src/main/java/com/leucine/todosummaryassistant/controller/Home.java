package com.leucine.todosummaryassistant.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {

  @RequestMapping("/")
  public String show(){
    return "Hello World";
  }

}
