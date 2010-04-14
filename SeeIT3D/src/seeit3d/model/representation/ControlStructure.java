package seeit3d.model.representation;

import javax.vecmath.Color3f;

public enum ControlStructure {
	IF("if", new Color3f(1.0f, 0.0f, 0.0f)), 
	FOR("for", new Color3f(0.0f, 1.0f, 0.0f)),
	WHILE("while", new Color3f(0.0f,0.0f,1.0f)),
	ELSE("else", new Color3f(1.0f,1.0f,0.0f)),
	ELSE_IF("else if", new Color3f(1.0f,0.0f,1.0f)),
	NONE("", new Color3f(0.8f,0.8f,0.8f));

	private String name;

	private Color3f color;

	private ControlStructure(String name, Color3f color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public Color3f getDefaultColor() {
		return color;
	}
}
