package com.github.langion.creator.creators;

import java.lang.reflect.Modifier;

import com.github.javaparser.ast.Node;
import com.github.langion.creator.Explorer;
import com.github.langion.form.ModifierEntity;
import com.github.langion.form.consts.Kind;
import com.github.langion.form.consts.Modifiers;

public class ModifierCreator extends EntityCreator<Node, ModifierEntity> {

	private Integer modifiers;

	public ModifierCreator(Integer modifiers, Explorer explorer) {
		super(explorer.elicitCanonical(modifiers), Kind.Modifier, explorer, new ModifierEntity());
		this.modifiers = modifiers;
	}

	@Override
	public void decode() {
		if (Modifier.isPrivate(this.modifiers)) {
			this.entity.Items.put(Modifiers.Private, Modifiers.Private);
		}

		if (Modifier.isProtected(modifiers)) {
			this.entity.Items.put(Modifiers.Protected, Modifiers.Protected);
		}

		if (Modifier.isPublic(modifiers)) {
			this.entity.Items.put(Modifiers.Public, Modifiers.Public);
		}
	}

}
