package com.alura.literalura.service;

import com.alura.literalura.entity.Autor;
import com.alura.literalura.entity.Datos;
import com.alura.literalura.entity.DatosLibro;
import com.alura.literalura.entity.Libro;
import com.alura.literalura.helpers.ConsumoAPI;
import com.alura.literalura.helpers.ConvierteDatos;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;

import java.util.List;
import java.util.Scanner;

public class LibroService {
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private List<Libro> libros;
    private List<Autor> autores;
    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();

    public DatosLibro getDatosLibros(String nombreLibro) {
        String json = consumoAPI.obtenerDatosLibros(URL_BASE + "?search=" + nombreLibro.replace(" ", "+")); // me trae un json
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        return datosBusqueda.listaResultados().stream()
                .filter(datosLibros -> datosLibros.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst()
                .orElse(null);
    }
}
