run = java -ea NinePuzzle 10_solvable_boards.txt && java -ea NinePuzzle 10_unsolvable_boards.txt
all: NinePuzzle.java
	javac -g NinePuzzle.java && $(run)

solverun:
	java -ea NinePuzzle 10_solvable_boards.txt

unsolverun:
	java -ea NinePuzzle 10_unsolvable_boards.txt

run:
	$(run)

build:
	javac -g NinePuzzle.java

test:
	java -ea NinePuzzle solved.txt
