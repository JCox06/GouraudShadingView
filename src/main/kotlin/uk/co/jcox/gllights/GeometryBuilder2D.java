package uk.co.jcox.gllights;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class GeometryBuilder2D {

    private GeometryBuilder2D() {

    }

    public static int cube() {

        /*
        Thank you to http://learnopengl.com/ for providing the vertex position and UV mapping and normals information for a cube
        #            http://learnopengl.com/code_viewer.php?code=getting-started/cube_vertices
        //Slightly edited
         */

        float vertices[] = {
                0.0f, 0.0f, 0.0f,  0.0f,  0.0f, -1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,  0.0f,  0.0f, -1.0f, 1.0f, 0.0f,
                1.0f,  1.0f, 0.0f,  0.0f,  0.0f, -1.0f, 1.0f, 1.0f,
                1.0f,  1.0f, 0.0f,  0.0f,  0.0f, -1.0f, 1.0f, 1.0f,
                0.0f,  1.0f, 0.0f,  0.0f,  0.0f, -1.0f,0.0f, 1.0f,
                0.0f, 0.0f, 0.0f,  0.0f,  0.0f, -1.0f, 0.0f, 0.0f,

                0.0f, 0.0f,  1.0f,  0.0f,  0.0f, 1.0f, 0.0f, 0.0f,
                1.0f, 0.0f,  1.0f,  0.0f,  0.0f, 1.0f, 1.0f, 0.0f,
                1.0f,  1.0f,  1.0f,  0.0f,  0.0f, 1.0f,1.0f, 1.0f,
                1.0f,  1.0f,  1.0f,  0.0f,  0.0f, 1.0f, 1.0f, 1.0f,
                0.0f,  1.0f,  1.0f,  0.0f,  0.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 0.0f,  1.0f,  0.0f,  0.0f, 1.0f,0.0f, 0.0f,

                0.0f,  1.0f,  1.0f, -1.0f,  0.0f,  0.0f, 1.0f, 0.0f,
                0.0f,  1.0f, 0.0f, -1.0f,  0.0f,  0.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 0.0f, -1.0f,  0.0f,  0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 0.0f, -1.0f,  0.0f,  0.0f,0.0f, 1.0f,
                0.0f, 0.0f,  1.0f, -1.0f,  0.0f,  0.0f, 0.0f, 0.0f,
                0.0f,  1.0f,  1.0f, -1.0f,  0.0f,  0.0f,1.0f, 0.0f,

                1.0f,  1.0f,  1.0f,  1.0f,  0.0f,  0.0f, 1.0f, 0.0f,
                1.0f,  1.0f, 0.0f,  1.0f,  0.0f,  0.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 0.0f,  1.0f,  0.0f,  0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f,  1.0f,  0.0f,  0.0f, 0.0f, 1.0f,
                1.0f, 0.0f,  1.0f,  1.0f,  0.0f,  0.0f, 0.0f, 0.0f,
                1.0f,  1.0f,  1.0f,  1.0f,  0.0f,  0.0f, 1.0f, 0.0f,

                0.0f, 0.0f, 0.0f,  0.0f, -1.0f,  0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f,  0.0f, -1.0f,  0.0f, 1.0f, 1.0f,
                1.0f, 0.0f,  1.0f,  0.0f, -1.0f,  0.0f,1.0f, 0.0f,
                1.0f, 0.0f,  1.0f,  0.0f, -1.0f,  0.0f,1.0f, 0.0f,
                0.0f, 0.0f,  1.0f,  0.0f, -1.0f,  0.0f,0.0f, 0.0f,
                0.0f, 0.0f, 0.0f,  0.0f, -1.0f,  0.0f,0.0f, 1.0f,

                0.0f,  1.0f, 0.0f,  0.0f,  1.0f,  0.0f, 0.0f, 1.0f,
                1.0f,  1.0f, 0.0f,  0.0f,  1.0f,  0.0f,1.0f, 1.0f,
                1.0f,  1.0f,  1.0f,  0.0f,  1.0f,  0.0f,1.0f, 0.0f,
                1.0f,  1.0f,  1.0f,  0.0f,  1.0f,  0.0f,1.0f, 0.0f,
                0.0f,  1.0f,  1.0f,  0.0f,  1.0f,  0.0f,0.0f, 0.0f,
                0.0f,  1.0f, 0.0f,  0.0f,  1.0f,  0.0f,0.0f, 1.0f,
        };


        return createVertexArrays(vertices);
    }

    private static int createVertexArrays(float[] vertices) {
        final int VAO = GL30.glGenVertexArrays();
        final int VBO = GL15.glGenBuffers();

        GL30.glBindVertexArray(VAO);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);


        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 8 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);

        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        GL20.glEnableVertexAttribArray(2);

        return VAO;
    }
}