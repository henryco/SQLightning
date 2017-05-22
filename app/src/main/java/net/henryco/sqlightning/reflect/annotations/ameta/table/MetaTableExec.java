package net.henryco.sqlightning.reflect.annotations.ameta.table;

import net.henryco.sqlightning.reflect.table.stb.SimpTableBuilder;

import java.lang.annotation.Annotation;


/**
 * Created by HenryCo on 12/05/17.
 */

public interface MetaTableExec {

	SimpTableBuilder process(final SimpTableBuilder builder, final Annotation self);
}
