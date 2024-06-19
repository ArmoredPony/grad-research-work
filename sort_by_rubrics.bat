javac -cp 3rd/* -sourcepath src -d bin src/NewsSeparator.java
java -cp 3rd/*;bin NewsSeparator data/csv/lenta-ru-news-tagged.csv