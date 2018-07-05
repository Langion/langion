package com.github.langion.creator;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.langion.creator.creators.AnnotationCreator;
import com.github.langion.creator.creators.ArgumentCreator;
import com.github.langion.creator.creators.ClassCreator;
import com.github.langion.creator.creators.EntityCreator;
import com.github.langion.creator.creators.EnumCreator;
import com.github.langion.creator.creators.FieldCreator;
import com.github.langion.creator.creators.GenericCreator;
import com.github.langion.creator.creators.InterfaceCreator;
import com.github.langion.creator.creators.LangionCreator;
import com.github.langion.creator.creators.MethodCreator;
import com.github.langion.creator.creators.ModifierCreator;
import com.github.langion.creator.creators.TypeCreator;
import com.github.langion.creator.creators.ValueCreator;
import com.github.langion.creator.creators.VariableCreator;
import com.github.langion.creator.creators.WildcardCreator;
import com.github.langion.form.AnnotationEntity;
import com.github.langion.form.ArgumentEntity;
import com.github.langion.form.FieldEntity;
import com.github.langion.form.GenericEntity;
import com.github.langion.form.MethodEntity;
import com.github.langion.form.ModifierEntity;
import com.github.langion.form.ModuleEntity;
import com.github.langion.form.TypeEntity;
import com.github.langion.form.ValueEntity;
import com.github.langion.form.VariableEntity;
import com.github.langion.form.WildcardEntity;

import javassist.Modifier;

public class Explorer {

	private LangionCreator langion;
	private Configuration config;

	private List<Class<?>> introspected = new ArrayList<Class<?>>();
	private Map<Class<?>, TypeEntity> types = new HashMap<Class<?>, TypeEntity>();

	public Explorer(LangionCreator langion, Configuration config) {
		this.langion = langion;
		this.config = config;
	}

	public TypeEntity make(Class<?> clazz) {
		Boolean hasBeenIntrospected = this.introspected.contains(clazz);

		if (hasBeenIntrospected) {
			return this.types.get(clazz);
		} else {
			this.introspected.add(clazz);
		}

		CompilationUnit cu = this.elicitCompilationUnit(clazz);
		EntityCreator<?, ?> creator;

		if (clazz.isInterface()) {
			Optional<ClassOrInterfaceDeclaration> dec = cu == null ? Optional.empty()
					: cu.getInterfaceByName(clazz.getSimpleName());

			creator = new InterfaceCreator(dec, clazz, this);
		} else if (clazz.isEnum()) {
			Optional<EnumDeclaration> dec = cu == null ? Optional.empty() : cu.getEnumByName(clazz.getSimpleName());
			creator = new EnumCreator(dec, clazz, this);
		} else {
			Optional<ClassOrInterfaceDeclaration> dec = null;

			try {
				dec = cu == null ? Optional.empty() : cu.getClassByName(clazz.getSimpleName());
			} catch (Exception e) {
				dec = Optional.empty();
			}

			creator = new ClassCreator(dec, clazz, this);
		}

		if (!this.isPrimitive(clazz)) {
			ModuleEntity module = this.langion.getModuleEntity(clazz);
			module.Exports.put(creator.entity.Name, creator.entity);
		}

		TypeEntity type = this.make((Type) clazz);
		this.types.put(clazz, type);

		creator.decode();

		return type;
	}

	public TypeEntity make(Type type) {
		TypeCreator creator = new TypeCreator(type, this);
		creator.decode();
		return creator.entity;
	}

	public TypeEntity make(Type type, Class<?> clazz) {
		TypeCreator creator = new TypeCreator(type, clazz, this);
		creator.decode();
		return creator.entity;
	}

	public FieldEntity make(Field field, Optional<FieldDeclaration> dec) {
		FieldCreator creator = new FieldCreator(dec, field, this);
		creator.decode();
		return creator.entity;
	}

	public GenericEntity make(Type generic, Integer position) {
		GenericCreator creator = new GenericCreator(generic, position, this);
		creator.decode();
		return creator.entity;
	}

	public VariableEntity make(TypeVariable<?> variable, Integer position) {
		VariableCreator creator = new VariableCreator(variable, position, this);
		creator.decode();
		return creator.entity;
	}

	public WildcardEntity make(WildcardType wildcard) {
		WildcardCreator creator = new WildcardCreator(wildcard, this);
		creator.decode();
		return creator.entity;
	}

	public ModifierEntity make(Integer modifiers) {
		ModifierCreator creator = new ModifierCreator(modifiers, this);
		creator.decode();
		return creator.entity;
	}

	public AnnotationEntity make(Annotation annotation, Optional<AnnotationExpr> dec) {
		AnnotationCreator creator = new AnnotationCreator(dec, annotation, this);
		creator.decode();
		return creator.entity;
	}

	public MethodEntity make(Method method, Optional<List<MethodDeclaration>> optionalList) {
		Optional<MethodDeclaration> dec = Optional.empty();

		if (optionalList.isPresent()) {
			List<MethodDeclaration> list = optionalList.get();

			dec = list.stream()
					.filter(m -> Stream.of(method.getParameters()).allMatch(p -> m.hasParametersOfType(p.getType())))
					.findFirst();
		}

		MethodCreator creator = new MethodCreator(dec, method, this);
		creator.decode();
		return creator.entity;
	}

	public ArgumentEntity make(Parameter parameter, Integer position,
			Optional<com.github.javaparser.ast.body.Parameter> dec) {
		ArgumentCreator creator = new ArgumentCreator(dec, parameter, position, this);
		creator.decode();
		return creator.entity;
	}

