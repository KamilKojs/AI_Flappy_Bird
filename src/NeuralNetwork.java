import java.util.Random;

public class NeuralNetwork {
    int inputNodes;
    int hiddenNodes;
    int outputNodes;
    Matrix weightsIH;
    Matrix weightsHO;
    Matrix biasH;
    Matrix biasO;
    double learningRate;

    public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes){
        this.inputNodes = inputNodes;
        this.hiddenNodes = hiddenNodes;
        this.outputNodes = outputNodes;

        weightsIH = new Matrix(this.hiddenNodes, this.inputNodes);
        weightsHO = new Matrix(this.outputNodes, this.hiddenNodes);
        weightsIH.randomizeWeights();
        weightsHO.randomizeWeights();

        biasH = new Matrix(hiddenNodes, 1);
        biasO = new Matrix(outputNodes, 1);
        biasH.randomizeBiases();
        biasO.randomizeBiases();

        learningRate = 0.01;
    }

    public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes, Matrix weightsIH, Matrix weightsHO, Matrix biasH, Matrix biasO, double learningRate){
        this.inputNodes = inputNodes;
        this.hiddenNodes = hiddenNodes;
        this.outputNodes = outputNodes;

        this.weightsIH = weightsIH;
        this.weightsHO = weightsHO;

        this.biasH = biasH;
        this.biasO = biasO;

        this.learningRate = learningRate;
    }

    public double[] feedforward(double[] inputArray){
        //Sigmoid function
        Sigmoid sigmoid = x -> 1 / (1 + Math.exp(-x));

        //Generating the hidden outputs
        Matrix inputMatrix = Matrix.fromArray(inputArray);
        Matrix hidden = Matrix.multiply(weightsIH, inputMatrix);
        hidden.add(biasH);
        //Activation function
        Matrix hiddenActivated = Matrix.map(hidden, sigmoid);

        //Generating the output outputs
        Matrix output = Matrix.multiply(weightsHO, hiddenActivated);
        output.add(biasO);
        //Activation function
        Matrix outputActivated = Matrix.map(output, sigmoid);

        return outputActivated.toArray();
    }

    public void train(double[] inputArray, double[] targetArray){
        //feedForward ##################################################################################################

        //Sigmoid function
        Sigmoid sigmoid = x -> 1 / (1 + Math.exp(-x));
        //DivSigmoid function
        DivSigmoid divSigmoid = x -> x * (1 - x);

        //Generating the hidden outputs
        Matrix inputMatrix = Matrix.fromArray(inputArray);
        Matrix hidden = Matrix.multiply(weightsIH, inputMatrix);
        hidden.add(biasH);
        //Activation function
        Matrix hiddenActivated = Matrix.map(hidden, sigmoid);

        //Generating the output outputs
        Matrix output = Matrix.multiply(weightsHO, hiddenActivated);
        output.add(biasO);
        //Activation function
        Matrix outputActivated = Matrix.map(output, sigmoid);

        //Adjusting weights by their deltas (Backpropagation) ##########################################################

        //Convert targetArray to matrix object
        Matrix target = Matrix.fromArray(targetArray);



        //Output weights deltas &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

        //Calculate output error
        //ERROR = TARGET - OUTPUT
        Matrix outputError = Matrix.subtract(target, outputActivated);

        //Calculate output gradient
        Matrix outputGradient = Matrix.map(outputActivated, divSigmoid);
        outputGradient.hadamarMultiplication(outputError);
        outputGradient.multiply(learningRate);

        //Calculate hidden -> output deltas
        Matrix hidden_T = Matrix.transpose(hidden);
        Matrix weightsHO_deltas = Matrix.multiply(outputGradient, hidden_T);
        //adjust weights by weightDeltas
        weightsHO.add(weightsHO_deltas);
        //Adjust bias by its deltas (which is just the gradients)
        biasO.add(outputGradient);



        //Hidden weights deltas &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

        //Calculate hidden error
        Matrix weightsHO_T = Matrix.transpose(weightsHO);
        Matrix hiddenError = Matrix.multiply(weightsHO_T, outputError);

        //Calculate hidden gradient
        Matrix hiddenGradient = Matrix.map(hiddenActivated, divSigmoid);
        hiddenGradient.hadamarMultiplication(hiddenError);
        hiddenGradient.multiply(learningRate);

        //Calculate input -> hidden deltas
        Matrix input_T = Matrix.transpose(inputMatrix);
        Matrix weightsIH_deltas = Matrix.multiply(hiddenGradient, input_T);
        //adjust weights by weightDeltas
        weightsIH.add(weightsIH_deltas);
        //Adjust bias by its deltas (which is just the gradients)
        biasH.add(hiddenGradient);
    }

    public NeuralNetwork copyItself(){
        return new NeuralNetwork(this.inputNodes, this.hiddenNodes, this.outputNodes, this.weightsIH, this.weightsHO, this.biasH, this.biasO, this.learningRate);
    }

    public NeuralNetwork mutate(double mutationRate){
        Random random = new Random();
        Mutate mutation = x -> x += (Math.random()*2-1)*0.1;

        this.weightsIH = Matrix.map(weightsIH, mutation, mutationRate);
        this.weightsHO = Matrix.map(weightsHO, mutation, mutationRate);

        this.biasH = Matrix.map(biasH, mutation, mutationRate);
        this.biasO = Matrix.map(biasO, mutation, mutationRate);

        return this;
    }
}
