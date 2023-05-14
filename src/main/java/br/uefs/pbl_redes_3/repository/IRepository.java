package br.uefs.pbl_redes_3.repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface IRepository<T,ID>{

    public T save(T model);

    public boolean deleteIf(Predicate<T> predicate);

    public Optional<T> update(T model);

    public boolean contains(Predicate<T> predicate);

    public Optional<T> findById(ID id);

    public List<T> findAll(Predicate<T> predicate);
}
