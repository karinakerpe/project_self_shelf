package com.selfshelf.project;

import com.selfshelf.project.model.BookEntity;
import com.selfshelf.project.model.Status;
import com.selfshelf.project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan
@Configuration
@EnableAutoConfiguration
public class ProjectApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}
	@Autowired
	BookRepository bookRepository;

	@Override
	public void run(String... args) throws Exception {

		BookEntity book1 = new BookEntity("Alice's Adventures in Wonderland", "Lewis Carroll", 1998, 200, "019283374X", Status.BOOK_AVAILABLE);
		bookRepository.save(book1);
		BookEntity book2 = new BookEntity("The Emperor's New Mind", "Roger Penrose", 1989, 480, "0192861980", Status.BOOK_AVAILABLE);
		bookRepository.save(book2);
		BookEntity book3 = new BookEntity("The Little Mermaid", "Hans Christian Andersen", 2019, 64, "1782692509", Status.BOOK_AVAILABLE);
		bookRepository.save(book3);
	}

}
