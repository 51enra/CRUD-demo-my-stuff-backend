package de.telekom.sea.mystuff.backend;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/items")
public class ItemRestController {

	@Autowired
	private ItemRepo repository;
	// Besser ist es für Testbarkeit, wenn der Konstruktor explizit eingebaut wird
	// und dort da @Autowired angeben, dann kann man auch ein "Testrepository" injekten.

	@GetMapping
	public List<Item> findAll() {
		return this.repository.findAll();
	}

	@GetMapping("{id}")
	public Item getById(@PathVariable Long id) {
		return this.repository.findById(id).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND); // ResourceNotFoundException(id);
		});
	}

	@PostMapping
	public Item createItem(@RequestBody Item newItem, HttpServletResponse response) {
		
		// Hier besser die id explizit auf null, damit sichergestellt ist, dass 
		// keine existierenden Items überschrieben werden können!
		
		response.setStatus(HttpServletResponse.SC_CREATED);
		return this.repository.save(newItem);
	}

	@PutMapping("{id}")
	public Item replace(@PathVariable Long id, @RequestBody Item updatedItem) {

		return this.repository.findById(id).map( item -> {
			item.setName(updatedItem.getName());
			item.setAmount(updatedItem.getAmount());
			item.setLocation(updatedItem.getLocation());
			item.setDescription(updatedItem.getDescription());
			item.setLastUsed(updatedItem.getLastUsed());
			return repository.save(item);
		}).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND); // ResourceNotFoundException(id);
		});
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable Long id, HttpServletResponse response) {
		try {
			repository.deleteById(id);
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		} catch (EmptyResultDataAccessException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
}
