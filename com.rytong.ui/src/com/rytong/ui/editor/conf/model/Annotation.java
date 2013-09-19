package com.rytong.ui.editor.conf.model;

/**
 * @author lu.jingbo@rytong.com
 * 
 */
public class Annotation {
	// {element, [{annotation, [{type, tuple},
	// {name, "adapter"},
	// {docstring, "adapter doc"},
	// {repeat, {1,n}},
	// {tag, adapter},
	// {value, list}]},
	private String type;
	private String name;
	private String docstring;
	private Tag tag;
	private String value;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocstring() {
		return docstring;
	}

	public void setDocstring(String docstring) {
		this.docstring = docstring;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public interface Type {
		public static final String TUPLE = "tuple";
		public static final String LIST = "list";
		public static final String STRING = "string";
		public static final String INTEGER = "integer";
		public static final String ATOM = "atom";
		public static final String BOOLEAN = "boolean";
		public static final String TERM = "term";
	}

	public class Tag {
		private Type type;
		private String value;

		public Tag(Type type, String value) {
			this.type = type;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}
	}

	public class Repeat {
		private int min = 1;
		private int max = 1;

		public int getMin() {
			return min;
		}

		public void setMin(int min) {
			this.min = min;
		}

		public int getMax() {
			return max;
		}

		public void setMax(int max) {
			this.max = max;
		}
	}
}
