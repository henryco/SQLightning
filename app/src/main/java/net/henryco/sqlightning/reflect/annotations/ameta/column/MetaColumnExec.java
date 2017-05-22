package net.henryco.sqlightning.reflect.annotations.ameta.column;

import net.henryco.sqlightning.reflect.table.stb.StbColumn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by HenryCo on 10/05/17.
 */
@FunctionalInterface
public interface MetaColumnExec {
	StbColumn process(final StbColumn column, Annotation self, Field annotatedField);
}
