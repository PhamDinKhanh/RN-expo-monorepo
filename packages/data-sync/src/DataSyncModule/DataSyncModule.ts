import { requireNativeModule } from 'expo-modules-core';

declare class DataSyncModule {
  fetchPokemons(limit: number): Promise<string>;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<DataSyncModule>('NativeDataSyncModule');
