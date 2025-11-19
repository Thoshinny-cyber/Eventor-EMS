package com.restapi.dataloader;

import com.restapi.model.AppUser;
import com.restapi.model.Category;
import com.restapi.model.Role;
import com.restapi.repository.CategoryRepository;
import com.restapi.repository.RoleRepository;
import com.restapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

//        Create user roles
        Role userRole = createRoleIfNotFound(Role.USER);
        Role adminRole = createRoleIfNotFound(Role.ADMIN);

//        Create user
        createUserIfNotFound("user", "user", "user@example.com", 1234567890L, "Male", userRole);
        createUserIfNotFound("admin", "admin", "admin@example.com", 9876543210L, "Male", adminRole);

//        Create categories
        createCategoryIfNotFound("Movies & Cinema");
        createCategoryIfNotFound("Concerts & Music Shows");
        createCategoryIfNotFound("Sports & Fitness");
        createCategoryIfNotFound("Theatre & Performing Arts");
        createCategoryIfNotFound("Workshops & Learning");
        createCategoryIfNotFound("Exhibitions & Expos");
        createCategoryIfNotFound("Food & Drink Festivals");
        createCategoryIfNotFound("Parties & Nightlife");
        createCategoryIfNotFound("Spiritual & Wellness");
        createCategoryIfNotFound("Kids, Family & Community Events");

        alreadySetup = true;
    }

    @Transactional
    private Role createRoleIfNotFound(final String username) {
        Role role = roleRepository.findByName(username);
        if (role == null) {
            role = new Role();
            role.setName(username);
            role = roleRepository.save(role);
        }
        return role;
    }

    @Transactional
    private AppUser createUserIfNotFound(final String username, final String password,
                                         final String email, final long phone, final String gender,
                                         final Role role) {
        Optional<AppUser> optionalUser = userRepository.findByUsername(username);
        AppUser user = null;
        if (optionalUser.isEmpty()) {
            user = new AppUser();
            user.setUsername(username);
            user.setName(username);
            user.setEmail(email);
            user.setPhone(phone);
            user.setGender(gender);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setRoles(role);
            user = userRepository.save(user);
        }
        return user;
    }

    @Transactional
    private Category createCategoryIfNotFound(final String categoryName) {
        Optional<Category> optionalCategory = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                .findFirst();
        
        Category category = null;
        if (optionalCategory.isEmpty()) {
            category = new Category();
            category.setName(categoryName);
            category = categoryRepository.save(category);
        } else {
            category = optionalCategory.get();
        }
        return category;
    }
}
