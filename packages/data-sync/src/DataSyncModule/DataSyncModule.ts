import { requireNativeModule } from 'expo-modules-core';

declare class DataSyncModule {
  PI: number;
  hello(): string;
  setValueAsync(value: string): Promise<void>;
  getBatteryLevel(): number;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<DataSyncModule>('NativeDataSyncModule');
