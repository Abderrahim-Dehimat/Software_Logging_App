package com.example.softwareloggingapp.spoon;
import spoon.Launcher;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
public class LogInjector {
    public static void main(String[] args) {
        // Initialize Spoon Launcher
        Launcher launcher = new Launcher();
        launcher.addInputResource("src/main/java");// Path to source code

        launcher.setSourceOutputDirectory("src/main/generated");// Save modified code here

        launcher.getEnvironment().setAutoImports(true);
        // Build the model
        launcher.buildModel();
        // Iterate over all classes
        for (CtType<?> clazz : launcher.getFactory().Class().getAll()) {
            for (CtMethod<?> method : clazz.getMethods()) {
                // Ensure the method signature is not invalid
                if ((!method.isAbstract()) && (method.getBody() != null)) {
                    // Inject logging for READ operations
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
        // Save the modified code
        launcher.prettyprint();
    }

    private static void injectLogStatement(Launcher launcher, CtMethod<?> method, String logMessage) {
        // Add logging at the start of the method body
        CtStatement logStatement = launcher.getFactory().createCodeSnippetStatement(("log.info(\"" + logMessage) + ": \" + authenticatedUserEmail)");
        // Ensure the method body is not null before inserting
        if (method.getBody() != null) {
            method.getBody().insertBegin(logStatement);
        }
    }
}