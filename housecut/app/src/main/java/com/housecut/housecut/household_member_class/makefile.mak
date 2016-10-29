#Java Makefile for household_member_class.java
#Made by: Adam Faulkner

JFLAGS = -g
JC = javac

sourcefiles = \
household_member_class.java \
household_class.java 

classfiles = $(sourcefiles:.java=.class)
#classfiles = household_member_class.class household_class.class

all: $(classfiles)
%.class: %.java
	$(JC) $(JFLAGS) . -classpath . $<

clean:
	$(RM) *.class