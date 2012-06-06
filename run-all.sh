#!/bin/sh

echo "Benchmark starting..."

export ALLOCATION_JAR=`find ~/.m2/repository -name java-allocation-instrumenter-2.0.jar`

mvn clean install
cd java/benchmark
mvn -o exec:exec
cd ../../fractal/benchmark
mvn -o exec:exec
cd ../../sca/benchmark
mvn -o exec:exec
cd ../..

echo "Benchmark completed"