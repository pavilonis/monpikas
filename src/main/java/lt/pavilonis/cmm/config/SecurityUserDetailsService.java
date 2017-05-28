package lt.pavilonis.cmm.config;

import lt.pavilonis.cmm.canteen.domain.SecurityUser;
import lt.pavilonis.cmm.security.service.SecurityUserRepository;
import lt.pavilonis.cmm.security.ui.SecurityUserFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

   @Autowired
   private SecurityUserRepository securityUserRepository;

   @Override
   public UserDetails loadUserByUsername(String username) {

      SecurityUserFilter filter = new SecurityUserFilter(null, username, null);

      List<SecurityUser> result = securityUserRepository.load(filter);

      if (result.size() > 1) {
         throw new IllegalStateException("Duplicate usernames?");

      } else if (result.size() == 1) {

         return result.get(0);
      }

      return null;
   }
}
