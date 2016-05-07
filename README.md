[![Codacy Badge](https://api.codacy.com/project/badge/grade/027eb70aacb542ac836c88dbb4f10e78)](https://www.codacy.com/app/Codacy/codacy-macros)
[![Circle CI](https://circleci.com/gh/codacy/codacy-macros.svg?style=shield&circle-token=:circle-token)](https://circleci.com/gh/codacy/codacy-macros)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.codacy/codacy-macros_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.codacy/codacy-macros_2.11)

# codacy-macros
scala and play framework macros

### Setup
Add the following to your sbt configuration:

```scala
libraryDependencies += "com.codacy" %% "codacy-macros" % version
```
```scala
addCompilerPlugin(Dependencies.macroParadise cross CrossVersion.full)
```

### Usage
Start by adding the following import to your code
```scala
import com.codacy.macros._
````
and then:
```scala
@json case class Form(title: String, message: String)
```


