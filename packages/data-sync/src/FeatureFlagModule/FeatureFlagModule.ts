import { NativeModule, requireNativeModule } from 'expo-modules-core';

import { FeatureFlagKey } from './FeatureFlag.types';

declare class FeatureFlagModule extends NativeModule {
  syncFlags(): Promise<void>;
  isFeatureEnabled(featureKey: FeatureFlagKey, defaultValue?: boolean): boolean;
  getAllFlags(): Record<FeatureFlagKey, boolean>;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<FeatureFlagModule>('NativeFeatureFlagModule');
