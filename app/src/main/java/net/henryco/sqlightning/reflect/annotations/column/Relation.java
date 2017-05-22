package net.henryco.sqlightning.reflect.annotations.column;

import net.henryco.sqlightning.reflect.annotations.ameta.column.MetaColumn;
import net.henryco.sqlightning.reflect.annotations.ameta.column.MetaRelation;
import net.henryco.sqlightning.reflect.annotations.ameta.column.stdprocessor.ColumnRelationProcessor;
import net.henryco.sqlightning.reflect.annotations.ameta.column.stdprocessor.RelationTableProcessor;
import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaLoader;
import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaSaver;
import net.henryco.sqlightning.reflect.annotations.ameta.io.MetaUpdater;
import net.henryco.sqlightning.reflect.annotations.ameta.io.stdprocessor.RelationLoadProcessor;
import net.henryco.sqlightning.reflect.annotations.ameta.io.stdprocessor.RelationSaveProcessor;
import net.henryco.sqlightning.reflect.annotations.ameta.io.stdprocessor.RelationUpdateProcessor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by HenryCo on 10/05/17.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@MetaRelation(RelationTableProcessor.class)
@MetaColumn(ColumnRelationProcessor.class)
@MetaLoader(RelationLoadProcessor.class)
@MetaSaver(RelationSaveProcessor.class)
@MetaUpdater(RelationUpdateProcessor.class)
public @interface Relation {

	String RELATED_KEY = "related_key";
}
