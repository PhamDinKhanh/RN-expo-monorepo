// Reexport the native module. On web, it will be resolved to DataSyncModule.web.ts
// and on native platforms to DataSyncModule.ts
import { PokemonPageResponse } from './DataSyncModule/DataSync.types';
import DataSyncModule from './DataSyncModule/DataSyncModule';



export function fetchPokemonsFromAPI(limit: number): Promise<PokemonPageResponse> {
  return DataSyncModule.fetchPokemons(limit);
}

export { fetchNetworkInfo, subscribeToNetworkChanges, checkIsConnected } from './NetworkModule';

export { checkFeatureEnabled, getAllFlags } from './FeatureFlagModule';

export { startReader, stopReader } from './NfcModule';
