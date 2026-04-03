import { requireNativeModule } from 'expo-modules-core';
import { PokemonPageResponse } from './DataSync.types';

declare class DataSyncModule {
  fetchPokemons(limit: number): Promise<PokemonPageResponse>;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<DataSyncModule>('NativeDataSyncModule');
