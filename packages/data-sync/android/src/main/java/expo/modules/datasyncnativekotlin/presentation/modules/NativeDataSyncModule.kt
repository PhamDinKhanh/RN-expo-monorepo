package expo.modules.datasyncnativekotlin.presentation.modules

import expo.modules.datasyncnativekotlin.di.KoinInitializer
import expo.modules.datasyncnativekotlin.presentation.facades.PokemonFacade
import expo.modules.kotlin.functions.Coroutine
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NativeDataSyncModule : Module(), KoinComponent {
    private val pokemonFacade: PokemonFacade by inject()

    override fun definition() = ModuleDefinition {
        // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
        // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
        // The module will be accessible from `requireNativeModule('DataSync')` in JavaScript.
        Name("NativeDataSyncModule")

        OnCreate {
            KoinInitializer.start(appContext.reactContext!!)
        }

        // AsyncFunction
        AsyncFunction("fetchPokemons") Coroutine { limit: Int ->
            return@Coroutine pokemonFacade.fetchPokemonForJS(limit)
        }
    }
}