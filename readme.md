# Geffe Generator

## about
Implementation of Geffe Generator using functional style Scala and lazy collections. 
Application provides command line and graphical interfaces.

Features:
* generation of geffe stream
* implementation of statistical tests: long series, poker and monobit
* two types of LFSR: xor and custom Fibonacci
* random registers generation

## build
```
sbt
sbt:geffe> assembly
```
Generated jar will be available at `target/scala-2.13/geffe.jar`

## cli
> CLI was developed only for development purposes and has very limited features

### generate stream
Type: `java -jar geffe.jar stream <NUMBER_BITS_TO_GENERATE> <FIRST_LFSR_TYPE> <FIRST_LFSR_SIZE> <SECOND_LFSR_TYPE> <SECOND_LFSR_SIZE> <THIRD_LFSR_TYPE> <THIRD_LFSR_SIZE>`
CLI uses random registers of given size and type

Example: `java -jar geffe.jar stream 100 xor 25 xor 25 fib 25`

### run FIPS tests
Type: `java -jar geffe.jar test <FILE_WITH_STREAM_TO_TEST>`
Stream file should consist of `1`s and `0`s. The application will run 4 FIPS tests.

Example: `java -jar geffe.jar test stream.txt`

## algorithm
### how it works?
Geffe Generator uses three LFSRs connected non-linearly by multiplexer. General equation for this stream is:
```
k = (a3 AND a1) OR ((NOT a1) AND a2)
```   

### example
Using three 4-bit XOR registers:
* first with coefficients 0110 and initial state 1010
* second with coefficients 1101 and initial state 1111
* third with coefficients 1111 and initial state 0101

Generating 3-bit stream:
```
First bit:
    LFSR1:
        output: 1
        next state: 0101
    LFSR2:
        output: 1
        next state: 1111
    LFSR3:
        output: 0
        next state: 1010
    
    Bit: 0

Second bit:
    LFSR1:
        output: 1
        next state: 1011
    LFSR2:
        output: 1
        next state: 1111
    LFSR3:
        output: 1
        next state: 0101
    
    Bit: 1

Third bit:
    LFSR1:
        output: 1
        next state: 0111
    LFSR2:
        output: 1
        next state: 1111
    LFSR3:
        output: 0
        next state: 1010
    
    Bit: 0


Result: 010
```

## credits
This is a project for Data Protection Basics course at Poznan University of Technology.
