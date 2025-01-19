package sem3tp.Storage;


import java.io.Serializable;
import java.util.ArrayList;

public abstract class Storage<T> implements Storagable<T>, Serializable {
    ArrayList<T> list;

    @Override
    public boolean isEmpty(){
        return list.isEmpty();
    }
    @Override
    public int indexOf(T t) {
        return this.list.indexOf(t);
    }

    @Override
    public T get(T t) {
        int index = list.indexOf(t);
        if(index>-1){
            return getByIndex(index);
        }
        return null;
    }

    @Override
    public ArrayList<T> getAll() {
        return this.list;
    }

    @Override
    public T getByIndex(int index) {
        return list.get(index);
    }

    @Override
    public boolean contains(T o) {
        return list.contains(o);
    }

    @Override
    public void insert(T t) {
        if(t!=null) {
            list.add(t);
        }
    }
/*
comparable to be considered
 */
    @Override
    public void delete(T t) {
        list.remove(t);
    }

    @Override
    public void update(T t) {
        //add implementation
    }

    @Override
    public int getSize() {
        return this.list.size();
    }
}
