package meeting.record.restapi.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
public class WelcomeController {


    @GetMapping(value={"/", "/home"})
    public @ResponseBody String welcome(){
        return "Welcome to use my rest api.";
    }
}
