package de.telekom.sea.mystuff.backend;

import java.util.List;
import java.util.function.Function;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/items")
public class ItemRestController {

	@Autowired
	private ItemRepo repository;

	@GetMapping
	public List<Item> findAll() {
		return this.repository.findAll();
	}

	@GetMapping("{id}")
	public Item getById(@PathVariable Long id) {
		return this.repository.findById(id).orElseThrow(() -> {
			throw new ResourceNotFoundException(id);
		});
	}

	@PostMapping
	public Item createItem(@RequestBody Item newItem, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_CREATED);
		return this.repository.save(newItem);
	}

	@PutMapping("{id}")
	public Item replace(@PathVariable Long id, @RequestBody Item updatedItem) {

		return this.repository.findById(id).map((Function<Item, Item>) item -> {
			item.setName(updatedItem.getName());
			item.setAmount(updatedItem.getAmount());
			item.setLocation(updatedItem.getLocation());
			item.setDescription(updatedItem.getDescription());
			item.setLastUsed(updatedItem.getLastUsed());
			return repository.save(item);
		}).orElseThrow(() -> {
			throw new ResourceNotFoundException(id);
		});
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable Long id, HttpServletResponse response) {
		try {
			repository.deleteById(id);
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		}
	}
}
