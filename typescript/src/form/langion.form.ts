import * as consts from "./consts";

export interface AnnotationEntity extends Entity {
    Items: Record<string, ValueEntity>;
}

export interface ArgumentEntity extends Entity {
    Position: number;
    Type: TypeEntity;
    Annotations: Record<string, AnnotationEntity>;
}

export interface ClassEntity extends Entity {
    Extends: TypeEntity;
    Modifiers: ModifierEntity;
    Annotations: Record<string, AnnotationEntity>;
    Fields: Record<string, FieldEntity>;
    Implements: Record<string, TypeEntity>;
    Methods: Record<string, MethodEntity>;
    Variables: Record<string, VariableEntity>;
}

export interface Entity {
    Canonical: string;
    Comment: string;
    Kind: consts.Kind;
    Name: string;
    Path: string;
}

export interface EnumEntity extends Entity {
    Items: Record<string, string>;
}

export interface FieldEntity extends Entity {
    Modifiers: ModifierEntity;
    Type: TypeEntity;
    Annotations: Record<string, AnnotationEntity>;
}

export interface GenericEntity extends Entity {
    IsParameter: boolean;
    Position: number;
    Type: TypeEntity;
    Variable: VariableEntity;
    Wildcard: WildcardEntity;
}

export interface InterfaceEntity extends Entity {
    Modifiers: ModifierEntity;
    Annotations: Record<string, AnnotationEntity>;
    Extends: Record<string, TypeEntity>;
    Methods: Record<string, MethodEntity>;
    Variables: Record<string, VariableEntity>;
}

export interface Langion extends Entity {
    Modules: Record<string, ModuleEntity>;
    Version: string;
}

export interface MethodEntity extends Entity {
    Modifier: ModifierEntity;
    Returns: TypeEntity;
    Annotations: Record<string, AnnotationEntity>;
    Arguments: Record<string, ArgumentEntity>;
    Variables: Record<string, VariableEntity>;
}

export interface ModifierEntity extends Entity {
    Items: Record<consts.Modifiers, consts.Modifiers>;
}

export interface ModuleEntity extends Entity {
    Exports: Record<string, Entity>;
    Modules: Record<string, ModuleEntity>;
}

export interface TypeEntity extends Entity {
    IsArray: boolean;
    IsParameter: boolean;
    Generics: Record<string, GenericEntity>;
}

export interface ValueEntity extends Entity {
    Content: any;
    Type: TypeEntity;
}

export interface VariableEntity extends Entity {
    Position: number;
}

export interface WildcardEntity extends Entity {
    Lower: Record<string, GenericEntity>;
    Upper: Record<string, GenericEntity>;
}
