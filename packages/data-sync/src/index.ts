// Reexport the native module. On web, it will be resolved to DataSyncModule.web.ts
// and on native platforms to DataSyncModule.ts
import DataSyncModule from './DataSyncModule/DataSyncModule';

export function fetchPokemonsFromAPI(): Promise<string> {
  return DataSyncModule.fetchPokemons(10);
}

export { fetchNetworkInfo, subscribeToNetworkChanges, checkIsConnected } from './NetworkModule';

export { checkFeatureEnabled, getAllFlags } from './FeatureFlagModule';

export { startReader, stopReader } from './NfcModule';
