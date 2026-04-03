import { StatusBar } from 'expo-status-bar';
import { useCallback, useEffect, useState } from 'react';
import { StyleSheet, Text, View } from 'react-native';


import {
  fetchPokemonsFromAPI,
  checkIsConnected,
  subscribeToNetworkChanges,
  checkFeatureEnabled,
  startReader,
  stopReader
} from '../../packages/data-sync';




export default function App() {
  const [statusNetwork, setStatusNetwork] = useState<string>('');
  const [typeNetwork, setTypeNetwork] = useState<string>('');
  const [isFeatureEnabled, setIsFeatureEnabled] = useState<boolean>(false);
  const [isScanning, setIsScanning] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string>("");
  const [pokemonName, setPokemonname] = useState<string>('');
  const [detailUrl, setDetailUrl] = useState<string>('');

  const callPokemonAPI = useCallback(async () => {
    const response = await fetchPokemonsFromAPI(10);
    const fristResponse = response.results[0]
    setPokemonname(fristResponse.name);
    setDetailUrl(fristResponse.detailUrl)
  }, []);

  const handleStartNfc = async () => {
    try {
      setIsScanning(true);
      await startReader();
    } catch (error: any) {
      setIsScanning(false);
      switch (error.code) {
        case 'ERR_ACTIVITY_NOT_FOUND':
        case 'ActivityNotFound':
          setErrorMessage("Lỗi hệ thống: Vui lòng mở lại màn hình này.");
          break;
        case 'NfcNotSupported':
          setErrorMessage("Máy không có NFC. Vui lòng quét mã QR.");
          break;
        case 'NfcDisabled':
          setErrorMessage("Lỗi đọc thẻ: " + error.message);
          break;
        default:
          setErrorMessage("Không có lỗi")
      }
    }
  }

  const handleStopNfc = async () => {
    try {
      await stopReader();
      setIsScanning(false);
    } catch (error: any) {
      console.error("Lỗi khi tắt NFC:", error);
    }
  };

  useEffect(() => {
    const testNativeFunction = () => {
      callPokemonAPI();
      handleStartNfc()
      subscribeToNetworkChanges((event) => {
        setStatusNetwork(event.isConnected ? 'connected' : 'disconnected')
        setTypeNetwork(event.type)
      });
      setIsFeatureEnabled(checkFeatureEnabled('enable_offline_sync'))
      return () => {
        handleStopNfc();
      };
    }

    testNativeFunction();
  }, []);


  return (
    <View style={styles.container}>
      <Text>Pokemon name: {pokemonName}</Text>
      <Text>Pokemon urlDetail: {detailUrl}</Text>
      <Text>Check is Connect: {checkIsConnected() ? 'True' : 'False'}</Text>
      <Text>The status network: {statusNetwork}</Text>
      <Text>The type netwotk: {typeNetwork}</Text>
      <Text>Is Feature Enabled: {isFeatureEnabled ? 'true' : 'false'}</Text>
      <Text>Is Scanning: {isScanning ? 'true' : 'false'}</Text>
      <Text>error Message: {errorMessage}</Text>
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
