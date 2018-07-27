# Langion
Langion is a JSON format definition. Sometimes you need to integrate with a system that is written in other language. For example your server is written in Java and your client is written in TypeScript. So when someone will change something on server, TypeScript will not be able no highlight errors because server is written in Java. But the information about types is close, just written in different language. What you need is to extract types from another language and generate types in you languages. 
The Langion format defines common information to describe types in different language. Using this format you can generate any code you want.
## How to run on Java
In order to generate Langion you need to add `langion-maven-plugin` to your `pom.xml`

    <plugin>
        <groupId>com.github.langion</groupId>
        <artifactId>langion-maven-plugin</artifactId>
        <version>1.5</version>
        <executions>
            <execution>
                <configuration>
                    <outFileName>{path to output file}</outFileName>
                    <srcBaseJavaFolders>{absolute path to src folder}</srcBaseJavaFolders>
                    <pattern>
                        <pattern>{Pattern of canonical paths}</pattern>
                    </pattern>
                </configuration>
                <id>java-to-json</id>
                <goals>
                    <goal>generate</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

## Form 
### Kind
Enum - All the entities that has Langion

|Key|Value|
|--|--|
|Annotation|Annotation|
|Enum|Enum|
|Field|Field|
|Generic|Generic|
|Interface|Interface|
|Langion|Langion|
|Method|Method|
|Class|Class|
|Modifier|Modifier|
|Package|Package|
|Type|Type|
|Value|Value|
|Variable|Variable|
|Wildcard|Wildcard|

### Modifiers
Enum - Modifiers that applied to fields

|Key|Value|
|--|--|
|Private|Private|
|Protected|Protected|
|Public|Public|

### Entity

|Key|Type|Comment|
|--|--|--|
|Canonical|string|
|Comment|string|
|Kind|[Kind](#kind)|
|Name|string|
|Path|string|

### AnnotationEntity
**Extends:** [Entity](#entity)

|Key|Type|Comment|
|--|--|--|
|Items|Map<string, [ValueEntity](#valueentity)>|

### ArgumentEntity
**Extends:** [Entity](#entity)

|Key|Type|Comment|
|--|--|--|
|Position|number|
|Type|[TypeEntity](#typeentity)|
|Annotations|Map<string, [AnnotationEntity](#annotationentity)>|

### ClassEntity
**Extends:** [Entity](#entity)

|Key|Type|Comment|
|--|--|--|
|Extends|[TypeEntity](#typeentity)|
|Modifiers|[ModifierEntity](#modifierentity)|
|Annotations|Map<string, [ValueEntity](#annotationentity)>|
|Fields|Map<string, [FieldEntity](#fieldentity)>|
|Implements|Map<string, [TypeEntity](#typeentity)>|
|Methods|Map<string, [MethodEntity](#methodentity)>|
|Variables|Map<string, [VariableEntity](#variableentity)>|

### EnumEntity
**Extends:** [Entity](#entity)

|Key|Type|Comment|
|--|--|--|
|Items|Map<string, string>|

### FieldEntity
**Extends:** [Entity](#entity)

|Key|Type|Comment|
|--|--|--|
|Modifiers|[ModifierEntity](#modifierentity)|
|Type|[TypeEntity](#typeentity)|
|Annotations|Map<string, [AnnotationEntity](#annotationentity)>|

### GenericEntity
**Extends:** [Entity](#entity)

|Key|Type|Comment|
|--|--|--|
|IsParameter|boolean|
|Position|number|
|Type|[TypeEntity](#typeentity)|
|Variable|[VariableEntity](#variableentity)|
|Wildcard|[WildcardEntity](#wildcardentity)|

### InterfaceEntity
**Extends:** [Entity](#entity)

|Key|Type|Comment|
|--|--|--|
|Modifiers|[ModifierEntity](#modifierentity)|
|Annotations|Map<string, [AnnotationEntity](#annotationentity)>|
|Extends|Map<string, [TypeEntity](#typeentity)>|
|Methods|Map<string, [MethodEntity](#methodentity)>|
|Variables|Map<string, [VariableEntity](#variableentity)>|

### Langion
**Extends:** [Entity](#entity)

|Key|Type|Comment|
|--|--|--|
|Modules|Map<string, [ModuleEntity](#moduleentity)>|
|Version|string|

### InterfaceEntity
**Extends:** [Entity](#entity)

|Key|Type|Comment|
|--|--|--|
|Modifier|[ModifierEntity](#modifierentity)|
|Returns|[TypeEntity](#typeentity)|
|Annotations|Map<string, [AnnotationEntity](#annotationentity)>|
|Arguments|Map<string, [ArgumentEntity](#argumententity)>|
|Variables|Map<string, [VariableEntity](#variableentity)>|

### ModifierEntity
**Extends:** [Entity](#entity)

|Key|Type|Comment|
|--|--|--|
|Items|Map<[Modifiers](#modifiers), [Modifiers](#modifiers)>|

### ModuleEntity
**Extends:** [Entity](#entity)

|Key|Type|Comment|
|--|--|--|
|Exports|Map<string, [Entity](#entity)>|
|Modules|Map<string, [ModuleEntity](#moduleentity)>|

### TypeEntity
**Extends:** [Entity](#entity)

|Key|Type|Comment|
|--|--|--|
|IsArray|boolean|
|IsParameter|boolean|
|Generics|Map<string, [GenericEntity](#genericentity)>|

### ValueEntity
**Extends:** [Entity](#entity)

|Key|Type|Comment|
|--|--|--|
|Content|any|
|Type|[TypeEntity](#typeentity)|

### VariableEntity
**Extends:** [Entity](#entity)

|Key|Type|Comment|
|--|--|--|
|Position|number|

### WildcardEntity
**Extends:** [Entity](#entity)

|Key|Type|Comment|
|--|--|--|
|Lower|Map<string, [GenericEntity](#genericentity)>|
|Upper|Map<string, [GenericEntity](#genericentity)>|
