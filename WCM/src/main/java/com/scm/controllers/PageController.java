package com.scm.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;




@Controller
public class PageController {


    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    
    @RequestMapping("/home")
    public String home(Model model)
    {
        System.out.println("Inside home method");
        model.addAttribute("name", "Spring Boot");
        model.addAttribute("youtubeChannel", "CodeJava");
         return "home";
    }

     // about  route
     @RequestMapping("/about")
     public String about(Model model)
     {
         System.out.println("Inside about method");
         return "about";
     }

     //services
     @RequestMapping("/services")
     public String services(Model model)
     {
         System.out.println("Inside services method");
         return "services";
     }

      //contact
      @GetMapping("/contact")
      public String contact()
      {
          return new String("contact");
      }

      //login 
      //this is login viewpage
      @GetMapping("/login")
      public String login()
      {
          return new String("login");
      }

      //register
      // Registration view 
      @GetMapping("/register")
      public String register(Model model)
      {
        UserForm userForm = new UserForm();
        // default data bhi daal skte hai
        // userForms.setName("Ravi");
        // userForms.setEmail("ravi@scm");
        // userForms.setPassword("1234");
        // userForms.setAbout("I am a software developer");
        // userForms.setPhoneNumber("1234567890");
        model.addAttribute("userForm", userForm);

          return "register";
      }




      
      //processing resquest 
      @RequestMapping(value = "/do-register",method = RequestMethod.POST)
      public String processRegister(@Valid  @ModelAttribute  UserForm userForm,BindingResult rBindingResult,  HttpSession session)
      {
        System.out.println("Processing registeration");
        // fetch form data
        // Userform
        System.out.println(userForm);
        // validate form data 
         if(rBindingResult.hasErrors())
         {
            return "register";
         }

        // save to database
        //userservice
         
        //UserForm--> User
        //   User user = User.builder()
        //    .name(userForm.getName())
        //    .email(userForm.getEmail())
        //     .password(userForm.getPassword())
        //     .about(userForm.getAbout()) 
        //     .phoneNumber(userForm.getPhoneNumber())
        //     .profilePic("https://i.pinimg.com/736x/f1/0f/f7/f10ff70a7155e5ab666bcdd1b45b726d.jpg") // default profile pic
        //    .build();
               
        User user = new User();
        
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false); // user is enabled by default
        user.setProfilePic("https://i.pinimg.com/736x/f1/0f/f7/f10ff70a7155e5ab666bcdd1b45b726d.jpg"); // default profile pic
         
        User savedUser =  userService.saveUser(user);

        System.out.println("User saved: " );


        // message = "Registration successful"

        //add the message 
        Message message = Message.builder().content("Registration successful").type(MessageType.green).build();
        
        session.setAttribute("message", message);

        // redirect login page 
        return "redirect:/register";
      }

}
 