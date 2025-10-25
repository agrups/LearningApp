package com.LearningApp.security;

import com.LearningApp.repository.PersonRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("SqlUserDetailsService")
public class SqlUserDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    public SqlUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var optionalUser = personRepository.findByEmail(username);
        if(optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UsernameNotFoundException("Invalid user with username: "+ username);
    }
}