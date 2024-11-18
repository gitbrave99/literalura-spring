package com.alura.literalura;

import com.alura.literalura.controller.AutorController;
import com.alura.literalura.controller.LibroController;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {
	@Autowired
	private LibroRepository libroRepository;
	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LibroController libroController = new LibroController(libroRepository,autorRepository);
		AutorController autorController = new AutorController(libroRepository,autorRepository);

		System.out.println("LITERALURA \n" +
				"1- Buscar libro por titulo\n" +
				"2- listar libros registrados\n" +
				"3- listra autores registrador\n" +
				"4- listar autores vivos en un determinado año\n" +
				"5- listrar libros por idioma\n");
		Scanner scanner = new Scanner(System.in);
		System.out.print("Por favor, ingrese la opción: ");
		int opcion = scanner.nextInt();
		switch (opcion) {
			case 1:
				libroController.buscarLibro();
				break;
			case 2:
				libroController.listarLibrosRegistrados();
				break;
			case 3:
				autorController.listarAutoresRegistrados();
				break;
			case 4:
				autorController.listarAutoresVivosEnAnio();
				break;
			case 5:
				libroController.listarLibrosPorIdioma();
				break;
			default:
				System.out.println("Opción inválida");
		}
	}
}
