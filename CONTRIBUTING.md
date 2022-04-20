Contributer Guidelines
===============================

## Code

### General Guidelines

* Stick to Use Google's Java Style Guide, [as documented here](https://google.github.io/styleguide/javaguide.html). You can download the stylesheet [in IntelliJ's style format](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml) to automate styling. 
* Avoid introducing any new libraries or dependencies. The code base aims to not employ any libraries outside of those bundled with Java.
 
### Before starting development

* If you want to help out with an existing bug report or feature request, **leave a comment** on that issue.
* If you would like to work on an issue that is not documented, feel free to open an issue and request to be assigned to it. You can also start working on a pull request without a corresponding issue if you are confident it will be accepted.
* Create PRs that cover only **one specific issue/solution/bug**. Do not create PRs that are huge monoliths and could have been split into multiple independent contributions.

### Creating a Pull Request (PR)

* Make changes on a **separate branch** with a meaningful name, not on the _master_ branch. This is commonly known as *feature branch workflow*. You may then send your changes as a pull request (PR) on GitHub.
* Please test your code by executing the program, *both inside **AND** outside a JAR*.
* Try to use meaningful and specific commit messages. Commit messages like "Fixed Bugs" or "Improved performance". If possible, include the name of the class(es) you modified in the commit.
* Respond if changes are requested or issues are raised with your PR.
* Try to figure out yourself why the Java CI workflow fails to build your PR. You may request help if you are stumped.
* Keep your branch up-to-date with the latest master. This can often be done with a single press of "Sync Branch" in Github, but you may need to resolve merge conflicts. PRs with merge conflicts will not be accepted until the conflict is resoleved.

## IDE setup & building the application

See [this guide in the project wiki](https://github.com/KosOrKosm/Fourier_Series_Applet/wiki/Setting-up-for-Development) for assistance configuring your development environment.<br>
A simple Ant build script is included with the project to quickly assemble the code into a JAR for testing. This script will be used to build the app during testing, so make sure it works for you!

## Communication

* Github Issues and PRs are the preferred way to communicate.
* For those in class, send a message on Discord to the user who posted the link to the repository.