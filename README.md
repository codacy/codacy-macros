default behavior:

@json case class Foo(param1:String)
@json case class Foo(param1:String, param2:Int)
@json case class Foo(param1:String) extends AnyVal

valid modes:
@json("value") case class Foo(param1:String)
@json("strict") case class Foo(param1:String) extends AnyVal


//does not compile:
@json("value") case class Foo(param1:String, param2:Int)

//will compile but is superfluous:
@json("strict") case class Foo(param1:String)
@json("strict") case class Foo(param1:String, param2:Int)
@json("value") case class Foo(param1:String) extends AnyVal



### Setup
add the following as a dependency to your sbt configuration:

```scala
libraryDependencies += "com.codacy" %% "play-dropwizard" % metricVersion
```

### Usage
start by adding the following import to your code
```scala
import codacy.metrics.dropwizard._
````
and measure some code. timing for example you can do by:
```scala
val name = TimerName(myTimer)
timed(name){ block }
```


