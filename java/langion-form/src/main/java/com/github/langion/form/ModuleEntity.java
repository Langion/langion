package com.github.langion.form;

import java.util.HashMap;
import java.util.Map;

public class ModuleEntity extends Entity {

	public Map<String, Entity> Exports = new HashMap<String, Entity>();
	public Map<String, ModuleEntity> Modules = new HashMap<String, ModuleEntity>();

}
