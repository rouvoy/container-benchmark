#!/bin/sh

echo "Compilation starting..."

export ALLOCATION_JAR=`find ~/.m2/repository -name java-allocation-instrumenter-2.0.jar`

cd parent
mvn clean install
echo "Compilation completed"
cd ../java/benchmark
echo "Benchmark starting..."
mvn -o exec:exec
cd ../../fractal/benchmark
mvn -o exec:exec
cd ../../sca/benchmark
mvn -o exec:exec
cd ../..

echo "Benchmark completed"