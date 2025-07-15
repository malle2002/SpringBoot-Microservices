package com.malle.microone.order;

import com.malle.microone.order.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

import static org.hamcrest.MatcherAssert.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {
	@ServiceConnection
	static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
			.withDatabaseName("order_service")
			.withUsername("root")
			.withPassword("password")
			.withReuse(true)
			.withEnv("MYSQL_ROOT_PASSWORD", "password");
	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mySQLContainer.start();
	}

	@Test
	void contextLoads() {
		String submitOrderJson = """
				{
				    "skuCode": "baba_ti",
				    "price": "5000000",
				    "quantity": "1"
				}
                """;

		InventoryClientStub.stubInventoryCall("baba_ti", 1);

		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().asString();

		assertThat(responseBodyString, Matchers.is("Order placed Successfully"));
	}

	@Test
	void shouldFailOrderWhenProductIsNotInStock() {
		String submitOrderJson = """
				{
				    "skuCode": "baba_ti",
				    "price": "5000000",
				    "quantity": "1000"
				}
                """;

		InventoryClientStub.stubInventoryCall("baba_ti", 1000);

		RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(500);
	}

}
