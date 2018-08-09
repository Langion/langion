package com.github.langion.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassEntity extends Entity {

	public TypeEntity Extends;
	public ModifierEntity Modifiers;
	public Map<String, AnnotationEntity> Annotations = new HashMap<String, AnnotationEntity>();
	public Map<String, FieldEntity> Fields = new HashMap<String, FieldEntity>();
	public Map<String, TypeEntity> Implements = new HashMap<String, TypeEntity>();
	public Map<String, List<MethodEntity>> Methods = new HashMap<String, List<MethodEntity>>();
	public List<MethodEntity> Constructors = new ArrayList<MethodEntity>();
	public Map<String, VariableEntity> Variables = new HashMap<String, VariableEntity>();

}
