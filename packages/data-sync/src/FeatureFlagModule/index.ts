import { FeatureFlagKey } from './FeatureFlag.types'
import FeatureFlagModule from './FeatureFlagModule';


export const checkFeatureEnabled = (featureKey: FeatureFlagKey, defaultValue?: boolean): boolean => {
    return FeatureFlagModule.isFeatureEnabled(featureKey, defaultValue);
}

export const getAllFlags = (): Record<FeatureFlagKey, boolean> => {
    return FeatureFlagModule.getAllFlags()
}
