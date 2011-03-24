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
package com.googlecode.java2objc.code;

import japa.parser.ast.expr.EnclosedExpr;

import com.googlecode.java2objc.converters.ExpressionConverter;
import com.googlecode.java2objc.objc.CompilationContext;
import com.googlecode.java2objc.objc.SourceCodeWriter;

/**
 * An enclosed expression in Objective C
 * 
 * @author David Gileadi
 */
public class ObjcExpressionEnclosed extends ObjcExpression {

  private final ObjcExpression inner;

  public ObjcExpressionEnclosed(CompilationContext context, EnclosedExpr expr) {
    this(context, context.getExpressionConverter().to(expr.getInner()));
  }

  public ObjcExpressionEnclosed(CompilationContext context, ObjcExpression inner) {
    super((ObjcType)null);
    this.inner = inner;
  }

  @Override
  public ObjcType getType() {
    return inner.getType();
  }

  @Override
  public void append(SourceCodeWriter writer) {
    writer.append('(').append(inner).append(')');
  }
}
