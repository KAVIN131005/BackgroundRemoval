package in.Kavin.removebg.controller;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.Kavin.removebg.dto.UserDTO;
import in.Kavin.removebg.response.RemoveBgResponse;
import in.Kavin.removebg.service.UserService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createOrUpdateUser(@RequestBody UserDTO userDTO, @Nonnull Authentication authentication) {
        RemoveBgResponse response;

        try {
            if (!authentication.getName().equals(userDTO.getClerkId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    RemoveBgResponse.builder()
                        .success(false)
                        .data("User does not have permission to access this resource.")
                        .statusCode(HttpStatus.FORBIDDEN)
                        .build()
                );
            }

            UserDTO user = userService.saveUser(userDTO);
            response = RemoveBgResponse.builder()
                    .success(true)
                    .data(user)
                    .statusCode(HttpStatus.OK)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                RemoveBgResponse.builder()
                    .success(false)
                    .data("Something went wrong: " + e.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build()
            );
        }
    }

    @GetMapping("/credits")
    public ResponseEntity<?> getUserCredits(@Nonnull Authentication authentication) {
        try {
            String clerkId = authentication.getName();
            if (clerkId == null || clerkId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    RemoveBgResponse.builder()
                        .statusCode(HttpStatus.FORBIDDEN)
                        .data("User is not authenticated or missing Clerk ID")
                        .success(false)
                        .build()
                );
            }

            UserDTO user;
            try {
                user = userService.getUserByClerkId(clerkId);
            } catch (UsernameNotFoundException ex) {
                // Create new user if not exists
                user = new UserDTO();
                user.setClerkId(clerkId);
                user.setFirstName("New");
                user.setLastName("User");
                user.setEmail(clerkId + "@default.com");
                user.setPhotoUrl("");
                user.setCredits(5); // Initial credits

                user = userService.saveUser(user);
            }

            Map<String, Integer> data = new HashMap<>();
            data.put("credits", user.getCredits());

            return ResponseEntity.ok(
                RemoveBgResponse.builder()
                    .statusCode(HttpStatus.OK)
                    .data(data)
                    .success(true)
                    .build()
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                RemoveBgResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data("Something went wrong: " + e.getMessage())
                    .success(false)
                    .build()
            );
        }
    }

    // Optional: Separate endpoint to add credits
    @PostMapping("/credits/add")
    public ResponseEntity<?> addCredits(@RequestParam int amount, @Nonnull Authentication authentication) {
        try {
            String clerkId = authentication.getName();
            UserDTO user = userService.getUserByClerkId(clerkId);
            user.setCredits(user.getCredits() + amount);
            user = userService.saveUser(user);

            Map<String, Integer> data = new HashMap<>();
            data.put("credits", user.getCredits());

            return ResponseEntity.ok(
                RemoveBgResponse.builder()
                    .statusCode(HttpStatus.OK)
                    .data(data)
                    .success(true)
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                RemoveBgResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data("Failed to add credits: " + e.getMessage())
                    .success(false)
                    .build()
            );
        }
    }
}
