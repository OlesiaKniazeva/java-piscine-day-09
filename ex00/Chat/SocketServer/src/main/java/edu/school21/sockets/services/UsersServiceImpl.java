package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("UsersService")
public class UsersServiceImpl implements UsersService {
    UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean signUp(String login, String password) {
       Optional<User> user = usersRepository.findByLogin(login);
       if (user.isPresent()) {
           return false;
       }
       String hashedPassword = passwordEncoder.encode(password);
       usersRepository.save(new User(null, login, hashedPassword));
       return true;
    }

    @Override
    public User findById(Long id) {
        return usersRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public void save(User entity) {
        usersRepository.save(entity);
    }

    @Override
    public void update(User entity) {
        usersRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        usersRepository.delete(id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return usersRepository.findByLogin(login);
    }
}
