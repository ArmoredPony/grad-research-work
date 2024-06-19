FILE := App

run: build
	java -cp 3rd/*;bin $(FILE)

build:
	javac -cp 3rd/* -sourcepath src -d bin src/$(FILE).java

jar: build
	jar cfe Rubricator.jar App -C ./bin .