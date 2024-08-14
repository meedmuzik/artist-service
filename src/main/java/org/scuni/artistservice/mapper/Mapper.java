package org.scuni.artistservice.mapper;

public interface Mapper<F, T> {

    T map(F object);

}
