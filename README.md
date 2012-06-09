# Container Benchmark

Project focusing on the definition of a common benchmark for evaluating the performances of various application containers.

It uses the [Caliper](http://caliper.googlecode.com) micro-benchmarking framework for writing, running and displaying the benchmark datasets.


## Supported Technologies

The current implementation supports the following technologies:

* Java (including various proxy/interception technics)

* [Fractal component](http://fractal.ow2.org) model: [Julia](http://fractal.ow2.org/julia)

* [Service Component Architecture (SCA)](http://en.wikipedia.org/wiki/Service_Component_Architecture) specifications: [FraSCAti](http://frascati.ow2.org), [Tuscany](http://tuscany.apache.org)


## Provided Applications

Current benchmarks are based on the following application:

* [Fibonacci number](http://en.wikipedia.org/wiki/Fibonacci_number)


## Configuration (optional)

Go to http://microbenchmarks.appspot.com to get an API key.
Paste this in your `~/.caliperrc` configuration file.
With this in place, Caliper will upload the results for each benchmark you run.


## Usage

Clone the repository and execute `run-benchmark.sh`


## License

Copyright (C) 2012 Inria, University Lille 1

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Library General Public
License as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Library General Public License for more details.

You should have received a copy of the GNU Library General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
Boston, MA  02110-1301, USA.