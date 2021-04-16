import pandas as pd
import numpy as np

from keras.models import Sequential
from keras.layers import Dense
from keras.utils import to_categorical

from sklearn.model_selection import train_test_split
from sklearn.utils import shuffle
from sklearn.svm import SVC
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import GridSearchCV
from sklearn.metrics import accuracy_score
from sklearn.base import BaseEstimator

def load_data(data, prediction):
  columns = ['home', 'away', 'home_score', 'away_score']
  df = pd.DataFrame([list(map(int, x.split(','))) for x in data.split('\n')], columns=columns)
  pred_df = pd.DataFrame([list(map(int, x.split(','))) for x in prediction.split('\n')], columns=columns[:2])

  # 0 - home win
  # 1 - draw
  # 2 - away win
  df[['score']] = df['home_score'] - df['away_score']
  df['score'] = df['score'].apply(lambda x: 0 if x > 0 else 2 if x < 0 else 1)
  df.drop(columns=['home_score', 'away_score'], inplace=True)
  df = shuffle(df)

  return df, pred_df

def split_data_in_train_and_test_sets(df, train_rate):
  batch = int(train_rate*df.shape[0])

  X_train = np.asarray(df.drop(['score'], axis=1)[:batch])
  y_train = df['score'].values[:batch]

  X_test = np.asarray(df.drop(['score'], axis=1)[batch:])
  y_test = df['score'].values[batch:]

  return X_train, y_train, X_test, y_test

def create_model():
  model = Sequential()

  model.add(Dense(20, input_dim=2, activation='sigmoid'))
  model.add(Dense(3, activation='softmax'))

  model.compile(loss='binary_crossentropy', optimizer='sgd', metrics=['accuracy'])

  return model

def predict_with_neural_network(X_train, y_train,
                                X_test, y_test,
                                pred_df):
  en_y_train = to_categorical(y_train)
  en_y_test = to_categorical(y_test)

  network = create_model()
  network.fit(X_train, en_y_train, epochs=30, verbose=0)
  _, accuracy = network.evaluate(X_test, en_y_test, verbose=0)
  print(f"Accuracy: {accuracy}")

  return np.argmax(network.predict(pred_df), axis=-1)

def predict_with_classifier(classifier, params,
                            X_train, y_train,
                            X_test, y_test,
                            pred_df):

  gcv = GridSearchCV(classifier, params, 'accuracy')

  gcv.fit(X_train, y_train)
  y_pred = gcv.predict(X_test)
  print(f"Accuracy: {accuracy_score(y_test, y_pred)}")

  return gcv.predict(pred_df)

def predictions_as_string(nn_results, svm_results, rfc_results):
  string = ""
  results_count = len(nn_results)

  for i in range(results_count):
    string += str(nn_results[i]) + ',' + str(svm_results[i]) + ',' + str(rfc_results[i]) + '\n'

  return string

def make_predictions(data, prediction):
  # preprocessing
  df, pred_df = load_data(data, prediction)

  train_rate = 0.8
  X_train, y_train, X_test, y_test = split_data_in_train_and_test_sets(df, train_rate)

  # neural network
  nn_predictions = predict_with_neural_network(X_train, y_train, X_test, y_test, pred_df)

  # SVM
  svm_params = {
    'C': [0.5, 1, 2, 2.5, 5],
    'gamma': ['auto', 'scale', 0.5, 1, 2]
  }

  svm_predictions = predict_with_classifier(SVC(), svm_params, X_train, y_train, X_test, y_test, pred_df)

  # Random Forest
  rfc_params = {
    'n_estimators': [5, 10, 50, 100],
    'max_depth': [None, 5, 10, 20]
  }

  rfc_predictions = predict_with_classifier(RandomForestClassifier(), rfc_params, X_train, y_train, X_test, y_test, pred_df)

  return predictions_as_string(nn_predictions, svm_predictions, rfc_predictions)