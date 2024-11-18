package com.alura.literalura.helpers;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> classInfo);
}
