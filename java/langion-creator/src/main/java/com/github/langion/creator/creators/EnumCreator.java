package com.github.langion.creator.creators;

import java.util.Optional;
import java.util.stream.Stream;

import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.langion.creator.Explorer;
import com.github.langion.form.EnumEntity;
import com.github.langion.form.consts.Kind;

public class EnumCreator extends EntityCreator<EnumDeclaration, EnumEntity> {

	private Class<?> clazz;

	public EnumCreator(Optional<EnumDeclaration> node, Class<?> clazz, Explorer explorer) {
		super(node, explorer.elicitCanonical(clazz), Kind.Enum, explorer, new EnumEntity());
		this.clazz = clazz;
	}

	@Override
	public void decode() {
		this.parseList();
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
