package jmediainspector.helpers.effects;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Resize helper for the main window.
 *
 * @author charlottew
 */
public class ResizeHelper {

    /**
     * Add a listener.
     *
     * @param stage listener
     */
    public static void addResizeListener(final Stage stage) {
        final ResizeListener resizeListener = new ResizeListener(stage);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, resizeListener);
        final ObservableList<Node> children = stage.getScene().getRoot().getChildrenUnmodifiable();
        for (final Node child : children) {
            addListenerDeeply(child, resizeListener);
        }
    }

    /**
     * Add listener.
     *
     * @param node
     * @param listener
     */
    public static void addListenerDeeply(final Node node, final EventHandler<MouseEvent> listener) {
        node.addEventHandler(MouseEvent.MOUSE_MOVED, listener);
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, listener);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, listener);
        node.addEventHandler(MouseEvent.MOUSE_EXITED, listener);
        node.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, listener);
        if (node instanceof Parent) {
            final Parent parent = (Parent) node;
            final ObservableList<Node> children = parent.getChildrenUnmodifiable();
            for (final Node child : children) {
                addListenerDeeply(child, listener);
            }
        }
    }

    private static class ResizeListener implements EventHandler<MouseEvent> {
        private final Stage stage;
        private Cursor cursorEvent = Cursor.DEFAULT;
        private final int border = 4;
        private double startX = 0;
        private double startY = 0;

        public ResizeListener(final Stage stage) {
            this.stage = stage;
        }

        @Override
        public void handle(final MouseEvent mouseEvent) {
            final EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();
            final Scene scene = this.stage.getScene();

            final double mouseEventX = mouseEvent.getSceneX(),
                    mouseEventY = mouseEvent.getSceneY(),
                    sceneWidth = scene.getWidth(),
                    sceneHeight = scene.getHeight();

            if (MouseEvent.MOUSE_MOVED.equals(mouseEventType) == true) {
                if (mouseEventX < this.border && mouseEventY < this.border) {
                    this.cursorEvent = Cursor.NW_RESIZE;
                } else if (mouseEventX < this.border && mouseEventY > sceneHeight - this.border) {
                    this.cursorEvent = Cursor.SW_RESIZE;
                } else if (mouseEventX > sceneWidth - this.border && mouseEventY < this.border) {
                    this.cursorEvent = Cursor.NE_RESIZE;
                } else if (mouseEventX > sceneWidth - this.border && mouseEventY > sceneHeight - this.border) {
                    this.cursorEvent = Cursor.SE_RESIZE;
                } else if (mouseEventX < this.border) {
                    this.cursorEvent = Cursor.W_RESIZE;
                } else if (mouseEventX > sceneWidth - this.border) {
                    this.cursorEvent = Cursor.E_RESIZE;
                } else if (mouseEventY < this.border) {
                    this.cursorEvent = Cursor.N_RESIZE;
                } else if (mouseEventY > sceneHeight - this.border) {
                    this.cursorEvent = Cursor.S_RESIZE;
                } else {
                    this.cursorEvent = Cursor.DEFAULT;
                }
                scene.setCursor(this.cursorEvent);
            } else if (MouseEvent.MOUSE_EXITED.equals(mouseEventType) || MouseEvent.MOUSE_EXITED_TARGET.equals(mouseEventType)) {
                scene.setCursor(Cursor.DEFAULT);
            } else if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType) == true) {
                this.startX = this.stage.getWidth() - mouseEventX;
                this.startY = this.stage.getHeight() - mouseEventY;
            } else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType) == true) {
                if (Cursor.DEFAULT.equals(this.cursorEvent) == false) {
                    if (Cursor.W_RESIZE.equals(this.cursorEvent) == false && Cursor.E_RESIZE.equals(this.cursorEvent) == false) {
                        final double minHeight = this.stage.getMinHeight() > (this.border * 2) ? this.stage.getMinHeight() : (this.border * 2);
                        if (Cursor.NW_RESIZE.equals(this.cursorEvent) == true || Cursor.N_RESIZE.equals(this.cursorEvent) == true || Cursor.NE_RESIZE.equals(this.cursorEvent) == true) {
                            if (this.stage.getHeight() > minHeight || mouseEventY < 0) {
                                this.stage.setHeight(this.stage.getY() - mouseEvent.getScreenY() + this.stage.getHeight());
                                this.stage.setY(mouseEvent.getScreenY());
                            }
                        } else {
                            if (this.stage.getHeight() > minHeight || mouseEventY + this.startY - this.stage.getHeight() > 0) {
                                this.stage.setHeight(mouseEventY + this.startY);
                            }
                        }
                    }

                    if (Cursor.N_RESIZE.equals(this.cursorEvent) == false && Cursor.S_RESIZE.equals(this.cursorEvent) == false) {
                        final double minWidth = this.stage.getMinWidth() > (this.border * 2) ? this.stage.getMinWidth() : (this.border * 2);
                        if (Cursor.NW_RESIZE.equals(this.cursorEvent) == true || Cursor.W_RESIZE.equals(this.cursorEvent) == true || Cursor.SW_RESIZE.equals(this.cursorEvent) == true) {
                            if (this.stage.getWidth() > minWidth || mouseEventX < 0) {
                                this.stage.setWidth(this.stage.getX() - mouseEvent.getScreenX() + this.stage.getWidth());
                                this.stage.setX(mouseEvent.getScreenX());
                            }
                        } else {
                            if (this.stage.getWidth() > minWidth || mouseEventX + this.startX - this.stage.getWidth() > 0) {
                                this.stage.setWidth(mouseEventX + this.startX);
                            }
                        }
                    }
                }

            }
        }
    }
}
