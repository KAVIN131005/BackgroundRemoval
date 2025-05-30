package in.Kavin.removebg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.Kavin.removebg.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

   

  Optional<OrderEntity> findByOrderId(String orderId);


    
}
