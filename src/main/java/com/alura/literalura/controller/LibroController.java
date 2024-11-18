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

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LibroController {
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    LibroService libroService = new LibroService();
    private Scanner sc = new Scanner(System.in);
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private List<Libro> libros;
    private List<Autor> autores;

    public LibroController(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository= libroRepository;
        this.autorRepository = autorRepository;
    }


    public void listarLibrosPorIdioma() {
        var menuIdiomas = """
                ----------------------------
                    IDIOMAS DISPONIBLES
                ----------------------------
                es(Spanich)
                en(English)
                fr(French)
                pt(Portuguese)
                """;
        String idiomaLibro;
        do {
            System.out.println(menuIdiomas);
            System.out.print("Ingresa el código de idioma como: es");
            idiomaLibro = sc.nextLine().toLowerCase();
            if (!idiomaLibro.matches("^[a-z]{2}$")) {
                System.out.println("""
                        
                        **************************************************
                        IDIOMA NO VÁLIDO
                        **************************************************""");
            }
        } while (!idiomaLibro.matches("^[a-z]{2}$"));
        List<Libro> librosPorIdioma = libroRepository.findByIdiomasContaining(idiomaLibro);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("LIBROS NO ENCONTRADO");
        } else {
            if (librosPorIdioma.size() == 1) {
                System.out.printf("""
                                   %d LIBRO EN EL IDIOMA '%s'            
                        %n""", librosPorIdioma.size(), idiomaLibro.toUpperCase());
            } else {
                System.out.printf("""
                                   %d LIBROS EN EL IDIOMA '%s'           
                        %n""", librosPorIdioma.size(), idiomaLibro.toUpperCase());
            }
            mostrarLibros(librosPorIdioma);
        }
    }

    public void listarLibrosRegistrados() {
        libros = libroRepository.findAllWithAutores();
        if (libros.isEmpty()) {
            System.out.println("""
                    NO HAY LIBROS REGISTRADOS
                    """);
            return;
        }
        System.out.printf("""
                            %d LIBROS REGISTRADOS             
                %n""", libros.size());
        mostrarLibros(libros);
    }

    public void buscarLibro(){
        System.out.print("Ingresa el titulo del libro a buscar: ");
        var nombreLibro = sc.nextLine();
        var datosLibros=libroService.getDatosLibros(nombreLibro);
        if (datosLibros == null) {
            System.out.println("LIBRO NO ENCONTRADO");
        }
        Optional<Libro> libroExistente = libroRepository.findByTitulo(datosLibros.titulo());
        if (libroExistente.isPresent()) {
            System.out.println("LIBRO REGISTRADO");
            LibroDTO libroDTO = new LibroDTO(
                    libroExistente.get().getId(),
                    libroExistente.get().getTitulo(),
                    libroExistente.get().getAutores().stream().map(autor -> new AutorDTO(autor.getId(), autor.getNombre(), autor.getFechaNacimiento(), autor.getFechaFallecimiento()))
                            .collect(Collectors.toList()),
                    String.join(", ", libroExistente.get().getIdiomas()),
                    libroExistente.get().getNumeroDeDescargas()
            );
            System.out.printf(
                    """
                            --------------------------------------------------
                                                  LIBRO ENCONTRADO                 
                            Título= %s
                            Autor= %s
                            Idioma= %s
                            No Descargas= %.2f%n
                           \n--------------------------------------------------""", libroDTO.titulo(),
                    libroDTO.autores().stream().map(AutorDTO::nombre).collect(Collectors.joining(", ")),
                    libroDTO.idiomas(),
                    libroDTO.numeroDeDescargas()
            );
            System.out.println("--------------------------------------------------");
            return;
        }
        List<Autor> autores = datosLibros.autor().stream()
                .map(datosAutor -> autorRepository.findByNombre(datosAutor.nombre())
                        .orElseGet(() -> {
                            // Crear y guardar nuevo autor
                            Autor nuevoAutor = new Autor();
                            nuevoAutor.setNombre(datosAutor.nombre());
                            nuevoAutor.setFechaNacimiento(datosAutor.fechaNacimiento());
                            nuevoAutor.setFechaFallecimiento(datosAutor.fechaFallecimiento());
                            autorRepository.save(nuevoAutor);
                            return nuevoAutor;
                        })
                ).collect(Collectors.toList());
        Libro libro = new Libro(datosLibros);
        libro.setAutores(autores);
        libroRepository.save(libro);
        LibroDTO libroDTO = new LibroDTO(
                libro.getId(),
                libro.getTitulo(),
                libro.getAutores().stream().map(autor -> new AutorDTO(autor.getId(), autor.getNombre(), autor.getFechaNacimiento(), autor.getFechaFallecimiento()))
                        .collect(Collectors.toList()),
                String.join(", ", libro.getIdiomas()),
                libro.getNumeroDeDescargas()
        );
        System.out.printf(
                """           
                        -----------------------------------------------
                                              LIBRO                     
                        Título=  %s
                        Autor=   %s
                        Idioma=  %s
                        No Descargas= %.2f%n""", libroDTO.titulo(),
                libroDTO.autores().stream().map(AutorDTO::nombre).collect(Collectors.joining(", ")),
                libroDTO.idiomas(),
                libroDTO.numeroDeDescargas()
        );
        System.out.println("--------------------------------------------------");
    }





    private void mostrarLibros(List<Libro> libroList) {
        for (Libro libro : libroList) {
            List<AutorDTO> autoresDTO = libro.getAutores().stream()
                    .map(autor -> new AutorDTO(autor.getId(), autor.getNombre(), autor.getFechaNacimiento(), autor.getFechaFallecimiento()))
                    .collect(Collectors.toList());

            LibroDTO libroDTO = new LibroDTO(
                    libro.getId(),
                    libro.getTitulo(),
                    autoresDTO,
                    String.join(", ", libro.getIdiomas()),
                    libro.getNumeroDeDescargas()
            );

            System.out.printf(
                    """
                            --------------------------------------------------
                                                  LIBRO ENCONTRADO            
                            Título= %s
                            Autor= %s
                            Idioma= %s
                            No Descargas: %.2f%n""", libroDTO.titulo(),
                    libroDTO.autores().stream().map(AutorDTO::nombre).collect(Collectors.joining(", ")),
                    String.join(", ", libro.getIdiomas()),
                    libroDTO.numeroDeDescargas()
            );
            System.out.println("--------------------------------------------------");

        }
    }


}
