package in.Kavin.removebg.service.impl;
import in.Kavin.removebg.service.UserService;

import java.util.Optional;
import org.springframework.stereotype.Service;

import in.Kavin.removebg.dto.UserDTO;
import in.Kavin.removebg.entity.UserEntity;
import in.Kavin.removebg.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        Optional<UserEntity> optionalUser = userRepository.findByClerkId(userDTO.getClerkId());
        if (optionalUser.isPresent()) {
            UserEntity existingUser = optionalUser.get();
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setFirstName(userDTO.getFirstName());
            existingUser.setLastName(userDTO.getLastName());
            existingUser.setPhotoUrl(userDTO.getPhotoUrl());
            if (userDTO.getCredits() != null) {
                existingUser.setCredits(userDTO.getCredits());
            }
            existingUser = userRepository.save(existingUser);
            return mapToDTO(existingUser);
        }

        UserEntity newUser = mapToEntity(userDTO);
        userRepository.save(newUser);
        return mapToDTO(newUser);
    }

    private UserDTO mapToDTO(UserEntity newUser) {
        return UserDTO.builder()
            .clerkId(newUser.getClerkId())
            .credits(newUser.getCredits())
            .email(newUser.getEmail())
            .firstName(newUser.getFirstName())
            .lastName(newUser.getLastName())
            .photoUrl(newUser.getPhotoUrl())
            .build();
    }

    private UserEntity mapToEntity(UserDTO userDTO) {
        return UserEntity.builder()
            .clerkId(userDTO.getClerkId())
            .credits(userDTO.getCredits())
            .email(userDTO.getEmail())
            .firstName(userDTO.getFirstName())
            .lastName(userDTO.getLastName())
            .photoUrl(userDTO.getPhotoUrl())
            .build();
    }
}
