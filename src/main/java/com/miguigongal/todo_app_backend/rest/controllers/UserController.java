package com.miguigongal.todo_app_backend.rest.controllers;

import static com.miguigongal.todo_app_backend.rest.dtos.UserConversor.toAuthenticatedUserDto;
import static com.miguigongal.todo_app_backend.rest.dtos.UserConversor.toUser;
import static com.miguigongal.todo_app_backend.rest.dtos.UserConversor.toUserDto;

import java.net.URI;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.miguigongal.todo_app_backend.model.domain.User;
import com.miguigongal.todo_app_backend.model.exceptions.DuplicateInstanceException;
import com.miguigongal.todo_app_backend.model.exceptions.IncorrectLoginExcecption;
import com.miguigongal.todo_app_backend.model.exceptions.IncorrectPasswordException;
import com.miguigongal.todo_app_backend.model.exceptions.InstanceNotFoundException;
import com.miguigongal.todo_app_backend.model.exceptions.PermissionException;
import com.miguigongal.todo_app_backend.model.services.UserService;
import com.miguigongal.todo_app_backend.rest.common.ErrorsDto;
import com.miguigongal.todo_app_backend.rest.common.JwtGenerator;
import com.miguigongal.todo_app_backend.rest.common.JwtInfo;
import com.miguigongal.todo_app_backend.rest.dtos.AuthenticatedUserDto;
import com.miguigongal.todo_app_backend.rest.dtos.ChangePasswordParamsDto;
import com.miguigongal.todo_app_backend.rest.dtos.LoginParamsDto;
import com.miguigongal.todo_app_backend.rest.dtos.UserDto;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
	private JwtGenerator jwtGenerator;
	
	@Autowired
	private UserService userService;

	@ExceptionHandler(IncorrectLoginExcecption.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleIncorrectLoginException(IncorrectLoginExcecption exception) {
		
		return new ErrorsDto("INCORRECT_LOGIN", exception.getMessage());

	}

	@ExceptionHandler(IncorrectPasswordException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ErrorsDto handleIncorrectPasswordException(IncorrectPasswordException exception) {
		
		return new ErrorsDto("INCORRECT_PASSWORD",exception.getMessage());

	}

	@PostMapping("/signUp")
	public ResponseEntity<AuthenticatedUserDto> signUp(
		@Validated({UserDto.AllValidations.class}) @RequestBody UserDto userDto) throws DuplicateInstanceException {
		
		User user = toUser(userDto);
		
		userService.signUp(user);
		
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest().path("/{id}")
			.buildAndExpand(user.getId()).toUri();
	
		return ResponseEntity.created(location).body(toAuthenticatedUserDto(generateServiceToken(user), user));

	}
	
	@PostMapping("/login")
	public AuthenticatedUserDto login(@Validated @RequestBody LoginParamsDto params)
		throws IncorrectLoginExcecption {
		
		User user = userService.login(params.getUserName(), params.getPassword());
			
		return toAuthenticatedUserDto(generateServiceToken(user), user);
		
	}
	
	@PostMapping("/loginFromServiceToken")
	public AuthenticatedUserDto loginFromServiceToken(@RequestAttribute Long userId, 
		@RequestAttribute String serviceToken) throws InstanceNotFoundException {
		
		User user = userService.loginFromId(userId);
		
		return toAuthenticatedUserDto(serviceToken, user);
		
	}

	@PutMapping("/{id}")
	public UserDto updateProfile(@RequestAttribute Long userId, @PathVariable Long id,
		@Validated({UserDto.UpdateValidations.class}) @RequestBody UserDto userDto) 
		throws InstanceNotFoundException, PermissionException {
				
		if (!id.equals(userId)) {
			throw new PermissionException();
		}
		
		return toUserDto(userService.updateProfile(id, userDto.getFirstName(), userDto.getLastName(),
			userDto.getEmail()));
		
	}
	
	@PostMapping("/{id}/changePassword")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void changePassword(@RequestAttribute Long userId, @PathVariable Long id,
		@Validated @RequestBody ChangePasswordParamsDto params)
		throws PermissionException, InstanceNotFoundException, IncorrectPasswordException {
		
		if (!id.equals(userId)) {
			throw new PermissionException();
		}
		
		userService.changePassword(id, params.getOldPassword(), params.getNewPassword());
		
	}
	
	private String generateServiceToken(User user) {
		
		JwtInfo jwtInfo = new JwtInfo(user.getId(), user.getUserName());
		
		return jwtGenerator.generate(jwtInfo);
		
	}

}
