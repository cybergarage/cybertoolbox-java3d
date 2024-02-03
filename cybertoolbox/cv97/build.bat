del *.class
del field\*.class
del j3d\*.class
del node\*.class
del image\*.class
del parser\vrm97\*.class
del parser\wavefront\*.class
del parser\autodesk\*.class
del parser\sense8\*.class
del parser\threedsystems\*.class
del route\*.class
del util\*.class
javac -O *.java
javac -O field\*.java
javac -O j3d\*.java
javac -O node\*.java
javac -O image\*.java
javac -O parser\vrm97\*.java
javac -O parser\wavefront\*.java
javac -O parser\autodesk\*.java
javac -O parser\sense8\*.java
javac -O parser\threedsystems\*.java
javac -O route\*.java
javac -O util\*.java
