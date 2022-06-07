package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import lombok.Data;

//@Component
@Controller
@RequestMapping
public class testController {
	
	@GetMapping("main")
	public String getMain(Model model) {
		model.addAttribute("test", "테스트성공!");
		
		return "main";
	}
	
	@GetMapping("basic/text-basic") //컨텐츠 데이터 출력
	public String textBasic(Model model) {
		model.addAttribute("data", "Hello <b>Spring<b>");
		
		return "basic/text-basic";
	}
	
    @GetMapping("/basic/variable") //변수 - SpringEL
    public String variable(Model model) {
        User userA = new User("userA", 10);
        User userB = new User("userB", 20);

        List<User> list = new ArrayList<>(Arrays.asList(userA, userB));

        Map<String, User> map = new HashMap<>();
        map.put("userA", userA);
        map.put("userB", userB);

        model.addAttribute("user", userA);
        model.addAttribute("users", list);
        model.addAttribute("userMap", map);

        return "basic/variable";
    }

    @Data
    public class User{
        private String username;
        private int age;

        public User(String username, int age) {
            this.username = username;
            this.age = age;
        }
    }
    
    @GetMapping("/basic/basic-objects") //기본 객체, 편의 객체
    public String basicObject(HttpSession httpSession) {
    	httpSession.setAttribute("sessionData", "Hello Session");

        return "basic/basic-objects";  
    }
    
    @Component("helloBean")
    static class HelloBean{
    	public String hello(String data) {
    		return "Hello " + data;
    	}
    }
    
    @GetMapping("/basic/date") //유틸리티 객체와 날짜
    public String date(Model model) {
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "basic/date";
    } 
    
	
}//END class testController 
