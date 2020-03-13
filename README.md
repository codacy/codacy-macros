[![Codacy Badge](https://api.codacy.com/project/badge/Grade/027eb70aacb542ac836c88dbb4f10e78)](https://www.codacy.com/gh/codacy/codacy-macros?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=codacy/codacy-macros&amp;utm_campaign=Badge_Grade)
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

## What is Codacy?

[Codacy](https://www.codacy.com/) is an Automated Code Review Tool that monitors your technical debt, helps you improve your code quality, teaches best practices to your developers, and helps you save time in Code Reviews.

### Among Codacy’s features:

- Identify new Static Analysis issues
- Commit and Pull Request Analysis with GitHub, BitBucket/Stash, GitLab (and also direct git repositories)
- Auto-comments on Commits and Pull Requests
- Integrations with Slack, HipChat, Jira, YouTrack
- Track issues in Code Style, Security, Error Proneness, Performance, Unused Code and other categories

Codacy also helps keep track of Code Coverage, Code Duplication, and Code Complexity.

Codacy supports PHP, Python, Ruby, Java, JavaScript, and Scala, among others.

### Free for Open Source

Codacy is free for Open Source projects.
