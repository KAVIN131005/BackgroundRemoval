package in.Kavin.removebg.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.Kavin.removebg.entity.UserEntity;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

   boolean existsByClerkId(String clerkId);

   Optional<UserEntity> findByClerkId(String clerkId);

}