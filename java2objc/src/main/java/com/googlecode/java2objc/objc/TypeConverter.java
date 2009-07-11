/*
 * Copyright (C) 2009 Inderjeet Singh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.java2objc.objc;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.type.ClassOrInterfaceType;

import java.util.List;
import java.util.Set;

/**
 * Provides utility methods to convert {@link TypeDeclaration} to {@link ObjcType}
 * 
 * @author Inderjeet Singh
 */
public final class TypeConverter {
  private final CompilationContext context;
  private final Set<ObjcType> imports;

  public TypeConverter(CompilationContext context, Set<ObjcType> imports) {
    this.context = context;
    this.imports = imports;
  }

  public ObjcType to(ClassOrInterfaceDeclaration type) {
    UserDefinedObjcTypeBuilder typeBuilder = 
      new UserDefinedObjcTypeBuilder(context, type.getName(), type.isInterface(), imports);
    buildType(typeBuilder, type);
    return typeBuilder.build();
  }

  private void buildType(UserDefinedObjcTypeBuilder typeBuilder, ClassOrInterfaceDeclaration type) {
    if (type.getExtends() != null) {
      for (ClassOrInterfaceType extendedClass : type.getExtends()) {
        typeBuilder.addBaseClass(ObjcType.getTypeFor(extendedClass.getName()));
      }
    }
    if (type.getImplements() != null) {
      for (ClassOrInterfaceType implementedClass : type.getImplements()) {
        typeBuilder.addBaseClass(ObjcType.getTypeFor(implementedClass.getName()));
      }
    }
    List<BodyDeclaration> members = type.getMembers();
    for (BodyDeclaration member : members) {
      if (member instanceof FieldDeclaration) {
        FieldDeclaration field = (FieldDeclaration) member;
        for (VariableDeclarator var : field.getVariables()) {
          ObjcField objcField = new ObjcField(field.getType(), var);
          typeBuilder.addField(objcField);
        }
      } else if (member instanceof MethodDeclaration) {
        MethodDeclaration method = (MethodDeclaration) member;
        typeBuilder.addMethod(new ObjcMethod(context, method));
      } else if (member instanceof ConstructorDeclaration) {
        ConstructorDeclaration constructor = (ConstructorDeclaration) member;
        typeBuilder.addMethod(new ObjcMethodInit(context, constructor));
      }
    }
  }
}
