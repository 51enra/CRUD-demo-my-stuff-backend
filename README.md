# 1 CRUD-demo-my-stuff-backend

Challenge: Backend mit Java Spring Boot für eine REST-basierte CRUD-Anwendung

## Aufgabe

* Spring Boot-basiertes REST-Backend für "Item"-Resourcen erstellen
* Persistenz: Spring Data JPA / MySQL
* Bereitstellung des Codes in eigenem GitHub-Repo
* Nachweis der Funktion durch Beispiel-Requests (Postman)
* Einfügen von 3 Beispieldatensätzen per DevBootstrap-Klasse (ApplicationListener)
* Zunächst keine Security notwendig

### REST-Endpoints

| Method | Endpoint / example request |           Purpose           |   Response code  |
|:------:|:--------------------------:|:---------------------------:|:----------------:|
| GET    | ```/items```               | Retrieve all item resources |     200 (OK)     |
| GET    | ```/items/4711```          | Retrieve item resource 4711 |     200 (OK)     |
| POST   | ```/items```               | Create a new item resource  |   201 (Created)  |
| PUT    | ```/items/4711```          | Update item resource 4711   |     200 (OK)     |
| DELETE | ```/items/4711```          | Delete item resource 4711   | 204 (No Content) |

### Datenmodell

#### Item
* Long id
* String name
* int amount
* String location
* String description
* Date lastUsed


## Tipps zum Vorgehen

### 1. Neues Spring Starter Projekt in STS anlegen

 * Artifact: my-stuff-backend
 * Group: de.telekom.sea
 * Package: de.telekom.sea.mystuff.backend
 * Maven
 * Java 11
 * Dependendencies: Lombok, MySQL Driver, Spring Boot Actuator, Spring Boot DevTools, Spring Data JPA, Spring Web

### 2. Neues GitHub-Projekt anlegen / verbinden

* Name: my-stuff-backend
* Wichtig: Keine initiale README.md oder .gitignore anlegen (sonst klappt das Verbinden mit dem lokalen Repo nicht)

#### Hint: Lokales Projekt mit GitHub-Projekt verbinden

```bash
git init
# adapt the following line
git remote add origin git@github.com:yourname/projectname.git
git fetch
git branch master origin/master
git checkout master
```

### Hint: .gitignore manuell hinzufügen

```
HELP.md
bin/
target/
!.mvn/wrapper/maven-wrapper.jar
!**/src/main/**
!**/src/test/**
*.log

### STS ###
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr

### NetBeans ###
/nbproject/private/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/
build/
### VS Code ###
.vscode/
/.attach_pid13316
```

### 3. MySQL DB anlegen / in Spring Boot anbinden

#### Hint: DB anlegen

```sql
-- uninstall plugin validate_password;
CREATE DATABASE IF NOT EXISTS my_stuff;
CREATE USER IF NOT EXISTS 'my_stuff'@'localhost' IDENTIFIED BY 'my_stuff';
GRANT ALL PRIVILEGES ON my_stuff.* TO 'my_stuff'@'localhost';
```

#### Hint: application.properties

```
# Database Settings
spring.datasource.url=jdbc:mysql://localhost:3306/my_stuff?serverTimezone=CET
spring.datasource.username=my_stuff
spring.datasource.password=my_stuff

# Table generation: none (default), create, create-drop, validate, update
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### 4. Minimalen(!) ItemRestController anlegen

Ziel: Route: "/api/v1/items" gibt String "items" zurück

Check: http://localhost:8080/api/v1/items aufrufen

Hints:
 * https://www.youtube.com/watch?v=KUl_KQt0ix8&list=PLVBvhDBS_eGWm3_N3sx95lGhBWkxuv_Qn&index=9&t=0s
 * https://www.youtube.com/watch?v=QSF-k7oFGTU&list=PLVBvhDBS_eGWm3_N3sx95lGhBWkxuv_Qn&index=8&t=0s

### 5. Swagger konfigurieren

Ziel: http://localhost:8080/swagger-ui.html liefert UI zum Testen des REST-Endpoints

#### Hint: Anpassung der pom.xml

```xml
  	<properties>
		  <springfox.version>2.9.2</springfox.version>
	  </properties>

		<!-- spring fox -->
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>${springfox.version}</version>
		</dependency>
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>${springfox.version}</version>
		</dependency>
