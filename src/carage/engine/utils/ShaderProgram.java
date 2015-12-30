package carage.engine.utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ShaderProgram {

    private int id;
    private HashMap<String, Integer> uniformLocations = new HashMap<>();

    public ShaderProgram(String resourceNameWithoutExtension) {
        this(resourceNameWithoutExtension + ".v", resourceNameWithoutExtension + ".g", resourceNameWithoutExtension + ".f");
    }

    public ShaderProgram(String vertexResourceName, String fragmentResourceName) {
        this(vertexResourceName, null, fragmentResourceName);
    }

    public ShaderProgram(String vertexResourceName, String geometryResourceName, String fragmentResourceName) {
        id = glCreateProgram();

        compileAndAttach(vertexResourceName, GL_VERTEX_SHADER);
        compileAndAttach(fragmentResourceName, GL_FRAGMENT_SHADER);
        compileAndAttach(geometryResourceName, GL_GEOMETRY_SHADER);

        glLinkProgram(id);
        if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE)
            throw new RuntimeException(glGetProgramInfoLog(id, glGetProgrami(id, GL_INFO_LOG_LENGTH)));
    }

    public void bind() {
        glUseProgram(id);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public int getId() {
        return id;
    }

    private void compileAndAttach(String resource, int type) {
        InputStream stream = ShaderProgram.class.getResourceAsStream("/res/shaders/" + resource);
        if (stream == null) {
            if (type != GL_GEOMETRY_SHADER)
                throw new RuntimeException(resource + " does not exist!");

            return;
        }

        StringBuilder source = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int shaderId = glCreateShader(type);
        glShaderSource(shaderId, source.toString());
        glCompileShader(shaderId);

        String compileLog = "";
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Shader " + resource + " not compiled: " + compileLog);

        if (!compileLog.isEmpty())
            System.err.println(resource + ": " + compileLog);

        glAttachShader(id, shaderId);
    }

    // http://stackoverflow.com/questions/3158730/java-3-dots-in-parameters
    public void bindAttributeLocations(String... variableNames) {
        int i = 0;
        for (String var : variableNames) {
            glBindAttribLocation(id, i, var);
            i++;
        }

        glLinkProgram(id);
        if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE)
            throw new RuntimeException(glGetProgramInfoLog(id, glGetProgrami(id, GL_INFO_LOG_LENGTH)));
    }

    public int getUniformLocation(String name) {
        if (!uniformLocations.containsKey(name)) {
            return fetchUniformLocation(name);
        }
        return uniformLocations.get(name);
    }

    private int fetchUniformLocation(String name) {
        int location = glGetUniformLocation(this.id, name);
        uniformLocations.put(name, location);
        return location;
    }

}
