import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class Bird {

    public int x;
    public int y;
    public int width;
    public int height;

    public boolean dead;

    public double yvel;
    public double gravity;

    private int jumpDelay;
    private double rotation;

    private Image image;

    public static double pipeAheadX;
    public static double pipeAheadNorthY;
    public static double pipeAheadSouthY;

    public NeuralNetwork brain;
    public long score;
    public double fitness;

    public Bird() {
        x = 100;
        y = 150;
        yvel = 0;
        width = 45;
        height = 32;
        gravity = 0.5;
        jumpDelay = 0;
        rotation = 0.0;
        dead = false;
        score = 0;
        fitness = 0;

        brain = new NeuralNetwork(8, 16, 1);
    }

    public Bird(NeuralNetwork brain) {
        x = 100;
        y = 150;
        yvel = 0;
        width = 45;
        height = 32;
        gravity = 0.5;
        jumpDelay = 0;
        rotation = 0.0;
        dead = false;
        score = 0;
        fitness = 0;

        this.brain = brain.copyItself().mutate(0.1);
    }

    public void update() {
        score++;
        yvel += gravity;

        if (jumpDelay > 0)
            jumpDelay--;

        think();

        y += (int) yvel;
    }

    public void think() {
        double birdYCoord = (double) this.y / App.HEIGHT;
        double distanceBetweenBirdAndPipeX = ((double) pipeAheadX - this.x )/ 350;
        double distanceBetweenBirdAndNorthPipeY = ((double) this.y - pipeAheadNorthY )/ App.HEIGHT;
        double distanceBetweenBirdAndSouthPipeY = ((double) pipeAheadSouthY - (double)(this.y + this.height))/ App.HEIGHT;
        double birdVelY = yvel/15;

        double northPipeY = (double) pipeAheadNorthY / App.HEIGHT;
        double southPipeY = (double) pipeAheadSouthY / App.HEIGHT;
        double distanceBetweenBirdAndPipeEndX = ((double) pipeAheadX + 66 - this.x )/ 350;

        double[] inputs = {birdYCoord, distanceBetweenBirdAndPipeX, distanceBetweenBirdAndNorthPipeY, distanceBetweenBirdAndSouthPipeY, birdVelY, northPipeY, southPipeY, distanceBetweenBirdAndPipeEndX};
        double[] outputs = brain.feedforward(inputs);
        if (outputs[0] > 0.5) {
            yvel = -10;
            jumpDelay = 10;
        }
    }

    public Render getRender() {
        if(!this.dead) {
            Render r = new Render();
            r.x = x;
            r.y = y;

            if (image == null) {
                image = Util.loadImage("lib/bird.png");
            }
            r.image = image;

            rotation = (90 * (yvel + 20) / 20) - 90;
            rotation = rotation * Math.PI / 180;

            if (rotation > Math.PI / 2)
                rotation = Math.PI / 2;

            r.transform = new AffineTransform();
            r.transform.translate(x + width / 2, y + height / 2);
            r.transform.rotate(rotation);
            r.transform.translate(-width / 2, -height / 2);

            return r;
        }else{
            return null;
        }
    }
}
