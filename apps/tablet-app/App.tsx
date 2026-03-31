import { StyleSheet, Text, View } from 'react-native';

import { StatusBar } from 'expo-status-bar';

import {
  NATIVE_PI,
  getBatteryLevel,
  checkIsConnected,
  subscribeToNetworkChanges,
  getAllFlags,
  checkFeatureEnabled
} from '../../packages/data-sync';
import { useEffect, useState } from 'react';



export default function App() {
  const [statusNetwork, setStatusNetwork] = useState<string>('');
  const [typeNetwork, setTypeNetwork] = useState<string>('');
  const [isFeatureEnabled, setIsFeatureEnabled] = useState<boolean>(false);

  useEffect(() => {

    subscribeToNetworkChanges((event) => {
      setStatusNetwork(event.isConnected ? 'connected' : 'disconnected')
      setTypeNetwork(event.type)
    });

    setIsFeatureEnabled(checkFeatureEnabled('enable_offline_sync'))
  }, []);

  return (
    <View style={styles.container}>
      <Text>The Native PI value: {NATIVE_PI}</Text>
      <Text>The BatteryLevel: {getBatteryLevel()}</Text>
      <Text>Check is Connect: {checkIsConnected() ? 'True' : 'False'}</Text>
      <Text>The status network: {statusNetwork}</Text>
      <Text>The type netwotk: {typeNetwork}</Text>
      <Text>Is Feature Enabled: {isFeatureEnabled ? 'true' : 'false'}</Text>
      <StatusBar style='auto' />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center'
  }
});