```

#### Hint: Config

```java
package de.telekom.sea.mystuff.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/api/**")).build();
	}

}

```

### 6. Entity-Klasse anlegen (Item.java)

* Check: Wurde die Tabelle in der Datenbank korrekt angelegt?

### 7. ItemRepository anlegen

* Hint: JpaRepository extenden (Interface!)

### 8. Controller mit Repository verdrahten

* Hint: Constructor-Based Dependency Injection
* Hint: findAll mittels Repo implementieren
* Check: In PostMan: Rückgabe leeres Array

### 9. Beispieldaten per DevBootstrap-Klasse einfügen

* Hint: @Component
* Hint: extends ApplicationListener<ContextRefreshedEvent>
* Hint: Einfügen über JPA
* Check: In PostMan: Rückgabe der Testdatensätze

### 10. Verbliebene CRUD-Methoden implementieren

Hint: https://gist.github.com/MrSnyder/436f1e985d4ada774a43486100554cbb/



# 2 Challenge: Integration Tests mit Java Spring Boot für REST-basierte CRUD Anwendung

## Aufgabe

* Integration Tests für alle Endpoints bereitstellen
* Bereitstellung des Codes in eigenem GitHub-Repo

## Links

* [Übersicht zu Test-Varianten für Spring Controller](https://thepracticaldeveloper.com/2017/07/30/guide-spring-boot-controller-tests/)
* [Live-Coding zu MVC-Tests (Februar)](https://www.youtube.com/watch?v=TGcE7lEqhN4&list=PLVBvhDBS_eGWm3_N3sx95lGhBWkxuv_Qn&index=17&t=3513s)
* [Testing OAuth](https://www.baeldung.com/oauth-api-testing-with-spring-mvc)

* [Assertj](https://assertj.github.io/doc/)

### REST-Endpoints

| Method | Endpoint / example request | Purpose                     | Response code                      |
|--------|----------------------------|-----------------------------|------------------------------------|
| GET    | ```/items```               | Retrieve all item resources | 200 (OK)                           |
| GET    | ```/items/4711```          | Retrieve item resource 4711 | 200 (OK) / 404 (NOT FOUND)         |
| POST   | ```/items```               | Create a new item resource  | 201 (Created)                      |
| PUT    | ```/items/4711```          | Update item resource 4711   | 200 (OK) / 404 (NOT FOUND)         |
| DELETE | ```/items/4711```          | Delete item resource 4711   | 204 (No Content) / 404 (NOT FOUND) |

### Grundlegender Aufbau wie im Live-Coding demonstriert

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ItemRestControllerTest {

	private static final String BASE_PATH = "/api/v1/items";

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ItemRepository repo;

	@BeforeEach
	void setupRepo() {
		repo.deleteAll();
	}

	@Test
	void shouldBeAbleToUploadAnItem() {
                fail();
	}
	
	@Test
	void shouldReadAllItems() {
		fail();
	}

	@Test
	void shouldFindOneItem() {
		fail();
	}

	@Test
	void shouldFindNoItemForUnknownId() throws URISyntaxException {
		fail();
	}

	@Test
	void shouldBeAbleToDeleteAnItem() throws URISyntaxException {
		fail();
	}

	@Test
	void shouldNotBeAbleToDeleteAnItemWithUnknownId() throws URISyntaxException {
		fail();
	}

	@Test
	void shouldBeAbleToReplaceAnItem() throws URISyntaxException {
		fail();
	}

	@Test
	void shouldNotBeAbleToReplaceAnItemWithUnknownId() throws URISyntaxException {
		fail();
	}

	private ResponseEntity<Item> givenAnInsertedItem() {
		Item item = buildLawnMower();
		return restTemplate.postForEntity(BASE_PATH, item, Item.class);
	}

	private Item buildLawnMower() {
		Item item = Item.builder().name("Lawn mower").amount(1).lastUsed(Date.valueOf("2019-05-01"))
				.location("Basement").build();
		return item;
	}

	private Item buildLawnTrimmer() {
		Item item = Item.builder().name("Lawn trimmer").amount(1).lastUsed(Date.valueOf("2018-05-01"))
				.location("Basement").build();
		return item;
	}

}
```
