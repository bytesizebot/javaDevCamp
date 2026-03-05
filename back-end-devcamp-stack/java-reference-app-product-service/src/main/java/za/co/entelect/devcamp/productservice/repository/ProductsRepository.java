package za.co.entelect.devcamp.productservice.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.entelect.devcamp.productservice.model.Products;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Products, Long> {

    Optional<Products> getProductByName(String name);
}
