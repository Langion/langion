package com.github.langion.creator.creators;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import com.github.javaparser.ast.Node;
import com.github.langion.creator.Explorer;
import com.github.langion.form.GenericEntity;
import com.github.langion.form.TypeEntity;
import com.github.langion.form.consts.Kind;

public class TypeCreator extends EntityCreator<Node, TypeEntity> {

	private Type type;
	private Class<?> clazz;

	public TypeCreator(Type type, Explorer explorer) {
		super(explorer.elicitCanonical(type), Kind.Type, explorer, new TypeEntity());
		this.type = type;
	}

	public TypeCreator(Type type, Class<?> clazz, Explorer explorer) {
		this(type, explorer);
		this.clazz = clazz;
		this.type = type;
	}

	@Override
	public void decode() {
		if (this.type instanceof ParameterizedType) {
			ParameterizedType parameterized = (ParameterizedType) this.type;
			this.type = parameterized.getRawType();
			Type[] arguments = parameterized.getActualTypeArguments();

			for (int position = 0; position < arguments.length; position++) {
				Type argument = arguments[position];
				GenericEntity generic = this.explorer.make(argument, position);
				this.entity.Generics.put(position, generic);
			}
		}

		if (this.type instanceof TypeVariable<?>) {
			this.entity.IsParameter = true;
		} else if (this.type instanceof Class<?>) {
			Class<?> clazz = (Class<?>) this.type;
			this.decodeAsClazz(clazz);
		} else if (this.clazz != null) {
			this.decodeAsClazz(this.clazz);
		}
	}

	private void decodeAsClazz(Class<?> clazz) {

		if (clazz.isArray()) {
			Class<?> component = clazz.getComponentType();
			Type type = (Type) component;
			GenericEntity entity = this.explorer.make(type, 0);
			this.entity.Generics.put(0, entity);
			this.entity.IsArray = true;
		} else {
			this.recursiveLoad(clazz);
		}
	}

	private void recursiveLoad(Class<?> clazz) {
		this.explorer.make(clazz);
	}

}
