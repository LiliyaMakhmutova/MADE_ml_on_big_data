package linear_regression

import breeze.linalg.{DenseMatrix, DenseVector, csvread, csvwrite, inv, min, sum}
import breeze.numerics.sqrt

import java.io.File

/** Singleton for linear regression solver
 * Preprocessed (without categorical variables) 80 cereals dataset
 * is used (https://www.kaggle.com/crawford/80-cereals) and predicts ratings
 * */
object Main {
  /** Main linear regression solver
   * @param args contains array of strings,
   *             where first element is a path to train dataset
   *             and second element is a path to test dataset
   *  */
  def main(args: Array[String]): Unit = {
    val origTrainDataset = csvread(new File(args(0)),',',skipLines=1)
    val numRows = origTrainDataset.rows
    val numCols = origTrainDataset.cols

    // train_test_split
    val lastTrainRow = (0.85 * numRows).toInt
    val train = origTrainDataset(0 to lastTrainRow, ::)
    val validation = origTrainDataset(lastTrainRow+1 to numRows-1, ::)

    val xTrain = train(::, 0 to (numCols - 2))
    val yTrain = train(::, (numCols - 1))

    val xValidation = validation(::, 0 to (numCols - 2))
    val yValidation = validation(::, (numCols - 1))

    val linearRegression : LinearRegression = new LinearRegression(xTrain, yTrain)
    val yPredicted: DenseVector[Double] = linearRegression.predict(xValidation)
    print(f"RMSE on validation data: ${evaluate(yValidation, yPredicted)}\n")

    val testDataset = csvread(new File(args(1)),',',skipLines=1)
    val xTest = testDataset(::, 0 to (numCols - 2))
    val yTest = testDataset(::, (numCols - 1))
    val yPredictedForTest: DenseVector[Double] = linearRegression.predict(xTest)
    csvwrite(new File("predicted.csv"), yPredictedForTest.toDenseMatrix.t, separator = ',')
    print(f"RMSE on test data: ${evaluate(yTest, yPredictedForTest)}")
  }

  /** Provides classes for linear regression model
   *
   *  ==Overview==
   *  @param X contains dataset to predict
   *  @param y contains target function
   */
  class LinearRegression(X: DenseMatrix[Double], y: DenseVector[Double]) {
    var coeffs = inv(this.X.t * this.X) * this.X.t * this.y
    /** Predicts values
     *
     *  @param X contains dataset to predict
     */
    def predict(X: DenseMatrix[Double]) : DenseVector[Double] = {
      return X * this.coeffs
    }
  }

  /* Computes RMSE on predicted and true target function */
  def evaluate(Ytrue: DenseVector[Double], Ypredicted: DenseVector[Double]): Double = {
    return sqrt(sum((Ytrue - Ypredicted)*(Ytrue - Ypredicted)) / Ytrue.length)
  }
}
