package uk.co.jcox.gllights

import org.joml.Math
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30
import org.lwjgl.stb.STBImage
import java.io.IOException
import java.nio.IntBuffer
import java.nio.file.Files
import java.nio.file.Paths

private val window = Window("LWJGL OpenGL Test", 1000, 1000)
private val mainProgram = ShaderProgram("Main")
private val lightProgram = ShaderProgram("Program")

private val camera = Camera()

private const val CAM_SENSE = 0.25f
private const val CAM_SPEED = 5f;

private var cubeObj: ObjRepresentative? = null
private var lightObj: ObjRepresentative? = null

private var lightPosition = Vector3f(2.0f, 0.0f, 2.0f)
private var lightColour = Vector3f(1f, 1f, 1f)

fun main() {

    window.createGlContext()
    GL.createCapabilities()
    GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    GL11.glEnable(GL11.GL_DEPTH_TEST)

    val cubeGeometry = GeometryBuilder2D.cube()
    var texture = loadTexture("data/textures/default.png")
    var material = Material(Vector3f(1.0f, 1.0f, 1.0f), texture)

    cubeObj = ObjRepresentative(cubeGeometry, material)

    texture = loadTexture("data/textures/lightsource.png")
    material = Material(Vector3f(1.0f, 1.0f, 1.0f), texture)

    lightObj = ObjRepresentative(cubeGeometry, material)

    val lightSourceVshSrc = readFile("data/shaders/lightSource.vsh")
    val lightSourceFshSrc = readFile("data/shaders/lightSource.fsh")
    val gShadVshSrc = readFile("data/shaders/gshad.vsh")
    val gShadFshSrc = readFile("data/shaders/gshad.fsh")

    mainProgram.createProgram(ShaderProgram.ShaderInfo(ShaderProgram.ShaderType.VERTEX, gShadVshSrc), ShaderProgram.ShaderInfo(ShaderProgram.ShaderType.FRAGMENT, gShadFshSrc))
    lightProgram.createProgram(ShaderProgram.ShaderInfo(ShaderProgram.ShaderType.VERTEX, lightSourceVshSrc), ShaderProgram.ShaderInfo(ShaderProgram.ShaderType.FRAGMENT, lightSourceFshSrc))


    renderLoop()

    window.terminate()
}


private fun renderLoop() {

    window.setMouseFunc {
        if (window.mousePressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            camera.rotate(window.getxOffset() * CAM_SENSE, window.getyOffset() * CAM_SENSE)
        }
    }

    var lastTimeFrame = 0.0f
    var deltaTime: Float

    while(! window.shouldClose()) {
        var time = window.timeElapsed.toFloat()
        deltaTime = time - lastTimeFrame
        lastTimeFrame = time
        GL11.glViewport(0, 0, window.width, window.height)

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        render()
        input(deltaTime)
        update(deltaTime)

        window.runWindowUpdates()
    }

}


private fun render() {
    val proj = camera.getProjection(window.width / window.height.toFloat())
    mainProgram.bind()
    mainProgram.send("projMatrix", proj)
    mainProgram.send("camMatrix", camera.lookAt)
    mainProgram.send("modelMatrix", Matrix4f())
    mainProgram.send("lightColour", lightColour)
    mainProgram.send("lightPos", lightPosition)
    drawObjRep(cubeObj!!, mainProgram)


    lightProgram.bind()
    lightProgram.send("projMatrix", proj)
    lightProgram.send("camMatrix", camera.lookAt)
    lightProgram.send("modelMatrix", Matrix4f().translate(lightPosition).scale(0.2f))
    drawObjRep(lightObj!!, lightProgram)
}


private fun input(deltaTime: Float) {
    if (window.isPressed(GLFW.GLFW_KEY_W)) {
        camera.moveForward(CAM_SPEED * deltaTime)
    }
    if (window.isPressed(GLFW.GLFW_KEY_S)) {
        camera.moveForward(-CAM_SPEED * deltaTime)
    }
    if (window.isPressed(GLFW.GLFW_KEY_D)) {
        camera.moveRight(CAM_SPEED * deltaTime)
    }
    if (window.isPressed(GLFW.GLFW_KEY_A)) {
        camera.moveRight(-CAM_SPEED * deltaTime)
    }
}


private fun update(deltaTime: Float) {
    val radius = 1.5f;
    val speed = 1;

    lightPosition.x = radius * Math.sin(window.timeElapsed * speed).toFloat()
    lightPosition.z = radius * Math.cos(window.timeElapsed * speed).toFloat()
    lightPosition.y = radius * Math.sin(window.timeElapsed * speed).toFloat()
}


private fun drawObjRep(obj: ObjRepresentative, program: ShaderProgram) {
    GL30.glBindVertexArray(obj.geometry)
    GL15.glActiveTexture(GL15.GL_TEXTURE0)
    GL15.glBindTexture(GL15.GL_TEXTURE_2D, obj.material.textureId)
    program.send("main2D", 0)
    GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 36)
}

private fun readFile(file: String): String {
    try {
        val path = Paths.get(file)
        println(path.toAbsolutePath())
        return Files.readString(path)
    } catch (e: IOException) {
        e.printStackTrace()
    }


    return ""
}


private fun loadTexture(pngPath: String): Int {

    STBImage.stbi_set_flip_vertically_on_load(true)

    println("Loading texture: $pngPath")

    val widthBuff: IntBuffer = BufferUtils.createIntBuffer(1)
    val heightBuff: IntBuffer = BufferUtils.createIntBuffer(1)
    val nrChannels: IntBuffer = BufferUtils.createIntBuffer(1)

    val data = STBImage.stbi_load(pngPath, widthBuff, heightBuff, nrChannels, 4)

    val textureId = GL11.glGenTextures()
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId)

    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, widthBuff.get(), heightBuff.get(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data)
    GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D)
    if (data != null) {
        STBImage.stbi_image_free(data)
    }

    println("Texture loading completed")

    return textureId
}
