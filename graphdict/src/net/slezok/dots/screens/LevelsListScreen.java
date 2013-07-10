package net.slezok.dots.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.swarmconnect.Swarm;
import com.swarmconnect.SwarmLeaderboard;

import net.slezok.dots.Assets;
import net.slezok.dots.Constants;
import net.slezok.dots.Dots;
import net.slezok.dots.Level;

public class LevelsListScreen implements Screen {
	private Dots game;
	private Stage stage;
	private List levelsList;
	private ImageButton playButton;
	private ImageButton recordsButton;
	private Level level = null;

	public LevelsListScreen(Dots game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
//		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		FileHandle file =  Gdx.files.internal("data/levels.json");
		Json json = new Json();
		@SuppressWarnings("unchecked")
		final
		Array<Level> levels = json.fromJson(Array.class, Level.class, file);

		String[] levelNames = new String[levels.size];
		for(int i = 0; i < levels.size; i++){
			levelNames[i] = levels.get(i).getDescription();
		}

		levelsList = new List(levelNames, Assets.skin);
		levelsList.setSelectedIndex(0);
		level = levels.get(0);
		levelsList.addListener(new EventListener(){
			@Override
			public boolean handle(Event event) {
				List list = (List)event.getListenerActor();
				level = levels.get(list.getSelectedIndex());
				return true;
			}
		});
		ScrollPane scroller = new ScrollPane(levelsList);
		scroller.setScale(1.5f);//FIXME

		Image backImage = new Image(Assets.listBackgroundTexture);
		backImage.setFillParent(true);
		
		playButton = new ImageButton(new TextureRegionDrawable(Assets.play), new TextureRegionDrawable(Assets.playPressed));
		playButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(level != null){
					game.setScreen(new DictScreen(game, level));
				}
				return true;
			}
		});

		recordsButton = new ImageButton(new TextureRegionDrawable(Assets.records), new TextureRegionDrawable(Assets.recordsPressed));
		recordsButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//				game.setScreen(new PropertiesScreen(game));
				Swarm.showLeaderboards();
				return true;
			}
		});

		Table table = new Table(Assets.skin);
		table.setFillParent(true);
//		table.debug(); 
		table.add(scroller).width(200).height(160);
		table.row();
		table.add(playButton).width(250).height(80);
		table.add(recordsButton).width(250).height(80);
		
		stage.addActor(backImage);
		stage.addActor(table);
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
		stage.dispose();
	}
}
