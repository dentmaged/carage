package carage;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

import java.util.EnumMap;
import java.util.HashMap;

import lenz.opengl.utils.Texture;

public class VertexArrayObject {

	private int id = 0;
		
	private EnumMap<ShaderAttribute, VertexBufferObject> vbos = new EnumMap<>(ShaderAttribute.class); // TODO This might actually not be needed?
	private IndexBufferObject ibo = null;
	
	public VertexArrayObject() {
		generateId();
	}
	
	public void addVBO(VertexBufferObject vbo, ShaderAttribute shaderAttribute) {
		vbos.put(shaderAttribute, vbo);
		bind();
		vbo.bind();
		glVertexAttribPointer(shaderAttribute.getLocation(), vbo.getChunkSize(), GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(shaderAttribute.getLocation());	// Enable the new VBO
		vbo.unbind();
		unbind();
	}

	// TODO Why would we want to access the VBOs later on? This might be unnecessary!
	public VertexBufferObject getVBO(ShaderAttribute shaderAttribute) {
		return vbos.get(shaderAttribute);
	}
	
	public void bind() {
		glBindVertexArray(id);
	}
	
	public void unbind() {
		glBindVertexArray(0);
	}
	
	public int getId() {
		return id;
	}
	
	public void setIBO(IndexBufferObject ibo) {
		this.ibo = ibo;
	}
	
	public boolean hasIBO() {
		return (ibo != null);
	}
	
	public IndexBufferObject getIBO() {
		return ibo;
	}
	
	public int getIBOId() {
		return (hasIBO()) ? ibo.getId() : 0;
	}
		
	public void delete() {
		glDeleteVertexArrays(id);
	}
		
	private void generateId() {
		id = (id == 0) ? glGenVertexArrays() : id;
	}
	
}
