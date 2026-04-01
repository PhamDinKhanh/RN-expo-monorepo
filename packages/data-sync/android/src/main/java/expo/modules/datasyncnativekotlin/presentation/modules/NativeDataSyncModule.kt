package expo.modules.datasyncnativekotlin.presentation.modules

import expo.modules.datasyncnativekotlin.presentation.facades.PokemonFacade
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NativeDataSyncModule : Module() {
    private val moduleScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val pokemonFacade = PokemonFacade()

    override fun definition() = ModuleDefinition {
        // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
        // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
        // The module will be accessible from `requireNativeModule('DataSync')` in JavaScript.
        Name("NativeDataSyncModule")

        // AsyncFunction hỗ trợ sẵn suspend function của Coroutines
        // JS sẽ nhận được một Promise
        AsyncFunction("fetchPokemons") { limit: Int ->
            // Tạo một Coroutine chạy trên luồng nền (IO)
            moduleScope.launch {
                try {
                    pokemonFacade.fetchPokemonsForJS(limit)
                } catch (e: Exception) {
                    // Bắn lỗi về khối catch {} bên JavaScript
                }
            }
        }

    }
}