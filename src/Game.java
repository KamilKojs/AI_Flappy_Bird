import org.omg.SendingContext.RunTime;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {

    public static final int PIPE_DELAY = 150;
    public static final int BIRDS_AMMOUNT = 400;

    private int pipeDelay;

    public static ArrayList<Bird> bestPopulationAllTime;
    public static Bird bestBirdAllTime;
    public static long bestScoreAllTime;
    public static ArrayList<Bird> birds;
    private ArrayList<Pipe> pipes;

    public int score;
    public Boolean gameover;
    public Boolean started;

    GeneticAlgorithm geneticAlgorithm;
    int generationCounter;

    public Game() {
        birds = new ArrayList<>();
        bestPopulationAllTime = new ArrayList<>();
        bestBirdAllTime = new Bird();
        bestScoreAllTime = 0;
        generationCounter = 1;
        for(int i=0; i<BIRDS_AMMOUNT ;i++){
            birds.add(new Bird());
        }
        bestPopulationAllTime = birds;
        geneticAlgorithm = new GeneticAlgorithm();
        restart();
    }

    public void restart() {
        started = false;
        gameover = false;

        score = 0;
        pipeDelay = 0;

        Bird.pipeAheadX = 500;
        Bird.pipeAheadNorthY = 200;
        Bird.pipeAheadSouthY = 350;

        pipes = new ArrayList<Pipe>();
    }

    public void update() {
        for(Bird bird : birds) {
            if(!bird.dead) {
                bird.update();
            }
        }

        if (gameover){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            generationCounter++;
            restart();
            geneticAlgorithm.nextGeneration();
            return;
        }

        movePipes();
        checkForCollisions();
        checkForAnyAlive();
    }

    public ArrayList<Render> getRenders() {
        ArrayList<Render> renders = new ArrayList<Render>();
        renders.add(new Render(0, 0, "lib/background.png"));
        for (Pipe pipe : pipes)
            renders.add(pipe.getRender());
        renders.add(new Render(0, 0, "lib/foreground.png"));
        for(Bird bird : birds) {
            if(!bird.dead)
                renders.add(bird.getRender());
        }
        return renders;
    }

    private void movePipes() {
        pipeDelay--;

        if (pipeDelay < 0) {
            pipeDelay = PIPE_DELAY;
            Pipe northPipe = null;
            Pipe southPipe = null;

            // Look for pipes off the screen
            for (Pipe pipe : pipes) {
                if (pipe.x + pipe.width < 0) {
                    if (northPipe == null) {
                        northPipe = pipe;
                    } else if (southPipe == null) {
                        southPipe = pipe;
                        break;
                    }
                }
            }

            if (northPipe == null) {
                Pipe pipe = new Pipe("north");
                pipes.add(pipe);
                northPipe = pipe;
            } else {
                northPipe.reset();
            }

            if (southPipe == null) {
                Pipe pipe = new Pipe("south");
                pipes.add(pipe);
                southPipe = pipe;
            } else {
                southPipe.reset();
            }

            northPipe.y = southPipe.y + southPipe.height + 175;
        }

        Pipe closest = null;
        int closestDistance = 600;
        for (Pipe pipe : pipes) {
            pipe.update();
            int d = pipe.x + pipe.width - birds.get(0).x;
            if(d < closestDistance && d > 0){
                closest = pipe;
            }
        }
        if(closest.orientation.equals("north")){
            Bird.pipeAheadX = closest.x;
            Bird.pipeAheadSouthY = closest.y;
            Bird.pipeAheadNorthY = closest.y - 175;
        }else if(closest.orientation.equals("south")){
            Bird.pipeAheadX = closest.x;
            Bird.pipeAheadNorthY = closest.y + 400;
            Bird.pipeAheadSouthY = closest.y + 400 + 175;
        }
    }

    private void checkForCollisions() {
        boolean scoreAdded = false;
        for (Pipe pipe : pipes) {
            for(Bird bird : birds) {
                if (pipe.collides(bird.x, bird.y, bird.width, bird.height)) {
                    bird.dead = true;
                } else if (pipe.x == bird.x && pipe.orientation.equalsIgnoreCase("south")) {
                    if(!scoreAdded) {
                        score++;
                        scoreAdded = true;
                    }
                }
            }
        }

        // Ground + Bird collision
        for(Bird bird : birds) {
            if (bird.y + bird.height > App.HEIGHT - 80) {
                bird.dead = true;
                bird.y = App.HEIGHT - 80 - bird.height;
            }
        }

        //Sky + Bird collision
        for(Bird bird : birds) {
            if (bird.y + bird.height < 0) {
                bird.dead = true;
                bird.y = App.HEIGHT - 80 - bird.height;
            }
        }
    }

    private void checkForAnyAlive(){
        boolean anyAlive = false;
        for(Bird bird : birds){
            if(!bird.dead) anyAlive = true;
        }
        if(!anyAlive) gameover = true;
    }
}
