package com.shepapps.memdots.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shepapps.memdots.MemDots;
import java.lang.String;
import java.util.Random;


/**
 * Created by scott_000 on 8/25/2016.
 */
public class GameScreen implements Screen {

    private Stage stage;
    private Viewport viewport;
    private Camera cam;
    private ShapeRenderer sr;

    private Image[] dots = new Image[30];
    private Label[] labels = new Label[30];

    private int level = 1;
    private int seeking = 0;
    private boolean lost = false;

    private float timeLeft = 5;
    private static final int totalTime = 5;


    public GameScreen(){

        //initialize stage/viewport
        cam = new OrthographicCamera(MemDots.V_WIDTH, MemDots.V_HEIGHT);
        viewport = new FitViewport(MemDots.V_WIDTH, MemDots.V_HEIGHT, cam);
        stage = new Stage(viewport, MemDots.batch);
        sr = new ShapeRenderer();

        //set stage to input processor
        Gdx.input.setInputProcessor(stage);

        //create font for dots
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        for(int i = 0; i < 30; i++){

            //initiate dots for each iteration
            dots[i] = new Image(new Texture("circle.png"));
            dots[i].setSize(50, 50);
            dots[i].setPosition(-50, -50);

            //initiate labels for each iteration
            labels[i] = new Label(String.valueOf(i + 1), font);
            labels[i].setSize(50,50);
            labels[i].setAlignment(Align.center);
            labels[i].setPosition(-50, -50);

            //add image and label to stage
            stage.addActor(dots[i]);
            stage.addActor(labels[i]);
        }

        //set click listeners on all dots
        DotClickListener();
        //start level 1
        newLevel();
    }

    private Vector2 genRandomPos(int position){

        //set up variables for random
        int xPos, yPos;
        Random rand = new Random();

        //rectangles to test overlap
        Rectangle rect = new Rectangle();
        Rectangle rect2 = new Rectangle();

        //variable to test while
        boolean overlapping = true;

        do{
            //generate random position
            xPos = (int)(rand.nextFloat() * 240) + 5;
            yPos = (int)(rand.nextFloat() * 440) + 5;
            rect.set(xPos, yPos, 50, 50);

            if(position > 0){
                //loop to find if dot overlaps with other dots
                for(int i = 0; i < position; i++){

                    rect2.set(dots[i].getX(), dots[i].getY(), 50, 50);
                    overlapping = rect.overlaps(rect2);

                    if(overlapping){ break; }
                }

            }else{
                overlapping = false;
            }

        } while (overlapping);

        return new Vector2(xPos, yPos);
    }

    private void resetDots(){
        for(int i = 0; i < 30; i ++){
            //move dots and labels off screen
            dots[i].setPosition(-50, -50);
            labels[i].setPosition(-50, -50);
        }
    }

    private void newLevel(){

        seeking = 0;

        //loop through each dot in level
        for(int i = 0; i < level; i++){

            //generate random position
            Vector2 position = genRandomPos(i);

            //set position of each dot/label
            dots[i].setPosition(position.x, position.y);
            labels[i].setPosition(dots[i].getX(), dots[i].getY());

            //reset time
            timeLeft = totalTime;
        }
    }

    private void DotClickListener(){

        for(int i = 0; i < 30; i++){
            final int dot = i;

            dots[i].addListener(new InputListener(){

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                    Gdx.app.log("TAG", "Click: " + String.valueOf(dot));

                    //check if click was correct
                    if(seeking == dot){
                        //move dot off screen
                        dots[dot].setPosition(-50, -50);
                        seeking++;

                        if(dot == level - 1){
                            level++;
                            newLevel();
                        }

                    }else{
                        lost = true;
                    }
                    return false;
                }
            });
        }
    }

    private void DrawRemainingTime(){
        //set up renderer
        Gdx.gl.glLineWidth(50);
        sr.setProjectionMatrix(cam.combined);

        sr.setColor(Color.CYAN);
        sr.begin(ShapeRenderer.ShapeType.Line);
        //draw hp line
        int endX = (int)((timeLeft / totalTime) * MemDots.V_WIDTH);
        sr.line(new Vector2(0, 490), new Vector2(endX, 490));
        sr.end();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //clear screen to white
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //draw stage on top
        stage.draw();

        //line for remaining time and decrease of timer
        timeLeft -= delta;

        if(timeLeft < 0){
            //set all labels off screen
            for(int i = 0; i < 30; i++){

                labels[i].setPosition(-50, -50);
            }
        }else{

            //draw countdown line for time
            DrawRemainingTime();

        }

        if(lost){
            level = 1;
            resetDots();
            newLevel();
            lost = false;
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
