package com.github.langion.creator.creators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.langion.creator.Explorer;
import com.github.langion.form.AnnotationEntity;
import com.github.langion.form.EnumEntity;
import com.github.langion.form.VariableEntity;
import com.github.langion.form.consts.Kind;

public class EnumCreator extends EntityCreator<EnumDeclaration, EnumEntity> {

	private Class<?> clazz;

	public EnumCreator(Optional<EnumDeclaration> node, Class<?> clazz, Explorer explorer) {
		super(node, explorer.elicitCanonical(clazz), Kind.Enum, explorer, new EnumEntity());
		this.clazz = clazz;
	}
	
	@Override
	public void decode() {
		this.parseModifiers();
		this.parseExtends();
		this.parseImplements();
		this.parseAnnotations();
		this.parseVariables();
		this.parseList();
	}

	private void parseModifiers() {
		Optional<Integer> optionalModifiers = this.getModifiers(this.clazz);

		if (!optionalModifiers.isPresent()) {
			return;
		}

		Integer modifiers = optionalModifiers.get();
		this.entity.Modifiers = this.explorer.make(modifiers);
	}

	private void parseExtends() {
		try {
			Optional<Type> optionalParent = this.getGenericSuperclass(this.clazz);

			if (!optionalParent.isPresent()) {
				return;
			}

			Type parent = optionalParent.get();

			String parentName = parent.toString();
			Boolean isObject = parentName.contains("class java.lang.Object");

			if (parent != null && !isObject) {
				this.entity.Extends = this.explorer.make(parent);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void parseImplements() {
		Optional<Type[]> optionalGenericInterfaces = this.getGenericInterfaces(this.clazz);

		if (!optionalGenericInterfaces.isPresent()) {
			return;
		}

		Type[] genericInterfaces = optionalGenericInterfaces.get();

		Stream.of(genericInterfaces).map(i -> this.explorer.make(i))
				.forEach(f -> this.entity.Implements.put(f.Name, f));
	}

	private void parseAnnotations() {
		Optional<Annotation[]> optionalAnnotations = this.getDeclaredAnnotations(this.clazz);

		if (!optionalAnnotations.isPresent()) {
			return;
		}

		Annotation[] annotations = optionalAnnotations.get();

		Stream.of(annotations).map(a -> this.parseAnnotation(a)).forEach(f -> this.entity.Annotations.put(f.Name, f));
	}

	private AnnotationEntity parseAnnotation(Annotation annotation) {
		Optional<AnnotationExpr> expr = this.getAnnotationByClassFromEnum(annotation, this.node);
		AnnotationEntity result = this.explorer.make(annotation, expr);
		return result;
	}

	private void parseVariables() {
		Optional<TypeVariable<?>[]> optionalTypeParameters = this.getTypeParameters(this.clazz);

		if (!optionalTypeParameters.isPresent()) {
			return;
		}

		TypeVariable<?>[] parameters = optionalTypeParameters.get();

		for (int i = 0; i < parameters.length; i++) {
			TypeVariable<?> variable = parameters[i];
			VariableEntity entity = this.explorer.make(variable, i);
			this.entity.Variables.put(entity.Name, entity);
		}
	}

	private void parseList() {
		Optional<Object[]> optionalEnumConstants = this.getEnumConstants(this.clazz);

		if (!optionalEnumConstants.isPresent()) {
			return;
		}

		Object[] enumConstants = optionalEnumConstants.get();
		Stream.of(enumConstants).forEach(e -> this.parseEnum(e));
	}

	private void parseEnum(Object object) {
		if (object instanceof Enum) {
			Enum<?> enumeration = (Enum<?>) object;
			String name = enumeration.name();
			String value = enumeration.toString();
			this.entity.Items.put(name, value);
		}
	}

}
