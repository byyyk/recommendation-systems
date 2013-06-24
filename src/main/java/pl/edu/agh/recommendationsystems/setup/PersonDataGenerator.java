package pl.edu.agh.recommendationsystems.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.agh.recommendationsystems.persistence.Person;
import pl.edu.agh.recommendationsystems.persistence.repository.PersonRepository;
import pl.edu.agh.recommendationsystems.persistence.repository.VoteRepository;

public class PersonDataGenerator {
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private VoteRepository voteRepository;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PersonDataGenerator.class);
	
	public void generateAndPersistUsers(int userCount) {
		LOGGER.debug("Generating users");
		for (int i = 1; i <= userCount; i++) {
			Person person = new Person();
			person.setUsername("user" + i);
			personRepository.save(person);
		}
	}

	public void removeAll() {
		personRepository.deleteAllInBatch();
	}
	
}
