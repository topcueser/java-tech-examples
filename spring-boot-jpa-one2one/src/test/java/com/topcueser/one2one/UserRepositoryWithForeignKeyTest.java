package com.topcueser.one2one;


import com.topcueser.one2one.withforeignkey.Address;
import com.topcueser.one2one.withforeignkey.User;
import com.topcueser.one2one.withforeignkey.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryWithForeignKeyTest {

   @Autowired
   UserRepository userRepository;

   @Test
   public void testAddUser() {

       User user = new User();
       user.setUsername("John");

      Address address = new Address();
      address.setStreet("Cumhuriyet Mah.");
      address.setCity("Istanbul");

      user.setAddress(address);
      address.setUser(user);

       User savedUser = userRepository.save(user);
       assertThat(savedUser).isNotNull();
   }


}
