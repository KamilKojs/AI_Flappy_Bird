import java.util.ArrayList;

public class GeneticAlgorithm {

    public void nextGeneration(){
        boolean newBestScore = checkForNewBestScore();
        if(newBestScore) {
            calculateFitness();
            Bird[] newBirdsArray = getNewBirdsArray();
            createNewBirds(newBirdsArray);
        }else{
            //calculateFitness();
            calculateBestPopulationFitness();
            //Bird[] newBirdsArray = getNewBirdsArray();
            Bird[] newBirdsArray = getNewBirdsArrayFromAllTimeBest();
            //Bird[] newBirdsArray = getNewBirdsArrayFromAllTimeBestBird();
            createNewBirds(newBirdsArray);
        }
    }

    private void calculateFitness() {
        int sum = 0;
        for(Bird bird : Game.birds){
            sum += bird.score;
        }

        for(Bird bird : Game.birds){
            bird.fitness = (double) bird.score / sum;
        }
    }

    private void calculateBestPopulationFitness() {
        int sum = 0;
        for(Bird bird : Game.bestPopulationAllTime){
            sum += bird.score;
        }

        for(Bird bird : Game.bestPopulationAllTime){
            bird.fitness = (double) bird.score / sum;
        }
    }

    private Bird[] getNewBirdsArray(){
        Bird[] birdsArray = new Bird[Game.birds.size()];
        for(int i=0; i<birdsArray.length ;i++){
            birdsArray[i] = pickNewBird();
        }
        return birdsArray;
    }

    private Bird[] getNewBirdsArrayFromAllTimeBest(){
        Bird[] birdsArray = new Bird[Game.bestPopulationAllTime.size()];
        for(int i=0; i<birdsArray.length ;i++){
            birdsArray[i] = pickNewBirdFromBestAllTime();
        }
        return birdsArray;
    }

    private Bird[] getNewBirdsArrayFromAllTimeBestBird(){
        Bird[] birdsArray = new Bird[Game.bestPopulationAllTime.size()];
        for(int i=0; i<birdsArray.length ;i++){
            birdsArray[i] = new Bird(Game.bestBirdAllTime.brain);
        }
        return birdsArray;
    }

    private void createNewBirds(Bird[] newBirdsArray){
        Game.birds = new ArrayList<>();
        Game.bestPopulationAllTime = new ArrayList<>();
        for(int i=0; i<newBirdsArray.length ;i++){
            Game.birds.add(newBirdsArray[i]);
            Game.bestPopulationAllTime.add(newBirdsArray[i]);
        }
    }

    private Bird pickNewBird(){
        Bird[] arr = Game.birds.toArray(new Bird[Game.birds.size()]);
        int index = 0;
        double r = Math.random();

        while(r > 0){
            r = r - arr[index].fitness;
            index++;
        }
        index--;

        Bird bird = arr[index];
        Bird child  = new Bird(bird.brain);
        return child;
    }

    private Bird pickNewBirdFromBestAllTime(){
        Bird[] arr = Game.bestPopulationAllTime.toArray(new Bird[Game.bestPopulationAllTime.size()]);
        int index = 0;
        double r = Math.random();

        while(r > 0){
            r = r - arr[index].fitness;
            index++;
        }
        index--;

        Bird bird = arr[index];
        Bird child  = new Bird(bird.brain);
        return child;
    }

    private boolean checkForNewBestScore(){
        boolean newBestScore = false;
        for(Bird bird : Game.birds){
            if(bird.score > Game.bestScoreAllTime) {
                Game.bestScoreAllTime = bird.score;
                Game.bestBirdAllTime = bird;
                newBestScore = true;
            }
        }
        return newBestScore;
    }
}
