package co.com.adminshz.utils;

import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

public  class  UtilsFuntions {
    private UtilsFuntions(){

    }
    public static <T> Mono<List<T>>OrdenarListaDescendente(List<T> lista){
        Collections.reverse(lista);
        return Mono.just(lista);
    }
}
