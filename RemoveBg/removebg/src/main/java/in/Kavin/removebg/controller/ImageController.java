package in.Kavin.removebg.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import in.Kavin.removebg.dto.UserDTO;
import in.Kavin.removebg.response.RemoveBgResponse;
import in.Kavin.removebg.service.RemoveBackgroundService;
import in.Kavin.removebg.service.UserService;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final RemoveBackgroundService removeBackgroundService;
    private final UserService userService;

    public ImageController(RemoveBackgroundService removeBackgroundService, UserService userService) {
        this.removeBackgroundService = removeBackgroundService;
        this.userService = userService;
    }

    @PostMapping("/remove-background")
    public ResponseEntity<?> removeBackground(@RequestParam("file") MultipartFile file,
                                              Authentication authentication) {
        Map<String, Object> responseMap = new HashMap<>();

        try {
            // ✅ Check if user is authenticated
            if (authentication == null || authentication.getName() == null || authentication.getName().isEmpty()) {
                return new ResponseEntity<>(
                        RemoveBgResponse.builder()
                                .statusCode(HttpStatus.FORBIDDEN)
                                .success(false)
                                .data("User does not have permission/access to this resource")
                                .build(),
                        HttpStatus.FORBIDDEN
                );
            }

            // ✅ Log request info
            System.out.println("🔐 Authenticated user: " + authentication.getName());
            System.out.println("📷 Received file: " + file.getOriginalFilename() + ", size: " + file.getSize());

            // ✅ Validate file
            if (file.isEmpty()) {
                return new ResponseEntity<>(
                        RemoveBgResponse.builder()
                                .statusCode(HttpStatus.BAD_REQUEST)
                                .success(false)
                                .data("Uploaded file is empty")
                                .build(),
                        HttpStatus.BAD_REQUEST
                );
            }

            // ✅ Get user and check credits
            UserDTO userDTO = userService.getUserByClerkId(authentication.getName());

            if (userDTO.getCredits() == 0) {
                responseMap.put("message", "No credit balance");
                responseMap.put("creditBalance", userDTO.getCredits());

                return ResponseEntity.ok(
                        RemoveBgResponse.builder()
                                .success(false)
                                .data(responseMap)
                                .statusCode(HttpStatus.OK)
                                .build()
                );
            }

            // ✅ Remove background
            byte[] imageBytes = removeBackgroundService.removeBackground(file);
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // ✅ Deduct credits
            userDTO.setCredits(userDTO.getCredits() - 1);
            userService.saveUser(userDTO);

            System.out.println("✅ Background removed successfully. Credits left: " + userDTO.getCredits());

            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                    .body(base64Image);

        } catch (Exception e) {
            System.err.println("❌ Internal Server Error in ImageController: " + e.getMessage());
            e.printStackTrace();

            return new ResponseEntity<>(
                    RemoveBgResponse.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                            .success(false)
                            .data("Internal error: " + e.getMessage()) // for dev; make generic in prod
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
