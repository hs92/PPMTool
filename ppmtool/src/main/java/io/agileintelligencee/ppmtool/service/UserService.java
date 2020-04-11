package io.agileintelligencee.ppmtool.service;

import io.agileintelligencee.ppmtool.exceptions.UserNameAlreadyExistsException;
import io.agileintelligencee.ppmtool.model.User;
import io.agileintelligencee.ppmtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User user) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setConfirmPassword("");
            return userRepository.save(user);
        } catch (Exception e) {
            throw new UserNameAlreadyExistsException("Username- " + user.getUsername() + " already exists!");
        }
    }
}
