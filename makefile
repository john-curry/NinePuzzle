run = java -Xss1024000m -ea NinePuzzle 10_solvable_boards.txt && java -ea NinePuzzle 10_unsolvable_boards.txt
all: NinePuzzle.java
	javac -g NinePuzzle.java && ctags . && $(run)

solverun:
	java -ea NinePuzzle 10_solvable_boards.txt

unsolverun:
	java -ea NinePuzzle 10_unsolvable_boards.txt

run:
	$(run)

build:
	javac -g NinePuzzle.java

test:
	javac -g NinePuzzle.java&&\
	java -ea NinePuzzle solved.txt
