package com.malle.microone.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class InventoryRepositoryServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
