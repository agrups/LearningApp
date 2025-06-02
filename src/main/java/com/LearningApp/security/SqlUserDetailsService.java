package com.LearningApp.security;

import com.LearningApp.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("SqlUserDetailsService")
public class SqlUserDetailsService implements UserDetailsService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var optionalUser = personRepository.findByEmail(username);
        System.out.println("vienas: " + optionalUser.isPresent());
        if(optionalUser.isPresent()) {
            System.out.println("antras: " + optionalUser.get());
            return optionalUser.get();
        }
        throw new UsernameNotFoundException("Invalid user with username: "+ username);
    }
}