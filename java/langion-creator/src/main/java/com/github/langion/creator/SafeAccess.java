package com.github.langion.creator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

public interface SafeAccess {
	
	public default Optional<Constructor<?>[]> getDeclaredConstructors(Class<?> clazz) {
		Optional<Constructor<?>[]> result = null;

		try {
			Constructor<?>[] value = clazz.getDeclaredConstructors();
			value = Stream.of(value).filter(v -> !v.isSynthetic()).toArray(Constructor<?>[]::new);
			result = Optional.ofNullable(value);
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Method[]> getDeclaredMethods(Class<?> clazz) {
		Optional<Method[]> result = null;

		try {
			Method[] value = clazz.getDeclaredMethods();
			value = Stream.of(value).filter(v -> !v.isSynthetic()).toArray(Method[]::new);
			result = Optional.ofNullable(value);
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Integer> getModifiers(Executable method) {
		Optional<Integer> result = null;
		Class<?> clazz = null;

		try {
			Integer value = method.getModifiers();
			result = Optional.ofNullable(value);
			clazz = method.getDeclaringClass();
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Integer> getModifiers(Field field) {
		Optional<Integer> result = null;
		Class<?> clazz = null;

		try {
			Integer value = field.getModifiers();
			result = Optional.ofNullable(value);
			clazz = field.getDeclaringClass();
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Integer> getModifiers(Class<?> clazz) {
		Optional<Integer> result = null;

		try {
			Integer value = clazz.getModifiers();
			result = Optional.ofNullable(value);
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Type> getGenericSuperclass(Class<?> clazz) {
		Optional<Type> result = null;

		try {
			Type value = clazz.getGenericSuperclass();
			result = Optional.ofNullable(value);
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Type> getGenericType(Field field) {
		Optional<Type> result = null;
		Class<?> clazz = null;

		try {
			Type value = field.getGenericType();
			result = Optional.ofNullable(value);
			clazz = field.getDeclaringClass();
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Type> getGenericReturnType(Method method) {
		Optional<Type> result = null;
		Class<?> clazz = null;

		try {
			Type value = method.getGenericReturnType();
			result = Optional.ofNullable(value);
			clazz = method.getDeclaringClass();
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Class<?>> getType(Field field) {
		Optional<Class<?>> result = null;
		Class<?> clazz = null;

		try {
			Class<?> value = field.getType();
			result = Optional.ofNullable(value);
			clazz = field.getDeclaringClass();
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<com.github.javaparser.ast.body.Parameter> getParameter(Method method,
			Optional<MethodDeclaration> optionalNode, Integer pos) {
		Optional<com.github.javaparser.ast.body.Parameter> result = null;

		if (!optionalNode.isPresent()) {
			return result;
		}

		CallableDeclaration<?> node = optionalNode.get();
		Class<?> clazz = null;

		try {
			clazz = method.getDeclaringClass();

			com.github.javaparser.ast.body.Parameter optionalFieldDec = node.getParameter(pos);

			if (optionalFieldDec == null) {
				return result;
			}

			result = Optional.ofNullable(optionalFieldDec);
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}
	
	public default Optional<com.github.javaparser.ast.body.Parameter> getParameter(Constructor<?> method,
			Optional<ConstructorDeclaration> optionalNode, Integer pos) {
		Optional<com.github.javaparser.ast.body.Parameter> result = null;

		if (!optionalNode.isPresent()) {
			return result;
		}

		CallableDeclaration<?> node = optionalNode.get();
		Class<?> clazz = null;

		try {
			clazz = method.getDeclaringClass();

			com.github.javaparser.ast.body.Parameter optionalFieldDec = node.getParameter(pos);

			if (optionalFieldDec == null) {
				return result;
			}

			result = Optional.ofNullable(optionalFieldDec);
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Parameter[]> getParameters(Executable method) {
		Optional<Parameter[]> result = null;
		Class<?> clazz = null;

		try {
			Parameter[] value = method.getParameters();
			result = Optional.ofNullable(value);
			clazz = method.getDeclaringClass();
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Class<?>> getReturnType(Method method) {
		Optional<Class<?>> result = null;
		Class<?> clazz = null;

		try {
			Class<?> value = method.getReturnType();
			result = Optional.ofNullable(value);
			clazz = method.getDeclaringClass();
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Type[]> getGenericInterfaces(Class<?> clazz) {
		Optional<Type[]> result = null;

		try {
			Type[] value = clazz.getGenericInterfaces();
			result = Optional.ofNullable(value);
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Annotation[]> getDeclaredAnnotations(Executable method) {
		Optional<Annotation[]> result = null;
		Class<?> clazz = null;

		try {
			Annotation[] value = method.getDeclaredAnnotations();
			result = Optional.ofNullable(value);
			clazz = method.getDeclaringClass();
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Annotation[]> getDeclaredAnnotations(Field field) {
		Optional<Annotation[]> result = null;
		Class<?> clazz = null;

		try {
			Annotation[] value = field.getDeclaredAnnotations();
			result = Optional.ofNullable(value);
			clazz = field.getDeclaringClass();
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Annotation[]> getDeclaredAnnotations(Class<?> clazz) {
		Optional<Annotation[]> result = null;

		try {
			Annotation[] value = clazz.getDeclaredAnnotations();
			result = Optional.ofNullable(value);
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<TypeVariable<?>[]> getTypeParameters(Executable method) {
		Optional<TypeVariable<?>[]> result = null;
		Class<?> clazz = null;

		try {
			TypeVariable<?>[] value = method.getTypeParameters();
			result = Optional.ofNullable(value);
			clazz = method.getDeclaringClass();
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<TypeVariable<?>[]> getTypeParameters(Class<?> clazz) {
		Optional<TypeVariable<?>[]> result = null;

		try {
			TypeVariable<?>[] value = clazz.getTypeParameters();
			result = Optional.ofNullable(value);
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Field[]> getDeclaredFields(Class<?> clazz) {
		Optional<Field[]> result = null;

		try {
			Field[] value = clazz.getDeclaredFields();
			value = Stream.of(value).filter(v -> !v.isSynthetic()).toArray(Field[]::new);
			result = Optional.ofNullable(value);
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Object[]> getEnumConstants(Class<?> clazz) {
		Optional<Object[]> result = null;

		try {
			Object[] value = clazz.getEnumConstants();
			result = Optional.ofNullable(value);
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<List<MethodDeclaration>> getMethodsByName(Method method,
			Optional<ClassOrInterfaceDeclaration> optionalNode) {
		
		Optional<List<MethodDeclaration>> result = Optional.empty();

		if (!optionalNode.isPresent()) {
			return result;
		}

		ClassOrInterfaceDeclaration node = optionalNode.get();
		Class<? extends Method> clazz = null;

		try {
			clazz = method.getClass();
			String name = method.getName();
			List<MethodDeclaration> declarations = node.getMethodsByName(name);
			Optional<List<MethodDeclaration>> optionalMethodsDec = Optional.ofNullable(declarations);

			if (!optionalMethodsDec.isPresent()) {
				return result;
			}

			List<MethodDeclaration> methodsDec = optionalMethodsDec.get();

			result = Optional.ofNullable(methodsDec);
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}
	
	public default Optional<ConstructorDeclaration> getConstructorByParameterTypes(Constructor<?> constructor,
			Optional<ClassOrInterfaceDeclaration> optionalNode) {
		
		Optional<ConstructorDeclaration> result = Optional.empty();

		if (!optionalNode.isPresent()) {
			return result;
		}

		ClassOrInterfaceDeclaration node = optionalNode.get();
		Class<?> clazz = null;

		try {
			clazz = constructor.getClass();
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			Optional<ConstructorDeclaration> declarations = node.getConstructorByParameterTypes(parameterTypes);

			if (!declarations.isPresent()) {
				return result;
			}

			ConstructorDeclaration constructorDec = declarations.get();

			result = Optional.ofNullable(constructorDec);
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<FieldDeclaration> getFieldByName(Field field,
			Optional<ClassOrInterfaceDeclaration> optionalNode) {
		Optional<FieldDeclaration> result = null;

		if (!optionalNode.isPresent()) {
			return result;
		}

		ClassOrInterfaceDeclaration node = optionalNode.get();
		Class<? extends Field> clazz = null;

		try {
			clazz = field.getClass();
			String name = field.getName();
			Optional<FieldDeclaration> optionalFieldDec = node.getFieldByName(name);

			if (!optionalFieldDec.isPresent()) {
				return result;
			}

			FieldDeclaration fieldDec = optionalFieldDec.get();

			result = Optional.ofNullable(fieldDec);
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<AnnotationExpr> getAnnotationByClassFromMethod(Annotation annotation,
			Optional<MethodDeclaration> optionalNode) {
		Optional<AnnotationExpr> result = null;

		if (!optionalNode.isPresent()) {
			return result;
		}

		CallableDeclaration<?> node = optionalNode.get();
		Optional<Class<? extends Annotation>> optionalType = this.getAnnotationType(annotation);

		if (!optionalType.isPresent()) {
			return result;
		}

		Class<? extends Annotation> type = optionalType.get();

		try {
			Optional<AnnotationExpr> optionalValue = node.getAnnotationByClass(type);

			if (!optionalValue.isPresent()) {
				return result;
			}

			AnnotationExpr value = optionalValue.get();

			result = Optional.ofNullable(value);
		} catch (Throwable e) {
			this.showError(e, type);
			result = Optional.empty();
		}

		return result;
	}
	
	public default Optional<AnnotationExpr> getAnnotationByClassFromConstructor(Annotation annotation,
			Optional<ConstructorDeclaration> optionalNode) {
		Optional<AnnotationExpr> result = null;

		if (!optionalNode.isPresent()) {
			return result;
		}

		CallableDeclaration<?> node = optionalNode.get();
		Optional<Class<? extends Annotation>> optionalType = this.getAnnotationType(annotation);

		if (!optionalType.isPresent()) {
			return result;
		}

		Class<? extends Annotation> type = optionalType.get();

		try {
			Optional<AnnotationExpr> optionalValue = node.getAnnotationByClass(type);

			if (!optionalValue.isPresent()) {
				return result;
			}

			AnnotationExpr value = optionalValue.get();

			result = Optional.ofNullable(value);
		} catch (Throwable e) {
			this.showError(e, type);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<AnnotationExpr> getAnnotationByClassFromFields(Annotation annotation,
			Optional<FieldDeclaration> optionalNode) {
		Optional<AnnotationExpr> result = Optional.empty();

		if (!optionalNode.isPresent()) {
			return result;
		}

		FieldDeclaration node = optionalNode.get();
		Optional<Class<? extends Annotation>> optionalType = this.getAnnotationType(annotation);

		if (!optionalType.isPresent()) {
			return result;
		}

		Class<? extends Annotation> type = optionalType.get();

		try {
			Optional<AnnotationExpr> optionalValue = node.getAnnotationByClass(type);

			if (!optionalValue.isPresent()) {
				return result;
			}

			AnnotationExpr value = optionalValue.get();

			result = Optional.ofNullable(value);
		} catch (Throwable e) {
			this.showError(e, type);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<AnnotationExpr> getAnnotationByClassFromClassOrInterface(Annotation annotation,
			Optional<ClassOrInterfaceDeclaration> optionalNode) {
		Optional<AnnotationExpr> result = null;

		if (!optionalNode.isPresent()) {
			return result;
		}

		ClassOrInterfaceDeclaration node = optionalNode.get();
		Optional<Class<? extends Annotation>> optionalType = this.getAnnotationType(annotation);

		if (!optionalType.isPresent()) {
			return result;
		}

		Class<? extends Annotation> type = optionalType.get();

		try {
			Optional<AnnotationExpr> optionalValue = node.getAnnotationByClass(type);

			if (!optionalValue.isPresent()) {
				return result;
			}

			AnnotationExpr value = optionalValue.get();

			result = Optional.ofNullable(value);
		} catch (Throwable e) {
			this.showError(e, type);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Class<? extends Annotation>> getAnnotationType(Annotation annotation) {
		Optional<Class<? extends Annotation>> result = null;
		Class<? extends Annotation> clazz = null;

		try {
			Class<? extends Annotation> value = annotation.annotationType();
			result = Optional.ofNullable(value);
			clazz = annotation.getClass();
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Type> getParameterizedType(Parameter parameter) {
		Optional<Type> result = null;
		Class<? extends Parameter> clazz = null;

		try {
			Type value = parameter.getParameterizedType();
			result = Optional.ofNullable(value);
			clazz = parameter.getClass();
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<Annotation[]> getDeclaredAnnotations(Parameter parameter) {
		Optional<Annotation[]> result = null;
		Class<? extends Parameter> clazz = null;

		try {
			Annotation[] value = parameter.getDeclaredAnnotations();
			result = Optional.ofNullable(value);
			clazz = parameter.getClass();
		} catch (Throwable e) {
			this.showError(e, clazz);
			result = Optional.empty();
		}

		return result;
	}

	public default Optional<AnnotationExpr> getAnnotationByClass(Annotation annotation,
			Optional<com.github.javaparser.ast.body.Parameter> optionalNode) {
		Optional<AnnotationExpr> result = null;

		if (!optionalNode.isPresent()) {
			return result;
		}

		com.github.javaparser.ast.body.Parameter node = optionalNode.get();
		Optional<Class<? extends Annotation>> optionalType = this.getAnnotationType(annotation);

		if (!optionalType.isPresent()) {
			return result;
		}

		Class<? extends Annotation> type = optionalType.get();

		try {
			Optional<AnnotationExpr> optionalValue = node.getAnnotationByClass(type);

			if (!optionalValue.isPresent()) {
				return result;
			}

			AnnotationExpr value = optionalValue.get();

			result = Optional.ofNullable(value);
		} catch (Throwable e) {
			this.showError(e, type);
			result = Optional.empty();
		}

		return result;
	}

	public default void showError(Throwable e, Class<?> clazz) {
		String message = e.toString();
		String path = "";

		try {
			if (clazz != null) {
				path = clazz.getCanonicalName();
			}
		} catch (Throwable err) {
			err.printStackTrace();
		}

		System.err.println("Exception in: " + path + " - " + message);
	}

}

