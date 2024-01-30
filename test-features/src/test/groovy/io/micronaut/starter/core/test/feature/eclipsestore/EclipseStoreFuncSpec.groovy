package io.micronaut.starter.core.test.feature.eclipsestore

import io.micronaut.starter.feature.eclipsestore.EclipseStore
import io.micronaut.starter.feature.validator.MicronautValidationFeature
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.template.RockerWritable
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf
import java.util.stream.Stream;

class EclipseStoreFuncSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "eclipseStore"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven EclipseStore with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, [EclipseStore.NAME, MicronautValidationFeature.NAME])

        and:
        // Write a class that requires serialization
        def fsoh = new FileSystemOutputHandler(dir, ConsoleOutput.NOOP)
        fsoh.write(
                "src/main/${language.name}/example/micronaut/Book.${language.extension}",
                new RockerWritable(Class.forName("${this.class.package.name}.book${language.name}").template())
        )

        and:
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Stream.of(Language.values())
                .filter(it -> it == Language.GROOVY)
                .toList()
    }

    void "test #buildTool EclipseStore with #language"(BuildTool buildTool, Language language) {
        when:
        generateProject(language, buildTool, [EclipseStore.NAME, MicronautValidationFeature.NAME])

        and:
        // Write a class that requires serialization
        def fsoh = new FileSystemOutputHandler(dir, ConsoleOutput.NOOP)
        fsoh.write(
                "src/main/${language.name}/example/micronaut/Book.${language.extension}",
                new RockerWritable(Class.forName("${this.class.package.name}.book${language.name}").template())
        )

        and:
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        [language, buildTool] << LanguageBuildCombinations.gradleCombinations()
                .stream()
                .filter(l -> !(l[0] == Language.KOTLIN && l[1] == BuildTool.MAVEN) ) // Caused by: java.lang.NoSuchMethodError: Micronaut method io.micronaut.context.DefaultBeanContext.getProxyTargetBean(BeanResolutionContext,BeanDefinition,Argument,Qualifier) not found. Most likely reason for this issue is that you are running a newer version of Micronaut with code compiled against an older version. Please recompile the offending classe"
                .toList()
    }
}
