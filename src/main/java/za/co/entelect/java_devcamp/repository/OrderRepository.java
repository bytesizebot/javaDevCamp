package za.co.entelect.java_devcamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.entelect.java_devcamp.entity.Order;
import za.co.entelect.java_devcamp.entity.Status;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByCustomerIdAndOrderStatus(Long customerId, Status status);

}
