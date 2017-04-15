package model;

import java.util.Map;

public class PersistentWebElement {
	
	// STATIC FIELDS
	public static final String MAP_EXTENSION_NAME = "_PERSISTENT";
	public static final String[] ATTRIBUTES_TO_COPY = {"onclick"};
	
	// NOT STATIC FIELDS
	private String text;
	private String tagName;
	private Map<String, String> attributes;
	private PersistentWebElement persistentParentElement;
	
	// CONSTRUCTORS
	public PersistentWebElement(){
	}
	
	public PersistentWebElement(String tagName, String text){
		this.tagName = tagName;
		this.text = text;
	}
	
	public PersistentWebElement(String tagName, String text, Map<String, String> attributes){
		this(tagName, text);
		this.attributes = attributes;
	}
	
	public PersistentWebElement(String tagName, String text, PersistentWebElement persistentParentElement){
		this(tagName, text);
		this.persistentParentElement = persistentParentElement;
	}
	
	public PersistentWebElement(String tagName, String text, Map<String, String> attributes, PersistentWebElement persistentParentElement){
		this(tagName, text, attributes);
		this.persistentParentElement = persistentParentElement;
	}
	
	// SETTERS
	public void setText(String text) {
		this.text = text;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public void setPersistentParentElement(PersistentWebElement persistentParentElement) {
		this.persistentParentElement = persistentParentElement;
	}
	
	// GETTERS
	public String getText() {
		return text;
	}

	public String getTagName() {
		return tagName;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public String getAttribute(String attributeName){
		if(attributes == null){
			return null;
		}
		return attributes.get(attributeName);
	}

	public PersistentWebElement getPersistentParentElement() {
		return persistentParentElement;
	}
	
	// STATIC METHODS
	public static String getPersistentMapKey(String notPersistentKey){
		return notPersistentKey + MAP_EXTENSION_NAME;
	}

}
