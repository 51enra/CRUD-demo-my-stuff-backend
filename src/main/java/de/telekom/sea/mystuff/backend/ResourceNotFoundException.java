package de.telekom.sea.mystuff.backend;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5909234906542641361L;

	ResourceNotFoundException(Long id) {
		super("Could not find resource " + id);
	}

}

