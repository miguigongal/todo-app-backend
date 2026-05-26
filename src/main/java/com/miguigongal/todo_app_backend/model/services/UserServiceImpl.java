package com.miguigongal.todo_app_backend.model.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miguigongal.todo_app_backend.model.domain.User;
import com.miguigongal.todo_app_backend.model.domain.UserDao;
import com.miguigongal.todo_app_backend.model.exceptions.DuplicateInstanceException;
import com.miguigongal.todo_app_backend.model.exceptions.IncorrectLoginExcecption;
import com.miguigongal.todo_app_backend.model.exceptions.IncorrectPasswordException;
import com.miguigongal.todo_app_backend.model.exceptions.InstanceNotFoundException;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private PermissionChecker permissionChecker;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDao userDao;

	@Override
	public void signUp(User user) throws DuplicateInstanceException {
		
		if (userDao.existsByUserName(user.getUserName())) {
			throw new DuplicateInstanceException("project.entities.user", user.getUserName());
		}
			
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(User.RoleType.USER);
		
		userDao.save(user);
		
	}

	@Override
	@Transactional(readOnly=true)
	public User login(String userName, String password) throws IncorrectLoginExcecption {
		
		Optional<User> user = userDao.findByUserName(userName);
		
		if (!user.isPresent()) {
			throw new IncorrectLoginExcecption(userName, password);
		}
		
		if (!passwordEncoder.matches(password, user.get().getPassword())) {
			throw new IncorrectLoginExcecption(userName, password);
		}
		
		return user.get();
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public User loginFromId(Long id) throws InstanceNotFoundException {
		return permissionChecker.checkUser(id);
	}

	@Override
	public User updateProfile(Long id, String firstName, String lastName, String email) throws InstanceNotFoundException {
		
		User user = permissionChecker.checkUser(id);
		
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		
		return user;

	}

	@Override
	public void changePassword(Long id, String oldPassword, String newPassword)
		throws InstanceNotFoundException, IncorrectPasswordException {
		
		User user = permissionChecker.checkUser(id);
		
		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new IncorrectPasswordException();
		} else {
			user.setPassword(passwordEncoder.encode(newPassword));
		}
		
	}

}
