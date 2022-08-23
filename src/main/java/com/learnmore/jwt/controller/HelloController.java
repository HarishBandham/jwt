package com.learnmore.jwt.controller;

import com.learnmore.jwt.Entity.AuthenticationRequest;
import com.learnmore.jwt.Entity.AuthenticationResponse;
import com.learnmore.jwt.service.MyUserServiceDetails;
import com.learnmore.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserServiceDetails myUserServiceDetails;
    @Autowired
    private JwtUtil jwtUtil;
    @GetMapping("/hello")
    public String getHello() {
        return "Hello World!";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticateRequest)throws Exception{

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticateRequest.getUserName(),
                            authenticateRequest.getPassword()));
        } catch (BadCredentialsException ex){
            System.out.println("HelloController.createAuthenticationToken exception");
            throw new Exception("incorrect User name and Password");
        }
        final UserDetails userDetails = myUserServiceDetails.loadUserByUsername(authenticateRequest.getUserName());
        final String jwtToken = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
    
    }
}
