package model.factory;

public interface ObjFactory<T> {

	T create(String line);

}