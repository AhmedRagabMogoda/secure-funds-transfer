package com.transferapp.security;

import com.transferapp.entity.User;
import com.transferapp.exception.ResourceNotFoundException;
import com.transferapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
    /**
     * Load user by username or email
     */
	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		log.debug("Loading user by username or email: {}", username);
		
		User user = userRepository.findByUsername(username)
				.orElseThrow( () -> new UsernameNotFoundException("User not found with username or email: " + username));
		
		return UserPrincipal.create(user);
	}
	
    /**
     * Load user by user ID
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long userId) {
        log.debug("Loading user by ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return UserPrincipal.create(user);
    }

}
