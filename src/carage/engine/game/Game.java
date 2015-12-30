package carage.engine.game;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public abstract class Game {

    protected boolean fullscreen = false;

    public static final String DEFAULT_TITLE = "Game";
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;

    public void start() {
        start(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public void start(int width, int height) {
        start(width, height, DEFAULT_TITLE);
    }

    public void start(int width, int height, String title) {
        try {
            DisplayMode found = null;
            for (DisplayMode mode : Display.getAvailableDisplayModes()) {
                if (mode.getWidth() == width && mode.getHeight() == height && mode.isFullscreenCapable()) {
                    found = mode;
                    break;
                }
            }

            if (found == null)
                found = new DisplayMode(width, height);
            Display.setDisplayMode(found);
            Display.setTitle(title);
            Display.setFullscreen(fullscreen);
            ContextAttribs contextAttributes = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
            Display.create(new PixelFormat(/* Alpha Bits */2, /* Depth bits */2, /* Stencil bits */0, /* samples */2), contextAttributes);
            init();
            while (!Display.isCloseRequested()) {
                update();
                render();
                Display.update();
                Display.sync(60);
            }

            Display.destroy();
            System.exit(0);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    protected abstract void init();

    protected abstract void update();

    protected abstract void render();

}
