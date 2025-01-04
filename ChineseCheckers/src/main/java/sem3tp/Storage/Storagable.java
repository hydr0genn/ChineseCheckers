package sem3tp.Storage;

import sem3tp.Poles.Pole;

import java.util.List;

/*
Interface for storages like UserStorage
Aim is to allow the editor to modify the storages easily
 */
public interface Storagable<T> {
    void insert(T t);

    int indexOf(T t);

    void delete(T t);

    List<T> getAll();

    void update(T t);

    int getSize();

    T getByIndex(int index);

    boolean contains(T o);

    T get(T t);
}
