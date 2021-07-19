package lt.pavilonis.cmm.config;

import lt.pavilonis.cmm.security.SystemUser;
import lt.pavilonis.cmm.security.SystemUserRepository;
import lt.pavilonis.cmm.security.ui.SystemUserFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemUserDetailsService implements UserDetailsService {

   private final SystemUserRepository systemUserRepository;

   public SystemUserDetailsService(SystemUserRepository systemUserRepository) {
      this.systemUserRepository = systemUserRepository;
   }

   @Override
   public UserDetails loadUserByUsername(String username) {

      var filter = new SystemUserFilter(null, username, null);
      List<SystemUser> result = systemUserRepository.load(filter);

      if (result.size() > 1) {
         throw new IllegalStateException("Duplicate usernames?");

      } else if (result.size() == 1) {

         return result.get(0);
      }

      return null;
   }
}
