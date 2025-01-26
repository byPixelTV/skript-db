package com.btk5h.skriptdb;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionInfo;
import ch.njol.skript.lang.VariableString;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;

public class SkriptUtil {

  private static final Field STRINGS;

  static {
    Field tempField = null;
    try {
      // Check for the correct field name in VariableString class
      tempField = VariableString.class.getDeclaredField("strings");
      tempField.setAccessible(true);
    } catch (NoSuchFieldException e) {
      Skript.error("Skript's 'strings' field could not be resolved.");
      e.printStackTrace();
    }
    STRINGS = tempField;
  }

  public static Object[] getTemplateString(VariableString vs) {
    if (STRINGS == null) {
      throw new IllegalStateException("The 'strings' field is not accessible.");
    }
    try {
      return (Object[]) STRINGS.get(vs);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static @NotNull Expression<?> getExpressionFromInfo(ExpressionInfo<?, ?> expressionInfo) {
    try {
      Constructor<?> constructor = Objects.requireNonNull(expressionInfo.getExpressionType()).getClass().getDeclaredConstructor();
      return (Expression<?>) constructor.newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Failed to create an instance of Expression", e);
    }
  }
}