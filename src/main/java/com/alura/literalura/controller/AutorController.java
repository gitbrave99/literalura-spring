package com.alura.literalura.controller;

import com.alura.literalura.dto.AutorDTO;
import com.alura.literalura.dto.LibroDTO;
import com.alura.literalura.entity.Autor;
import com.alura.literalura.entity.Libro;
import com.alura.literalura.helpers.ConsumoAPI;
import com.alura.literalura.helpers.ConvierteDatos;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.LibroService;

import java.time.Year;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AutorController {

    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    LibroService libroService = new LibroService();
    private Scanner sc = new Scanner(System.in);
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private List<Libro> libros;
    private List<Autor> autores;

    public AutorController(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository= libroRepository;
        this.autorRepository = autorRepository;
    }

    public void listarAutoresRegistrados() {
        autores = autorRepository.findAllWithLibros();
        if (autores.isEmpty()) {
            System.out.println("""
                    NO HAY REGISTROS
                    """);
            return;
        }
        System.out.printf("""
                ------------------------------------------------
                *            %d AUTORES REGISTRADOS              *
                %n""", autores.size());
        mostrarAutores(autores);
    }

    public void listarAutoresVivosEnAnio() {
        var valorValido = false;
        String anioEstaVivo;
        do {
            System.out.print("Ingresa el año del autor vivo: ");
            anioEstaVivo = sc.nextLine();
            if (!anioEstaVivo.matches("\\d{4}")) {
                System.out.println("año no válido");
                continue;
            }
            valorValido = true;
        } while (!valorValido);

        int anio = Integer.parseInt(anioEstaVivo);
        List<Autor> autoresVivos = autorRepository.findByFechaNacimientoBeforeAndFechaFallecimientoAfterOrFechaFallecimientoIsNullAndFechaNacimientoIsNotNull(String.valueOf(anio), String.valueOf(anio));
        int anioActual = Year.now().getValue();
        autoresVivos = autoresVivos.stream()
                .filter(autor -> {
                    if (autor.getFechaFallecimiento() == null) {
                        int anioNacimiento = Integer.parseInt(autor.getFechaNacimiento());
                        return anioActual - anioNacimiento <= 100;
                    }
                    return true;
                }).collect(Collectors.toList());

        if (autoresVivos.isEmpty()) {
            System.out.println("""
                    
                    ------------------------------------------------
                    NO HAY AUTORES """);
        } else {
            System.out.printf("""
                    ------------------------------------------------
                    *            %d AUTORES VIVOS EN %d               *
                    %n""", autoresVivos.size(), anio);
            mostrarAutores(autoresVivos);
        }
    }

    private void mostrarAutores(List<Autor> autoresList) {
        for (Autor autor : autoresList) {
            List<String> librosDelAutor = autor.getLibrosDelAutor().stream()
                    .map(Libro::getTitulo)
                    .collect(Collectors.toList());
            AutorDTO autorDTO = new AutorDTO(
                    autor.getId(),
                    autor.getNombre(),
                    autor.getFechaNacimiento(),
                    autor.getFechaFallecimiento()
            );
            System.out.printf(""" 
                            ------------------------------------------------
                                               AUTOR                     

                            Autor: %s
                            Fecha de Nacimiento: %s
                            Fecha de Fallecimiento: %s
                            Libros: %s%n""", autorDTO.nombre(),
                    autorDTO.fechaNacimiento() != null ? autorDTO.fechaNacimiento() : "N/A",
                    autorDTO.fechaFallecimiento() != null ? autorDTO.fechaFallecimiento() : "N/A",
                    librosDelAutor
            );
            System.out.println("--------------------------------------------------");
        }
    }
}
