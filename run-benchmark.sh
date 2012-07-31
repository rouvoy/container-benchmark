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

# OSGi Benchmark
cd ../../osgi/benchmark-osgi
rm -r felix-cache
mvn -o exec:exec

# iPOJO Benchmark
cd ../../osgi/benchmark-ipojo
rm -r felix-cache
# As Felix does not allow the mvn protocol to retrieve our ipojo-instrumented bundles, I'm doing it manually for the moment
if test "${M2_REPO+x}" != x; 
then 
        echo "$M2_REPO is not set - this benchmark may not work correctly"
fi
cp $M2_REPO/org/cbse/container/benchmark/iPOJOFibonacciDelegate/1.0-SNAPSHOT/iPOJOFibonacciDelegate-1.0-SNAPSHOT.jar ../bundles
cp $M2_REPO/org/cbse/container/benchmark/iPOJOFibonacciIterative/1.0-SNAPSHOT/iPOJOFibonacciIterative-1.0-SNAPSHOT.jar ../bundles
cp $M2_REPO/org/cbse/container/benchmark/iPOJOFibonacciSimple/1.0-SNAPSHOT/iPOJOFibonacciSimple-1.0-SNAPSHOT.jar ../bundles
cp $M2_REPO/org/cbse/container/benchmark/iPOJOFibonacciRecursive/1.0-SNAPSHOT/iPOJOFibonacciRecursive-1.0-SNAPSHOT.jar ../bundles
mvn -o exec:exec
cd ../..
echo "Benchmark completed"
