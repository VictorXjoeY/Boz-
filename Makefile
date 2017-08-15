# Victor Forbes - 9293394

ifdef file
	runfile = $(file)
	javafile = $(file).java
	zipfile = $(file).zip
else
	runfile = Main
	javafile = *.java
	zipfile = 9293394.zip
endif

all:
	@javac -sourcepath src/ -classpath bin/ -d bin/ src/$(javafile)
debug:
	@javac -sourcepath src/ -classpath bin/ -d bin/ src/$(javafile) -g
run:
	@java -classpath bin/ $(runfile)
doc:
	@javadoc -d javadoc/ src/$(javafile)
zip:
	@zip -r $(zipfile) *
