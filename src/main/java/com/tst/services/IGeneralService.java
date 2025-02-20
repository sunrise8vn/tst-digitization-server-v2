package com.tst.services;

import java.util.Optional;

public interface IGeneralService<E, T> {

    Optional<E> findById(T id);

    void delete(E e);

    void deleteById(T id);
}
