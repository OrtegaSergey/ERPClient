package DesktopClient.Services;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class WindowManageHelper {
    private boolean resizeBottom;
    private boolean resizeLeft;
    private boolean resizeRight;
    private boolean remove;
    private double dx;
    private double dy;

    private double xOffset;
    private double yOffset;


    public WindowManageHelper(Stage stage, Scene scene){
        //Пока не отменяет курсор при переходе на внутренний объект
//        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                System.out.println(checkMousePosition(stage, scene, event));
//                switch (checkMousePosition(stage, scene, event)){
//                    case 1:
//                        scene.setCursor(Cursor.W_RESIZE);
//                        break;
//                    case 2:
//                        scene.setCursor(Cursor.SW_RESIZE);
//                        break;
//                    case 3:
//                        scene.setCursor(Cursor.S_RESIZE);
//                        break;
//                    case 4:
//                        scene.setCursor(Cursor.SE_RESIZE);
//                        break;
//                    case 5:
//                        scene.setCursor(Cursor.E_RESIZE);
//                        break;
//                    case 6:
//                        scene.setCursor(Cursor.HAND);
//                        break;
//                        default:
//                            scene.setCursor(Cursor.DEFAULT);
//                }
//            }
//        });

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                remove = false;
                resizeLeft = false;
                resizeRight = false;
                resizeBottom = false;
                switch (checkMousePosition(stage, event)){
                    case 1:
                        dx = stage.getX() - event.getScreenX();
                        resizeLeft = true;
                        break;
                    case 2:
                        dy = stage.getHeight() - event.getY();
                        resizeBottom = true;
                        resizeLeft = true;
                        break;
                    case 3:
                        dy = stage.getHeight() - event.getY();
                        resizeBottom = true;
                        break;
                    case 4:
                        dx = stage.getWidth() - event.getX();
                        dy = stage.getHeight() - event.getY();
                        resizeBottom = true;
                        resizeRight = true;
                        break;
                    case 5:
                        dx = stage.getWidth() - event.getX();
                        resizeRight = true;
                        break;
                    case 6:
                        xOffset = stage.getX() - event.getScreenX();
                        yOffset = stage.getY() - event.getScreenY();
                        remove = true;
                        break;
                }
            }
        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (stage.getWidth()<stage.getMinWidth() || stage.getHeight()<stage.getMinHeight()
                || stage.getWidth()>stage.getMaxWidth() || stage.getHeight()>stage.getMaxHeight()) return;
                if (remove){
                    stage.setX(event.getScreenX() + xOffset);
                    stage.setY(event.getScreenY() + yOffset);
                }
                if (resizeRight){
                        stage.setWidth(event.getX() + dx);
                }
                if (resizeBottom){
                        stage.setHeight(event.getY() + dy);
                }
                if (resizeLeft){
                        xOffset = stage.getX();
                        stage.setX(event.getScreenX() - dx);
                        stage.setWidth(stage.getWidth() + xOffset - stage.getX());
                }
            }
        });

        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                    if (stage.getWidth() < stage.getMinWidth()) stage.setWidth(stage.getMinWidth());
                    if (stage.getHeight() < stage.getMinHeight()) stage.setHeight(stage.getMinHeight());
                    if (stage.getWidth()>stage.getMaxWidth()) stage.setWidth(stage.getMaxWidth());
                    if (stage.getHeight()>stage.getMaxHeight()) stage.setHeight(stage.getMaxHeight());
            }
        });
    }


    //Будет возвращать область, в которой находится мышь. 1 - левая граница, 2 - левый нижний угол
    //3 - нижняя граница, 4 - правый нижний угол, 5 - правая граница, 6 - верхняя область (заголовок)
    private int checkMousePosition(Stage stage, MouseEvent event){
        if (event.getY()<=23){
            return 6;
        } else if (Math.abs(event.getX())<3 && (stage.getHeight() - event.getY()) > 3 && event.getY() > 25){
            return 1;
        } else if (Math.abs(event.getX())<=3 && Math.abs(stage.getHeight()-event.getY())<=3){
            return 2;
        } else if (event.getX()>3 && (stage.getWidth() - event.getX())>3 && Math.abs(stage.getHeight() - event.getY())<3){
            return 3;
        } else if (Math.abs(stage.getWidth() - event.getX())<=3 && Math.abs(stage.getHeight() - event.getY())<=3){
            return 4;
        } else if (Math.abs(stage.getWidth() - event.getX())<3 && (stage.getHeight()-event.getY())>3 && event.getY() > 25){
            return 5;
        } else return 0;
    }

}
