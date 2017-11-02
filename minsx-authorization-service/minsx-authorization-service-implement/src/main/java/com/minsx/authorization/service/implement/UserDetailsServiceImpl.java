package com.minsx.authorization.service.implement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.minsx.authorization.entity.Auth;
import com.minsx.authorization.entity.Group;
import com.minsx.authorization.entity.Role;
import com.minsx.authorization.entity.User;
import com.minsx.authorization.repository.UserRepository;

/**
 * UserDetailsServiceImpl
 * created by Joker on 2017年11月1日
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(username);
		Set<GrantedAuthority> authorities = new HashSet<>();
		List<Group> userGroups = user.getGroups();
		userGroups.forEach(userGroup -> {
			List<Role> roles = userGroup.getRoles();
			roles.forEach(role -> {
				List<Auth> auths = role.getAuths();
				auths.forEach(auth -> {
					authorities.add(new SimpleGrantedAuthority(auth.getAuthType()+":"+auth.getAuthValue()));
				});
			});
		});
		user.setAuthorities(authorities);
		return user;
	}
}