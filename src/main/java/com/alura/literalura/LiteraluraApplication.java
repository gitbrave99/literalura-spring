package com.alura.literalura;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("1- bucar libro por titulo\n" +
				"2- listar libros registrados\n" +
				"3- listra autores registrador\n" +
				"4- listar autores vivos en un determinado año\n" +
				"5- listrar libros por idioma\n" +
				"Ingrese opción = ");
		/*
		* 1- bucar libro por titulo
		* 2- listar libros registrados
		* 2- listra autores registrador
		* 3- listar autores vivos en un determinado año
		* 4- listrar libros por idioma
		* 0- salir
		* */
	}
}
