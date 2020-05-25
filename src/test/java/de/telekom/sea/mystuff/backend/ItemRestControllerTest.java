package de.telekom.sea.mystuff.backend;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ItemRestControllerTest {

	private static final String BASE_PATH = "/api/v1/items";

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ItemRepo repo;

	@BeforeEach
	void setupRepo() {
		repo.deleteAll();
	}

	@Test
	void shouldBeAbleToUploadAnItem() {
		// Given | Arrange
		Item lawnMower = buildLawnMower();
		// When | Act
		ResponseEntity<Item> response = restTemplate.postForEntity(BASE_PATH, lawnMower, Item.class);
		lawnMower.setId(response.getBody().getId());
		// Then | Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isEqualToComparingFieldByField(lawnMower);
	}

	@Test
	void shouldReadAllItems() {
		// Given | Arrange
		Item lawnMower = givenAnInsertedItem().getBody();
		Item lawnTrimmer = givenAnotherInsertedItem().getBody();
		// When | Act
		ResponseEntity<Item[]> response = restTemplate.getForEntity(BASE_PATH, Item[].class);
		// Then | Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().length).isEqualTo(2);
		assertThat(response.getBody()[0]).isEqualToComparingFieldByField(lawnMower);
		assertThat(response.getBody()[1]).isEqualToComparingFieldByField(lawnTrimmer);
	}
	

	@Test
	void shouldFindOneItem() {
		// Given | Arrange
		Item lawnMower = givenAnInsertedItem().getBody();
		// When | Act
		ResponseEntity<Item> response = restTemplate.getForEntity(BASE_PATH + "/" + lawnMower.getId(), Item.class);
		// Then | Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualToComparingFieldByField(lawnMower);
	}

	@Test
	void shouldFindNoItemForUnknownId() throws URISyntaxException {
		// Given | Arrange
		Long unknownId = (long) 4711;
		// When | Act
		ResponseEntity<Item> response = restTemplate.getForEntity(BASE_PATH + "/" + unknownId, Item.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldBeAbleToDeleteAnItem() throws URISyntaxException {
		// Given | Arrange
		Item lawnMower = givenAnInsertedItem().getBody();
		// When | Act
		URI uri = new URI(restTemplate.getRootUri() + BASE_PATH + "/" + lawnMower.getId());
		RequestEntity<String> requestEntity = new RequestEntity<>(HttpMethod.DELETE, uri);
		ResponseEntity<String> deleteResponse = restTemplate.exchange(requestEntity, String.class);
		// Then | Assert
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		ResponseEntity<Item> getResponse = restTemplate.getForEntity(uri, Item.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	
	@Test
	void shouldNotBeAbleToDeleteAnItemWithUnknownId() throws URISyntaxException {
		// Given | Arrange
		Long unknownId = (long) 4711;
		// When | Act
		URI uri = new URI(restTemplate.getRootUri() + BASE_PATH + "/" + unknownId);
		RequestEntity<String> requestEntity = new RequestEntity<>(HttpMethod.DELETE, uri);
		ResponseEntity<String> deleteResponse = restTemplate.exchange(requestEntity, String.class);
		// Then | Assert
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	
	@Test
	void shouldBeAbleToReplaceAnItem() throws URISyntaxException {
		// Given | Arrange
		Item lawnMower = givenAnInsertedItem().getBody();
		Item lawnTrimmer = buildLawnTrimmer();
		// When | Act
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		URI uri = new URI(restTemplate.getRootUri() + BASE_PATH + "/" + lawnMower.getId());
		HttpEntity<Item> requestUpdate = new HttpEntity<Item>(lawnTrimmer, headers);
		ResponseEntity<Item> response = restTemplate.exchange(uri, HttpMethod.PUT, requestUpdate, Item.class);
		lawnTrimmer.setId(lawnMower.getId());
		// Then | Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualToComparingFieldByField(lawnTrimmer);
	}

	@Test
	void shouldNotBeAbleToReplaceAnItemWithUnknownId() throws URISyntaxException {
		// Given | Arrange
		Item lawnTrimmer = buildLawnTrimmer();
		Long unknownId = (long) 4711;
		// When | Act
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		URI uri = new URI(restTemplate.getRootUri() + BASE_PATH + "/" + unknownId);
		HttpEntity<Item> requestUpdate = new HttpEntity<Item>(lawnTrimmer, headers);
		ResponseEntity<Item> response = restTemplate.exchange(uri, HttpMethod.PUT, requestUpdate, Item.class);
		// Then | Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<Item> givenAnInsertedItem() {
		Item item = buildLawnMower();
		return restTemplate.postForEntity(BASE_PATH, item, Item.class);
	}

	private ResponseEntity<Item> givenAnotherInsertedItem() {
		Item item = buildLawnTrimmer();
		return restTemplate.postForEntity(BASE_PATH, item, Item.class);
	}

	private Item buildLawnMower() {
		Item item = Item.builder().name("Lawn mower").amount(1).lastUsed(LocalDate.parse("2019-05-01")) // Date.valueOf("2019-05-01")
				.location("Basement").build();
		return item;
	}

	private Item buildLawnTrimmer() {
		Item item = Item.builder().name("Lawn trimmer").amount(1).lastUsed(LocalDate.parse("2019-05-01"))
				.location("Basement").build();
		return item;
	}

}
