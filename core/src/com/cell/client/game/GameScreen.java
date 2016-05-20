package com.cell.client.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cell.client.GameClient;
import com.cell.client.Main;
import com.cell.client.game.chat.ChatController;
import com.cell.client.game.chat.ChatWindow;
import com.cell.client.game.player.Cell;
import com.cell.client.game.player.PlayerInput;
import com.cell.server.room.Room;

import java.util.ArrayList;

/**
 * Created by kongo on 17.03.16.
 */
public class GameScreen implements Screen {
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private CameraContoller cameraContoller;

    private final Main main;
    private final GameClient gameClient;
    private Stage stage;
    private final ChatWindow chatWindow;

    private Cell cell;
    private PlayerInput playerInput;
    private ArrayList<Cell> entities;

    private int gridSize = 64;

    private int worldWidth;
    private int worldHeight;


    public GameScreen(Main main, GameClient gameClient, ChatWindow chatWindow, ChatController chatController){
        this.main = main;
        this.gameClient = gameClient;
        this.chatWindow = chatWindow;

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraContoller = new CameraContoller(camera);

        cell = new Cell(0,0, Room.startCellSize, 0);
        playerInput = new PlayerInput(gameClient, chatController, cell);
        entities = new ArrayList<Cell>(1000);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(playerInput);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        initGUI();
    }

    private void initGUI(){
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(chatWindow);

        //rootTable.add(chatWindow).bottom().left();
    }

    public void update(float delta){
        playerInput.processInputs(delta);
        cameraContoller.update(new Vector2(cell.getX(), cell.getY()));
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);

        drawGrid(shapeRenderer);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        ArrayList<Cell> entitiesToRender = new ArrayList<Cell>(entities);
        for(Cell cell : entitiesToRender) {
            if(cell != this.cell)
                cell.render(shapeRenderer);
        }
        cell.render(shapeRenderer);
        shapeRenderer.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    private void drawGrid(ShapeRenderer shapeRenderer){
        Rectangle viewBounds = cameraContoller.getViewBounds();
//        final int xMin = Math.max(0, (int) (viewBounds.x / gridSize));
//        final int xMax = Math.min(worldWidth / gridSize + 1, (int) ((viewBounds.x + viewBounds.width + gridSize) / gridSize));
//
//        final int yMin = Math.max(0, (int) (viewBounds.y / gridSize));
//        final int yMax = Math.min(worldHeight / gridSize + 1, (int) ((viewBounds.y + viewBounds.height + gridSize) / gridSize));

        int xMin = (int) (viewBounds.x / gridSize);
        int xMax = (int) ((viewBounds.x + viewBounds.width + gridSize) / gridSize);
//        System.out.println(xMin + " " + xMax + " " + viewBounds.x + " " + viewBounds.width);

        int yMin = (int) (viewBounds.y / gridSize);
        int yMax = (int) ((viewBounds.y + viewBounds.height + gridSize) / gridSize);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        int x = xMin * gridSize;
        for (int i = xMin; i < xMax; i++) {
            /*if(x == 0 || x == worldWidth)
                shapeRenderer.setColor(Color.PURPLE);
            else
                shapeRenderer.setColor(Color.BLACK);*/

            shapeRenderer.line(x, (yMin - 1) * gridSize, x, yMax * gridSize);
            x+=gridSize;
        }
        int y = yMin * gridSize;
        for (int i = yMin; i < yMax; i++) {
            /*if(y == 0 || y == worldHeight)
                shapeRenderer.setColor(Color.PURPLE);
            else
                shapeRenderer.setColor(Color.BLACK);*/
            shapeRenderer.line((xMin  -1) * gridSize, y, xMax * gridSize, y);
            y+=gridSize;
        }
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        //TODO hide
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public ChatWindow getChatWindow() {
        return chatWindow;
    }

    public Cell getCell() {
        return cell;
    }

    public PlayerInput getPlayerInput() {
        return playerInput;
    }

    public ArrayList<Cell> getEntities() {
        return entities;
    }

    public void setWorldHeight(int worldHeight) {
        this.worldHeight = worldHeight;
    }

    public void setWorldWidth(int worldWidth) {
        this.worldWidth = worldWidth;
    }
}
