package net.slezok.dots.screens;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.esotericsoftware.tablelayout.BaseTableLayout;

import net.slezok.dots.Assets;
import net.slezok.dots.Bridge;
import net.slezok.dots.DictGestureHandler;
import net.slezok.dots.Dots;
import net.slezok.dots.Level;
import net.slezok.dots.actors.DictField;

public class PropertiesScreen implements Screen, InputProcessor{
	private static final String TAG = "PropertiesScreen";

	private static final long CURRENT_SOUND_DELAY = 800;

	private World world;

	private final Dots game;

	private Stage staticStage;

	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	private Table table;

	public PropertiesScreen(Dots game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1f, 0f, 1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		staticStage.act(delta);
		staticStage.draw();
		//        Table.drawDebug(staticStage);

		world.step(1/60f, 6, 2);
		//        debugRenderer.render(world, camera.combined);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		world = new World(new Vector2(0f, -1), true);

		Table table = new Table(Assets.skin);
		table.setFillParent(true);
		TextField tf = new TextField("���", Assets.skin);
		table.add(tf).width(300).height(120);

		staticStage = new Stage();	

		Image bgrImage = new Image(Assets.dictBackgroundTexture);
		bgrImage.setFillParent(true);
		bgrImage.setPosition(0,  0);
		staticStage.addActor(bgrImage);
		staticStage.addActor(table);
		
//		InputMultiplexer multiplexer = new InputMultiplexer();
//		multiplexer.addProcessor(this);
//		multiplexer.addProcessor(staticStage);

//		Gdx.input.setOnscreenKeyboardVisible(true);
//		Gdx.input.setInputProcessor(this);

		MyTextInputListener listener = new MyTextInputListener();
		Gdx.input.getTextInput(listener, "Dialog Title", "Initial Textfield Value");
	}

	public class MyTextInputListener implements TextInputListener {
		   @Override
		   public void input (String text) {
		   }

		   @Override
		   public void canceled () {
		   }
		}
	
	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		staticStage.dispose();
		Assets.wellDoneSound.dispose();
		Assets.gameOverSound.dispose();
		Assets.errorSound.dispose();
	}

	public void addBridge(Bridge bridge) {
	}

	public void setNormalZoom() {
	}

	@Override
	public boolean keyDown(int keycode) {
		Gdx.app.log(TAG, "keydown : " + keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}	
}