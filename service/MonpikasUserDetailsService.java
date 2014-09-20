package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class MonpikasUserDetailsService implements UserDetailsService {

   private EntityManager entityManager;

   @PersistenceContext
   public void setEntityManager(EntityManager newEm) {
      this.entityManager = newEm;
   }

   public UserDetails loadUserByUsername(String username) {

      return entityManager.createQuery("from User where username = :username", User.class)
            .setParameter("username", username)
            .getSingleResult();
   }
}