	public ValueEntity make(Object result) {
		ValueCreator creator = new ValueCreator(result, this);
		creator.decode();
		return creator.entity;
	}

	public String elicitCanonical(Object result) {
		try {
			String name = result.getClass().getCanonicalName();
			return name;
		} catch (Throwable e) {
			e.printStackTrace();
			return "";
		}
	}

	public String elicitCanonical(Parameter parameter) {
		try {
			return parameter.getName();
		} catch (Throwable e) {
			e.printStackTrace();
			return "";
		}
	}

	public String elicitCanonical(Class<?> clazz) {
		try {
			return clazz.getCanonicalName();
		} catch (Throwable e) {
			e.printStackTrace();
			return "";
		}
	}

	public String elicitCanonical(Field field) {
		try {
			return field.getName();
		} catch (Throwable e) {
			e.printStackTrace();
			return "";
		}
	}

	public String elicitCanonical(Type type) {
		try {
			Boolean isClazz = type instanceof Class<?>;

			if (!isClazz) {
				if (type == null) {
					return "";
				} else {

					return type.getTypeName();

				}
			}

			Class<?> clazz = (Class<?>) type;

			if (this.isStringType(clazz)) {
				return "string";
			} else if (this.isDate(clazz)) {
				return "Date";
			} else if (this.isLogical(clazz)) {
				return "boolean";
			} else if (this.isNumericType(clazz)) {
				return "number";
			} else {
				return this.elicitCanonical(clazz);
			}

		} catch (Throwable e) {
			e.printStackTrace();
			return "";
		}
	}

	public String elicitCanonical(TypeVariable<?> variable) {
		try {
			return variable.getName();
		} catch (Throwable e) {
			e.printStackTrace();
			return "";
		}
	}

	public String elicitCanonical(WildcardType wildcard) {
		try {
			return wildcard.getTypeName();
		} catch (Throwable e) {
			e.printStackTrace();
			return "";
		}
	}

	public String elicitCanonical(Annotation annotation) {
		try {
			return annotation.annotationType().getName();
		} catch (Throwable e) {
			e.printStackTrace();
			return "";
		}
	}

	public String elicitCanonical(Method method) {
		try {
			return method.getName();
		} catch (Throwable e) {
			e.printStackTrace();
			return "";
		}
	}

	public String elicitCanonical(Integer modifiers) {
		return "Modifiers";
	}

	public Boolean isPrimitive(Class<?> type) {
		return type.isPrimitive() || this.isStringType(type) || this.isDate(type) || this.isLogical(type)
				|| this.isNumericType(type);
	}

	public Boolean isStringType(Class<?> type) {
		return Stream.of(String.class).anyMatch(t -> t.isAssignableFrom(type));
	}

	public Boolean isDate(Class<?> type) {
		return Stream.of(Date.class, LocalDate.class).anyMatch(t -> t.isAssignableFrom(type));
	}

	public Boolean isLogical(Class<?> type) {
		return Stream.of(Boolean.class, boolean.class).anyMatch(t -> t.isAssignableFrom(type));
	}

	public Boolean isNumericType(Class<?> type) {
		return Stream
				.of(int.class, long.class, float.class, double.class, byte.class, short.class, Integer.class,
						Long.class, Float.class, Double.class, Byte.class, Short.class, Long.class, BigDecimal.class)
				.anyMatch(t -> t.isAssignableFrom(type));
	}

	private CompilationUnit elicitCompilationUnit(Class<?> clazz) {

		if (!this.config.srcBaseJavaFolders.isPresent()) {
			return null;
		}

		Optional<CompilationUnit> cu = this.getCompilationUnit(clazz);

		if (cu.isPresent()) {
			return cu.get();
		} else {
			CompilationUnit unit = this.getComilationUnitOfInternalClass(clazz);
			return unit;
		}
	}

	private Optional<CompilationUnit> getCompilationUnit(Class<?> clazz) {
		String canonicalName = this.elicitCanonical(clazz);

		if (canonicalName == null) {
			return Optional.empty();
		}

		String clazzPath = canonicalName.replaceAll("\\.", "\\" + File.separator);
		List<String> srcBaseJavaFolders = this.config.srcBaseJavaFolders.get();

		Optional<CompilationUnit> cu = srcBaseJavaFolders.stream().map(s -> {
			String path = s + File.separator + clazzPath + ".java";

			File file = new File(path);

			try {
				return JavaParser.parse(file);
			} catch (FileNotFoundException e) {
				return null;
			}

		}).filter(s -> s != null).findFirst();

		return cu;
	}

	private CompilationUnit getComilationUnitOfInternalClass(Class<?> clazz) {
		Package pack = clazz.getPackage();

		if (pack == null) {
			return null;
		}

		String packageName = pack.getName();
		Stream<Class<?>> classes = this.langion.getClassFromPackageName(packageName);
		Stream<Class<?>> publicClasses = classes.filter(v -> Modifier.isPublic(v.getModifiers()));

		Stream<CompilationUnit> declarations = publicClasses.<CompilationUnit>map(v -> {
			Optional<CompilationUnit> optionalUnit = this.getCompilationUnit(v);

			if (!optionalUnit.isPresent()) {
				return null;
			}

			CompilationUnit unit = optionalUnit.get();
			String name = clazz.getSimpleName();
			Optional<ClassOrInterfaceDeclaration> unitOfInternalClazz = unit.getClassByName(name);

			if (unitOfInternalClazz.isPresent()) {
				return unit;
			} else {
				return null;
			}
		});

		Optional<CompilationUnit> result = declarations.filter(d -> d != null).findFirst();

		if (result.isPresent()) {
			CompilationUnit unit = result.get();
			return unit;
		} else {
			return null;
		}
	}

}
