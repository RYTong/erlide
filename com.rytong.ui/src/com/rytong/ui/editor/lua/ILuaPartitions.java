package com.rytong.ui.editor.lua;

import org.eclipse.jface.text.IDocument;


public interface ILuaPartitions {
	public final static String LUA_PARTITIONING = "__lua_partitionsing";

    public static final String LUA = "__lua";
    public static final String LUA_NUMBER = "__lua_number"; //$NON-NLS-1$
	public static final String LUA_COMMENT = "__lua_comment"; //$NON-NLS-1$
	public static final String LUA_MULTI_LINE_COMMENT = "__lua_multi_line_comment"; //$NON-NLS-1$
	public static final String LUA_STRING = "__lua_string"; //$NON-NLS-1$
	public static final String LUA_SINGLE_QUOTE_STRING = "__lua_single_quote_string"; //$NON-NLS-1$
	public static final String LUA_MULTI_LINE_STRING = "__lua_multi_line_string"; //$NON-NLS-1$
	public static final String LUA_DOC = "__lua_doc"; //$NON-NLS-1$
	public static final String LUA_DOC_MULTI = "__lua_doc_multi_line"; //$NON-NLS-1$

	public static final String COMMENT_STRING = "--"; //$NON-NLS-1$


	static final String[] LUA_PARTITION_TYPES = new String[] {
		IDocument.DEFAULT_CONTENT_TYPE,
		ILuaPartitions.LUA_NUMBER,
		ILuaPartitions.LUA_STRING,
		ILuaPartitions.LUA_SINGLE_QUOTE_STRING,
		ILuaPartitions.LUA_MULTI_LINE_STRING,
		ILuaPartitions.LUA_COMMENT,
		ILuaPartitions.LUA_MULTI_LINE_COMMENT };


}
