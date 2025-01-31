/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.json;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.staticResources.ContributingStaticResources;
import io.micronaut.starter.feature.staticResources.StaticResource;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

@Singleton
public class JsonSchemaFeature implements ContributingStaticResources {

    public static final String NAME = "json-schema";

    private static final Dependency JSON_SCHEMA_PROCESSOR_DEPENDENCY = MicronautDependencyUtils.jsonSchemaDependency()
            .artifactId("micronaut-json-schema-processor")
            .annotationProcessor()
            .build();

    private static final Dependency JSON_SCHEMA_ANNOTAIONS_DEPENDENCY = MicronautDependencyUtils.jsonSchemaDependency()
            .artifactId("micronaut-json-schema-annotations")
            .compile()
            .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "JSON Schema";
    }

    @Override
    public String getDescription() {
        return "Adds JSON Schema to a Micronaut Application";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.API;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(JSON_SCHEMA_PROCESSOR_DEPENDENCY);
        generatorContext.addDependency(JSON_SCHEMA_ANNOTAIONS_DEPENDENCY);
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://json-schema.org/learn/getting-started-step-by-step";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-json-schema/latest/guide/";
    }

    @Override
    public List<StaticResource> staticResources() {
        return Collections.singletonList(new StaticResource("jsonschema", "/schemas/**", "classpath:META-INF/schemas"));
    }
}
