//Define types/interfaces for data exchanged between JavaScript and native code.
export type NetworkInfo = {
  isConnected: boolean;
  isValidated: boolean;
  type: 'WIFI' | 'CELLULAR' | 'UNKNOWN';
};

export type NetworkModuleEvents = {
  networkChanged: (params: NetworkInfo) => void;
};
