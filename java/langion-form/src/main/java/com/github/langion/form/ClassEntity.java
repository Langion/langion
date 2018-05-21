package com.github.langion.form;

import java.util.HashMap;
import java.util.Map;

public class ClassEntity extends Entity {

	public TypeEntity Extends;
	public ModifierEntity Modifiers;
	public Map<String, AnnotationEntity> Annotations = new HashMap<String, AnnotationEntity>();
	public Map<String, FieldEntity> Fields = new HashMap<String, FieldEntity>();
	public Map<String, TypeEntity> Implements = new HashMap<String, TypeEntity>();
	public Map<String, MethodEntity> Methods = new HashMap<String, MethodEntity>();
	public Map<String, VariableEntity> Variables = new HashMap<String, VariableEntity>();

}
