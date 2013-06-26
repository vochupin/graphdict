package net.slezok.dots.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.esotericsoftware.tablelayout.BaseTableLayout;

import net.slezok.dots.Assets;
import net.slezok.dots.Bridge;
import net.slezok.dots.BridgesGestureHandler;
import net.slezok.dots.InputHandler;
import net.slezok.dots.Dots;
import net.slezok.dots.OverlapTester;
import net.slezok.dots.actors.BridgesGrid;
import net.slezok.dots.actors.FallingMan;
import net.slezok.dots.actors.Platforms;

public class GridScreen implements Screen{
	private static final String TAG = "GridScreen";

	World world;
	Dots game;
	Stage stage;
	Stage staticStage;
	private BridgesGrid bridgesGrid;
	private BridgesGestureHandler inputHandler;

	//for zoom
	private float oldInitialDistance = 0;
	private float initialScale = 0;
	
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	TextButton upButton;
	TextButton downButton;
	TextButton leftButton;
	TextButton rightButton;
	
	public GridScreen(Dots game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		
		Camera camera = stage.getCamera();
		
		Gdx.gl.glClearColor(1f, 0f, 1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		staticStage.act(delta);
        staticStage.draw();
        Table.drawDebug(staticStage);

        stage.act(delta);
        stage.draw();
        
        world.step(1/60f, 6, 2);
//        debugRenderer.render(world, camera.combined);
        
	}

	@Override
	public void resize(int width, int height) {
		if(bridgesGrid != null){
			stage.setViewport(bridgesGrid.getScreenWidth(), bridgesGrid.getScreenHeight(), false);
		}
	}

	
	@Override
	public void show() {
		world = new World(new Vector2(0f, -1), true);
		
		stage = new Stage();
		staticStage = new Stage();	
		staticStage.addListener(new BridgesGestureHandler(this));

		bridgesGrid = new BridgesGrid(world);
		bridgesGrid.setPosition(0, 0);
		
		stage.addActor(bridgesGrid);
		
		Table table = new Table(Assets.skin);
		upButton = new TextButton("up", Assets.skin);
		downButton = new TextButton("down", Assets.skin);
		leftButton = new TextButton("left", Assets.skin);
		rightButton = new TextButton("right", Assets.skin);
		InputListener buttonListener = new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Actor actor = event.getListenerActor();
				if(actor.equals(upButton)){
					Gdx.app.log(TAG, "up pressed");
				}else if(actor.equals(downButton)){
					Gdx.app.log(TAG, "down pressed");
				}else if(actor.equals(leftButton)){
					Gdx.app.log(TAG, "left pressed");
				}else if(actor.equals(rightButton)){
					Gdx.app.log(TAG, "right pressed");
				}
				return true;
			}
			
		};
		upButton.addListener(buttonListener);
		downButton.addListener(buttonListener);
		leftButton.addListener(buttonListener);
		rightButton.addListener(buttonListener);

		table.setFillParent(true);
		table.defaults().width(100).height(80);
		table.debug();
		table.add(upButton).colspan(2).align(BaseTableLayout.CENTER);
		table.row();
		table.add(leftButton).padRight(100);
		table.add(rightButton).padLeft(100);
		table.row();
		table.add(downButton).colspan(2).align(BaseTableLayout.CENTER);
		
		Image bgrImage = new Image(Assets.backgroundTexture);
		bgrImage.setFillParent(true);
		bgrImage.setPosition(0,  0);
		staticStage.addActor(bgrImage);
		staticStage.addActor(table);
		
		Gdx.input.setInputProcessor(staticStage);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	
	
	public void moveRelatively(float x, float y) {
		Camera camera = stage.getCamera();
		
		camera.position.x -= x;
		if(camera.position.x < 0) camera.position.x = 0;
		if(camera.position.x > bridgesGrid.getWorldWidth()) camera.position.x = bridgesGrid.getWorldWidth();

		camera.position.y -= y;
		if(camera.position.y < 0) camera.position.y = 0;
		if(camera.position.y > bridgesGrid.getWorldHeight()) camera.position.y = bridgesGrid.getWorldHeight();
		
		Gdx.app.log(TAG, "New camera position: x=" + camera.position.x + " y=" + camera.position.y);
	}

	public void addBridge(Bridge bridge) {
		bridgesGrid.addBridge(bridge);
	}
	
	public void zoom(float initialDistance, float distance) {
		if(oldInitialDistance != initialDistance){
			initialScale = bridgesGrid.getScaleX();
			oldInitialDistance = initialDistance;
		}else{
			bridgesGrid.setScale(initialScale * distance / initialDistance);
		}
	}

	public void setNormalZoom() {
		bridgesGrid.setScale(1);		
	}	
}
