package ouhk.comps380f.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouhk.comps380f.dao.TicketUserRepository;
import ouhk.comps380f.exception.TicketUserNotFound;
import ouhk.comps380f.model.TicketUser;
import ouhk.comps380f.model.UserRole;

@Service
public class TicketUserService implements UserDetailsService {

    @Resource
    TicketUserRepository ticketUserRepo;
    
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        TicketUser ticketUser = ticketUserRepo.findById(username).orElse(null);
        if (ticketUser == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found.");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : ticketUser.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return new User(ticketUser.getUsername(), ticketUser.getPassword(), authorities);
    }
    
    @Transactional(rollbackFor = TicketUserNotFound.class)
    public void updateUser(String username, String newUsername,
                String newPassword, String[] roles)
            throws IOException, TicketUserNotFound {
        TicketUser updatedUser = ticketUserRepo.findById(username).orElse(null);
        if (updatedUser == null) {
            throw new TicketUserNotFound();
        }
        updatedUser.setUsername(newUsername);
        updatedUser.setPassword(newPassword);
        List<UserRole> userRoles = new ArrayList<>();
        for (String userRole : roles) {
            UserRole newUserRole = new UserRole();
            newUserRole.setUser(updatedUser);
            newUserRole.setRole(userRole);
            if (newUserRole != null && roles.length > 0) {
                userRoles.add(newUserRole);
            }
        }
        updatedUser.setRoles(userRoles);
        ticketUserRepo.save(updatedUser);
    }
}
