package net.henryco.sqlightning.reflect.annotations.ameta.table.stdprocessor;

import net.henryco.sqlightning.reflect.annotations.ameta.table.MetaTableExec;
import net.henryco.sqlightning.reflect.table.stb.SimpTableBuilder;

import java.lang.annotation.Annotation;

/**
 * Created by HenryCo on 12/05/17.
 */

public class DropProcessor implements MetaTableExec {

	@Override
	public SimpTableBuilder process(SimpTableBuilder builder, Annotation self) {
		return builder.dropIFExists();
	}
}
