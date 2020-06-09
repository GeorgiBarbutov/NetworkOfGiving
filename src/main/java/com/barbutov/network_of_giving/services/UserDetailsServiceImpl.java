package com.barbutov.network_of_giving.services;

import com.barbutov.network_of_giving.data.UserDetailsImpl;
import com.barbutov.network_of_giving.data.contracts.UserRepository;
import com.barbutov.network_of_giving.data.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("Username " + username + " doesn't exist");
        }

        return new UserDetailsImpl(user.get());
    }
}
