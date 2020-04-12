
public class Matrix {
    double[][] data;

    public Matrix(int rows, int cols){
        data = new double[rows][cols];
    }

    public void randomizeWeights(){
        for(int i=0; i<data.length ;i++){
            for(int j=0; j<data[i].length ;j++){
                data[i][j] = (double) (Math.random()*2-1);
            }
        }
    }

    public void randomizeBiases(){
        for(int i=0; i<data.length ;i++){
            for(int j=0; j<data[i].length ;j++){
                data[i][j] = (double) 1;
            }
        }
    }

    public static Matrix fromArray(double[] arr){
        Matrix newMatrix = new Matrix(arr.length, 1);
        for(int i=0; i<arr.length ;i++){
            newMatrix.data[i][0] = arr[i];
        }
        return newMatrix;
    }

    public double[] toArray(){
        double[] array = new double[data.length * data[0].length];
        int index = 0;
        for(int i=0; i<data.length ;i++){
            for(int j=0; j<data[i].length ;j++){
                array[index] = data[i][j];
                index++;
            }
        }
        return array;
    }

    public static Matrix map(Matrix a, Sigmoid sigmoid){
        Matrix newMatrix = new Matrix(a.data.length, a.data[0].length);
        for(int i=0; i<a.data.length ;i++){
            for(int j=0; j<a.data[i].length ;j++){
                double value = a.data[i][j];
                newMatrix.data[i][j] = sigmoid.calculateSigmoid(value);
            }
        }
        return newMatrix;
    }

    public static Matrix map(Matrix a, DivSigmoid divSigmoid){
        Matrix newMatrix = new Matrix(a.data.length, a.data[0].length);
        for(int i=0; i<a.data.length ;i++){
            for(int j=0; j<a.data[i].length ;j++){
                double value = a.data[i][j];
                newMatrix.data[i][j] = divSigmoid.calculateDivSigmoid(value);
            }
        }
        return newMatrix;
    }

    public void multiply(double x){
        for(int i=0; i<data.length ;i++){
            for(int j=0; j<data[i].length ;j++){
                data[i][j] *= x;
            }
        }
    }

    public static Matrix multiply(Matrix a, Matrix b){
        if(a.data[0].length != b.data.length) {
            System.out.println("Columns of A must match rows of B");
            return null;
        }
        else{
            Matrix newMatrix = new Matrix(a.data.length, b.data[0].length);

            for(int i=0; i<newMatrix.data.length ;i++){
                for(int j=0; j<newMatrix.data[i].length ;j++){
                    double sum = 0;
                    for(int k=0; k<a.data[0].length ;k++){
                        sum += a.data[i][k] * b.data[k][j];
                    }
                    newMatrix.data[i][j] = sum;
                }
            }
            return newMatrix;
        }
    }

    public void hadamarMultiplication(Matrix a){
        for(int i=0; i<data.length ;i++){
            for(int j=0; j<data[i].length ;j++){
                data[i][j] *= a.data[i][j];
            }
        }
    }

    public void add(double x){
        for(int i=0; i<data.length ;i++){
            for(int j=0; j<data[i].length ;j++){
                data[i][j] += x;
            }
        }
    }

    public void add(Matrix matr){
        for(int i=0; i<data.length ;i++){
            for(int j=0; j<data[i].length ;j++){
                data[i][j] += matr.data[i][j];
            }
        }
    }

    public static Matrix subtract(Matrix a, Matrix b){
        Matrix newMatrix = new Matrix(a.data.length, a.data[0].length);
        for(int i=0; i<newMatrix.data.length ;i++){
            for(int j=0; j<newMatrix.data[i].length ;j++){
                newMatrix.data[i][j] = a.data[i][j] - b.data[i][j];
            }
        }
        return newMatrix;
    }

    public static Matrix transpose(Matrix a){
        Matrix newMatrix = new Matrix(a.data[0].length, a.data.length);

        for(int i=0; i<a.data.length ;i++){
            for(int j=0; j<a.data[i].length ;j++){
                newMatrix.data[j][i] = a.data[i][j];
            }
        }
        return newMatrix;
    }

    public void show(){
        for(int i=0; i<data.length ;i++){
            for(int j=0; j<data[i].length ;j++){
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
    }
}
