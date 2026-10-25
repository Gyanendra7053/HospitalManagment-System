package user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import user.dto.ResponseDto;
import user.dto.UserDto;
import user.exception.HmsExcpetion;
import user.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;

    }

    // Register User
    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerUser(@RequestBody @Valid UserDto userDto)
            throws HmsExcpetion {
        userService.registerUser((userDto));
        return new ResponseEntity<>(new ResponseDto("Account Created"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> postMethodName(@RequestBody UserDto userDto) throws HmsExcpetion {
        return new ResponseEntity<>(userService.loginUser(userDto), HttpStatus.OK);

    }

}
