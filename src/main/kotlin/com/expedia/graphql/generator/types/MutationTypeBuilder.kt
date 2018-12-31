package com.expedia.graphql.generator.types

import com.expedia.graphql.TopLevelObject
import com.expedia.graphql.generator.SchemaGenerator
import com.expedia.graphql.generator.TypeBuilder
import com.expedia.graphql.generator.extensions.getValidFunctions
import graphql.schema.GraphQLObjectType

internal class MutationTypeBuilder(generator: SchemaGenerator) : TypeBuilder(generator) {

    fun getMutationObject(mutations: List<TopLevelObject>): GraphQLObjectType? {

        if (mutations.isEmpty()) {
            return null
        }

        val mutationBuilder = GraphQLObjectType.Builder()
        mutationBuilder.name(config.topLevelMutationName)

        for (mutation in mutations) {
            mutation.kClass.getValidFunctions(config.hooks)
                .forEach {
                    val function = generator.function(it, mutation.obj)
                    val functionFromHook = config.hooks.didGenerateMutationType(it, function)
                    mutationBuilder.field(functionFromHook)
                }
        }

        return mutationBuilder.build()
    }
}
