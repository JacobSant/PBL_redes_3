package br.uefs.pbl_redes_3.utils;

import org.modelmapper.ModelMapper;

public class Mapper {
    private static ModelMapper mapper = new ModelMapper();
    public static <D> D map(Object source, Class<D> destination){
        return mapper.map(source, destination);
    }
}
