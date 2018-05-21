package com.github.langion.form;

import java.util.HashMap;
import java.util.Map;

public class FieldEntity extends Entity {

	public ModifierEntity Modifiers;
	public TypeEntity Type;
	public Map<String, AnnotationEntity> Annotations = new HashMap<String, AnnotationEntity>();

}
