# BroadleafCommerce - DemoSite

This fork is used to explore the perturbability of [BroadleafCommerce](https://github.com/BroadleafCommerce/BroadleafCommerce) through the [Demosite](https://github.com/BroadleafCommerce/DemoSite) produces by the same team.

## What it is?

The perturbability of Software is a new field of research. It aims at establish how Software behave under perturbation, _i.e._ little change in their state during execution.

For more information, you can find the following paper: [Correctness Attraction: A Study of Stability of Software Behavior Under Runtime Perturbation ](https://hal.archives-ouvertes.fr/hal-01378523v2).

You can also, find a first online demo [here](https://danglotb.github.io/resources/correctness-attraction/live-demo.html).

## What does this project?

It starts a Demosite, proposed by Broadleaf on an instrumented implementation of it. Then it starts `monkeys`, that will execute random requests on it. At each request, a (executed) random perturbation point is enabled, _i.e._ a perturbation will occur on a specific expression. That is to say, that the value of this expression will be modify during the runtime. For instance, the value of `16 + 5 (= 22)` will be `=23`, _i.e._ `16 + 5 + 1`, where the `+ 1` is the perturbation.

  It will measure the number of _correct_ execution by checking the code status of the respond.

## What do you will see?

## How to explore the perturbability envelop?

**WARNING** this application required JavaFX

1. clone and install the instrumented branch of [BroadleafCommerce](https://github.com/danglotb/BroadleafCommerce)
```sh
git clone https://github.com/danglotb/BroadleafCommerce
cd BroadleafCommerce
mvn install -DskipTests
```

2. clone and install the Demosite (https://github.com/danglotb/DemoSite)
```sh
git clone https://github.com/danglotb/Demosite
cd Demosite
mvn package install -DskipTests
```

This fork has been enhanced with a specific module: `monkey`.
This module aims at creating monkey that will simulate clients (using Selenium).

3. run the `site` module
```sh
cd site
mvn spring-boot:run
```

4. run the `monkey` module
```sh
java -jar monkey/monkey-1.0.0-SNAPSHOT.jar 3
```
The arguments 3, is the number of parallel monkeys that will run (by default: 3). If you materials has trouble, reduce this number.