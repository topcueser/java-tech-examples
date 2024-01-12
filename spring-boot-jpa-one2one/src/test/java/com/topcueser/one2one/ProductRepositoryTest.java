package com.topcueser.one2one;

import com.topcueser.one2one.sharedprimarykey.Product;
import com.topcueser.one2one.sharedprimarykey.ProductDetail;
import com.topcueser.one2one.sharedprimarykey.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void testAddProduct() {

        ProductDetail detail = new ProductDetail();
        detail.setDescription("iPhone 14 pro");
        detail.setHeight(0.5f);
        detail.setLength(0.5f);
        detail.setWeight(0.5f);
        detail.setWidth(0.5f);

        Product product = new Product();
        product.setName("iPhone");
        product.setPrice(1000);

        product.setDetail(detail);
        detail.setProduct(product);

        Product savedProduct = productRepository.save(product);
        assertThat(savedProduct).isNotNull();
    }
}
