package com.example.backend.user;

import com.example.backend.book.Book;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Getting all users you searched for
    // Post mapping, because we obtain a string for the search
    @PostMapping(path="/getUsers" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String,Object>> getUsers(
            @RequestParam("string") String string
    ){
        return userService.getUsers(string);
    }

    // Change Password
    @PutMapping(path = "/changePassword", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String,Object>> changePassword(
            @RequestParam("email") String email,
            @RequestParam("old_password") String old_password,
            @RequestParam("new_password") String new_password,
            @RequestParam("check_password") String check_password)
    {return userService.changePassword(email,old_password,new_password,check_password);}

    // Register New User
    @PostMapping(path = "/registerUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String,Object>> registerNewUser(
            @RequestParam("email") String email,
            @RequestParam("meno") String meno,
            @RequestParam("priezvisko") String priezvisko,
            @RequestParam("password_hash") String password_hash,
            @RequestParam("check_password") String check_password,
            @RequestParam(required = false) boolean is_admin)

    { return userService.registerNewUser(email,meno,priezvisko,password_hash,check_password,is_admin); }


    // Login User
    @PostMapping(path = "/loginUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String,Object>> loginUser(
            @RequestParam("email") String email,
            @RequestParam("password_hash") String password_hash)
    { return userService.loginUser(email,password_hash); }

    // Remove User
    @DeleteMapping(path="/removeUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String,Object>> removeUser(
            @RequestParam("email") String email){
        return userService.removeUser(email);
    }
}
