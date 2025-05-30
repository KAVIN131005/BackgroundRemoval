package in.Kavin.removebg.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayException;

import in.Kavin.removebg.entity.OrderEntity;
import in.Kavin.removebg.repository.OrderRepository;
import in.Kavin.removebg.service.OrderService;
import in.Kavin.removebg.service.RazorpayService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final RazorpayService razorpayService;
    private final OrderRepository orderRepository;

    // Static map holding available plan details
    private static final Map<String, PlanDetails> PLAN_DETAILS = Map.of(
        "Basic", new PlanDetails("Basic", 100, 499.00),
        "Premium", new PlanDetails("Premium", 250, 899.00),
        "Ultimate", new PlanDetails("Ultimate", 1000, 1499.00)
    );

    // Java 16+ record to hold plan information
    private record PlanDetails(String name, int credits, double amount) {}

    @Override
    public Order createOrder(String planId, String clerkId) throws RazorpayException {
        PlanDetails details = PLAN_DETAILS.get(planId);

        if (details == null) {
            throw new IllegalArgumentException("Invalid planId: " + planId);
        }

        try {
            // Razorpay expects the amount in paise and as an integer (not double)
            int amountInPaise = (int) (details.amount() * 100);
            System.out.println("🧾 Amount (paise): " + amountInPaise);

            Order razorpayOrder = razorpayService.createOrder(amountInPaise, "INR");

            OrderEntity newOrder = OrderEntity.builder()
                .clerkId(clerkId)
                .plan(details.name())
                .credits(details.credits())
                .amount(details.amount()) // store original rupee amount in DB
                .orderId(razorpayOrder.get("id"))
                .build();

            orderRepository.save(newOrder);

            return razorpayOrder;

        } catch (RazorpayException e) {
            System.err.println("❌ Razorpay Exception: " + e.getMessage());
            throw e; // rethrowing for controller to handle
        }
    }
}
