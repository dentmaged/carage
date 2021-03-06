package carage.engine;

import org.lwjgl.util.vector.Matrix4f;

import carage.engine.utils.ShaderProgram;
import carage.engine.utils.Texture;

public interface Renderable {

	public VertexArrayObject getVAO();
	public int getVAOId();
	
	public boolean hasIBO();
	public IndexBufferObject getIBO();
	public int getIBOId();
	
	public boolean hasTexture();
	public Texture getTexture();
	public int getTextureId();
	
	public Matrix4f getTransformationMatrix();
	public void applyTransformationsToMatrix(Matrix4f modelMatrix);
	
	public void setMaterial(Material material);
	public boolean hasMaterial();
	public Material getMaterial();
	
	public ShaderProgram getShader();
	
}
