package user.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import user.dto.UserDto;
import user.entity.User;
import user.exception.HmsExcpetion;
import user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // register User
    @Override
    public void registerUser(UserDto userDto) throws HmsExcpetion {

        Optional<User> opt = userRepository.findByEmail(userDto.getEmail());
        if (opt.isPresent()) {
            throw new HmsExcpetion("USER_ALREADY_EXISTS");
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(userDto.toEntity());

    }

    // for login User

    @Override
    public UserDto loginUser(UserDto userDto) throws HmsExcpetion {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new HmsExcpetion("USER_NOT_FOUND"));

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new HmsExcpetion("INVALID_CREDENTIALS");
        }
        user.setPassword(null);
        return user.toDTO();

    }

    // get UserBy id;
    @Override
    public UserDto getUserById(Long id) throws HmsExcpetion {
        return userRepository.findById(id).orElseThrow(() -> new HmsExcpetion("USER_NOT_FOUND")).toDTO();
    }

    @Override
    public void updateUser(UserDto userDto) {
        throw new UnsupportedOperationException("Unimplemented method");
    }
}