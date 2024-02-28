package swtp12.modulecrediting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import swtp12.modulecrediting.model.Role;
import swtp12.modulecrediting.model.User;
import swtp12.modulecrediting.repository.RoleRepository;
import swtp12.modulecrediting.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private TestDataLoader testDataLoader;
    @Value("${app.config.data.adminUsername}")
    private String adminUsername;
    @Value("${app.config.data.adminPassword}")
    private String adminPassword;
    @Value("${app.config.data.loadTestData}")
    private String loadTestData;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("--- Dataloader: Starting ---");

        roleCreation();

        adminCreation();

        if (loadTestData.equals("true")) {
            System.out.println();
            System.out.println("--- Dataloader: Starting to load testdata ---");
            testDataLoader.run();
            System.out.println();
            System.out.println("--- Dataloader: Finished loading testdata ---");
        }
        System.out.println();
        System.out.println("--- Dataloader: Finished ---");
    }



    private void roleCreation() {
        System.out.println("Checking for Roles");

        List<String> namesDB = new ArrayList<>();
        List<String> names = namesList();
        List<Role> rolesDB = roleRepository.findAll();

        if (!rolesDB.isEmpty()) {
            for (Role roleDB : rolesDB) { 
                namesDB.add(roleDB.getRoleName()); 
            }
            names.removeAll(namesDB);
            if (!names.isEmpty()) {
                System.out.println("Roles are missing - Creating new Roles");
                createGivenRoles(names);
            }
            else System.out.println("All necessary Roles exist");
        }
        else {
            System.out.println("No Roles found - Creating new Roles");
            createGivenRoles(names);
        }
    }

    private List<String> namesList() {
        List<String> names = new ArrayList<>();
        names.add("ROLE_ADMIN");
        names.add("ROLE_STUDY");
        names.add("ROLE_CHAIR");
        return names;
    }

    private void createGivenRoles(List<String> names) {
        for (String name : names) {
            System.out.println("Creating " + name);
            Role role = new Role(name);
            roleRepository.save(role);
        }
    }


    private void adminCreation() {
        System.out.println("Checking for Admin user");
        List<User> usersDB = userRepository.findAll();

        Boolean adminExists = false;

        if (!usersDB.isEmpty()) {
            for (User userDB : usersDB) {
                if (userDB.getRole().getRoleName().equals("ROLE_ADMIN")) {
                    adminExists = true;
                }
            }
        }

        Role role = null;
        Optional<Role> roleCandidate = roleRepository.findByRoleName("ROLE_ADMIN");
        if (!roleCandidate.isPresent()) {
            System.out.println("There seems to be no ROLE_ADMIN in the database");
        } 
        else {
            role = roleCandidate.get();
        }

        if (adminExists == false) {
            User adminUser = new User(adminUsername, encoder.encode(adminPassword), true);
            if (role != null) {
                adminUser.setRole(role);
                userRepository.save(adminUser);
                System.out.println("Admin user created");
            }
            else {
                System.out.println("There seems to be no ROLE_ADMIN in the database");
            }
        }
        else {
            System.out.println("Admin user already exists");
        }
    }
}