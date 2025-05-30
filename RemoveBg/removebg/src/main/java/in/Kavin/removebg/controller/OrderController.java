package in.Kavin.removebg.controller;

import org.springframework.security.core.Authentication;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.razorpay.Order;
import com.razorpay.RazorpayException;

import in.Kavin.removebg.dto.RazorpayOrderDTO;
import in.Kavin.removebg.response.RemoveBgResponse;
import in.Kavin.removebg.service.OrderService;
import in.Kavin.removebg.service.RazorpayService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final RazorpayService razorpayService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Map<String, String> request, Authentication authentication) {
        RemoveBgResponse response;

        String planId = request.get("planId");
        System.out.println("🟡 Received planId: " + planId);

        if (planId == null || planId.trim().isEmpty()) {
            response = RemoveBgResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .data("Plan ID is required")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        if (authentication == null || authentication.getName() == null || authentication.getName().isEmpty()) {
            response = RemoveBgResponse.builder()
                    .statusCode(HttpStatus.FORBIDDEN)
                    .success(false)
                    .data("User does not have permission/access to this resource")
                    .build();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            String userId = authentication.getName();
            planId = planId.trim(); // Normalize input

            System.out.println("✅ Creating order for user: " + userId + " with planId: " + planId);

            Order order = orderService.createOrder(planId, userId);

            System.out.println("✅ Order created successfully: " + order.toString());

            RazorpayOrderDTO responseDTO = convertToDTO(order);

            response = RemoveBgResponse.builder()
                    .success(true)
                    .data(responseDTO)
                    .statusCode(HttpStatus.CREATED)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            System.err.println("❌ Invalid plan ID: " + e.getMessage());
            response = RemoveBgResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .data(e.getMessage())
                    .success(false)
                    .build();
            return ResponseEntity.badRequest().body(response);

        } catch (RazorpayException e) {
            System.err.println("❌ Razorpay error:");
            e.printStackTrace();
            response = RemoveBgResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data("Razorpay order creation failed: " + e.getMessage())
                    .success(false)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (Exception e) {
            System.err.println("❌ General error:");
            e.printStackTrace();
            response = RemoveBgResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data("Unexpected error: " + e.getMessage())
                    .success(false)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOrder(@RequestBody Map<String, Object> request) {
        try {
            String razorpayOrderId = request.get("razorpay_order_id").toString();

            Map<String, Object> result = razorpayService.verifyPayment(razorpayOrderId);

            return ResponseEntity.ok(result);

        } catch (RazorpayException e) {
            System.err.println("❌ Payment verification error:");
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Verification failed: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            System.err.println("❌ Unknown verification error:");
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Unknown error occurred");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

  private RazorpayOrderDTO convertToDTO(Order order) {
    return RazorpayOrderDTO.builder()
        .id(order.get("id").toString())
        .amount(order.get("amount").toString()) // convert Integer → String
        .currency(order.get("currency").toString())
        .status(order.get("status").toString())
        .created_at(order.get("created_at").toString()) // Unix timestamp → String
        .receipt(order.get("receipt").toString())
        .build();
}

    }

