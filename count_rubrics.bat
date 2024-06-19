javac -cp 3rd/* -sourcepath src -d bin src/RubricCounter.java
java -cp 3rd/*;bin RubricCounter data/csv/lenta-ru-news-tagged.csv