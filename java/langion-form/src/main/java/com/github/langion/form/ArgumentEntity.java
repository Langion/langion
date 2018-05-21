package com.github.langion.form;

import java.util.HashMap;
import java.util.Map;

public class ArgumentEntity extends Entity {

	public Integer Position;
	public TypeEntity Type;
	public Map<String, AnnotationEntity> Annotations = new HashMap<String, AnnotationEntity>();

}
