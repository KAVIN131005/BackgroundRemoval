package in.Kavin.removebg.service.impl;

import in.Kavin.removebg.dto.UserDTO;
import in.Kavin.removebg.entity.UserEntity;
import in.Kavin.removebg.repository.UserRepository;
import in.Kavin.removebg.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        Optional<UserEntity> optionalUser = userRepository.findByClerkId(userDTO.getClerkId());

        UserEntity userEntity;

        if (optionalUser.isPresent()) {
            userEntity = optionalUser.get();
            // Update fields
            if (userDTO.getEmail() != null) userEntity.setEmail(userDTO.getEmail());
            if (userDTO.getFirstName() != null) userEntity.setFirstName(userDTO.getFirstName());
            if (userDTO.getLastName() != null) userEntity.setLastName(userDTO.getLastName());
            if (userDTO.getPhotoUrl() != null) userEntity.setPhotoUrl(userDTO.getPhotoUrl());
            if (userDTO.getCredits() != null) userEntity.setCredits(userDTO.getCredits());
        } else {
            // Create new user
            userEntity = UserEntity.builder()
                    .clerkId(userDTO.getClerkId())
                    .email(userDTO.getEmail() != null ? userDTO.getEmail() : "")
                    .firstName(userDTO.getFirstName() != null ? userDTO.getFirstName() : "")
                    .lastName(userDTO.getLastName() != null ? userDTO.getLastName() : "")
                    .photoUrl(userDTO.getPhotoUrl() != null ? userDTO.getPhotoUrl() : "")
                    .credits(userDTO.getCredits() != null ? userDTO.getCredits() : 5)
                    .build();
        }

        userRepository.save(userEntity);
        return mapToDTO(userEntity);
    }

    @Override
    public UserDTO getUserByClerkId(String clerkId) {
        UserEntity userEntity = userRepository.findByClerkId(clerkId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return mapToDTO(userEntity);
    }

    @Override
    public void deleteUserByClerkId(String clerkId) {
        UserEntity userEntity = userRepository.findByClerkId(clerkId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(userEntity);
    }

    private UserDTO mapToDTO(UserEntity userEntity) {
        return UserDTO.builder()
                .clerkId(userEntity.getClerkId())
                .credits(userEntity.getCredits())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .photoUrl(userEntity.getPhotoUrl())
                .build();
    }
}
