package de.telekom.sea.mystuff.backend;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Item {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private int amount;
	private String location;
	private String description;
	private LocalDate lastUsed;

	public Item() {
		// TODO Auto-generated constructor stub
	}

}