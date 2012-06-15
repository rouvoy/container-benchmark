#!/bin/sh

echo "Compilation starting..."

export ALLOCATION_JAR=`find ~/.m2/repository -name java-allocation-instrumenter-2.0.jar`

cd parent
mvn clean install
echo "Compilation completed"
echo "Benchmark starting..."
cd ../java/benchmark-java
mvn -o exec:exec
cd ../java/benchmark-guice
mvn -o exec:exec
cd ../../fractal/benchmark
mvn -o exec:exec
cd ../../sca/benchmark-frascati
mvn -o exec:exec
cd ../benchmark-tuscany
mvn -o exec:exec
cd ../..

echo "Benchmark completed"
