package com.cell.client.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by kongo on 06.04.16.
 */
public class CameraContoller {
    private OrthographicCamera camera;
    private float lerp = 0.05f;// 0,1
    private Rectangle viewBounds;
    float viewWidth;
    float viewHeight;

    public CameraContoller(OrthographicCamera camera){
        this.camera = camera;
        viewBounds = new Rectangle();
    }

    public void update(Vector2 newPosition){
        Vector3 position = camera.position;
        position.x += (newPosition.x - position.x) * lerp;
        position.y += (newPosition.y - position.y) * lerp;
        camera.update();

        viewWidth = camera.viewportWidth * camera.zoom;
        viewHeight = camera.viewportHeight * camera.zoom;
        viewBounds.set(camera.position.x - viewWidth / 2, camera.position.y - viewHeight / 2, viewWidth, viewHeight);
    }

    public Rectangle getViewBounds() {
        return viewBounds;
    }
}
