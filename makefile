JC = javac
JFLAGS = -cp ij.jar:.
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Dilate_.java \
	Erode_.java \
	Hit_Or_Miss.java \
	Morph_Close.java \
	Morph_Open.java \
	Morphological_Gradient.java \
	New_Structuring_Element.java \
	Otsu_Threshold.java \
	Top_Hat.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
	
