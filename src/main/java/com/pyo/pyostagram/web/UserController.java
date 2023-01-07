package com.pyo.pyostagram.web;


import com.pyo.pyostagram.config.auth.PrincipalDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {


    @GetMapping("/user/{id}")
    public String profile(@PathVariable int id){

        return "user/profile";
    }


    @GetMapping("/user/{id}/update")
    public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("세션 정보 : " + principalDetails.getUser());

        return "user/update";
    }
}