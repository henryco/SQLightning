package net.henryco.sqlightning.reflect.annotations.repository;

import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntityLoader;
import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntitySaver;
import net.henryco.sqlightning.reflect.annotations.ameta.entity.MetaEntityUpdater;
import net.henryco.sqlightning.reflect.annotations.ameta.entity.stdprocessor.EntityLoadProcessor;
import net.henryco.sqlightning.reflect.annotations.ameta.entity.stdprocessor.EntitySaveProcessor;
import net.henryco.sqlightning.reflect.annotations.ameta.entity.stdprocessor.EntityUpdateProcessor;

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
@Target(ElementType.TYPE)
@MetaEntityLoader(EntityLoadProcessor.class)
@MetaEntitySaver(EntitySaveProcessor.class)
@MetaEntityUpdater(EntityUpdateProcessor.class)
public @interface Entity {

}
