package com.example.softwareloggingapp.spoon;
import spoon.Launcher;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
/**
 * The LogInjector class is responsible for automatically injecting logging statements
 * into the methods of a project's source code based on specific operations (READ, WRITE, SEARCH).
 * This uses the Spoon library for static code analysis and modification.
 */
public class LogInjector {
    public static void main(String[] args) {
        // Initialize Spoon Launcher for processing Java source files
        Launcher launcher = new Launcher();
        launcher.addInputResource("src/main/java");// Path to the source code directory

        launcher.setSourceOutputDirectory("src/main/generated");// Directory for saving modified code

        launcher.getEnvironment().setAutoImports(true);// Enable automatic imports for the generated code

        // Build the abstract syntax tree (AST) model from the source code
        launcher.buildModel();
        // Iterate over all the classes in the project
        for (CtType<?> clazz : launcher.getFactory().Class().getAll()) {
            // Iterate over all methods in the class
            for (CtMethod<?> method : clazz.getMethods()) {
                // Ensure the method has a valid body (not abstract or empty)
                if ((!method.isAbstract()) && (method.getBody() != null)) {
                    // Inject logging for READ operations based on method name patterns
                    if (method.getSimpleName().toLowerCase().contains("read")) {
                        injectLogStatement(launcher, method, "READ operation performed by user");
                    } else if (((method.getSimpleName().toLowerCase().contains("create") || method.getSimpleName().toLowerCase().contains("add")) || method.getSimpleName().toLowerCase().contains("update")) || method.getSimpleName().toLowerCase().contains("delete")) {
                        injectLogStatement(launcher, method, "WRITE operation performed by user");
                    } else if ((method.getSimpleName().toLowerCase().contains("fetch") || method.getSimpleName().toLowerCase().contains("read")) || method.getSimpleName().toLowerCase().contains("display")) {
                        injectLogStatement(launcher, method, "SEARCH operation performed by user");
                    }
                }
            }
        }
        // Save the modified code to the output directory
        launcher.prettyprint();
    }

    /**
     * Injects a logging statement at the beginning of the specified method.
     *
     * @param launcher
     * 		The Spoon Launcher instance.
     * @param method
     * 		The method into which the logging statement is to be injected.
     * @param logMessage
     * 		The log message to inject.
     */
    private static void injectLogStatement(Launcher launcher, CtMethod<?> method, String logMessage) {
        // Create the logging statement
        CtStatement logStatement = launcher.getFactory().createCodeSnippetStatement(("log.info(\"" + logMessage) + ": \" + authenticatedUserEmail)");
        // Insert the logging statement at the beginning of the method body
        if (method.getBody() != null) {
            // Ensure the method body is not null
            method.getBody().insertBegin(logStatement);
        }
    }
}