/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React from 'react';
import { Button, Text, View } from 'react-native';
import RNXprinter from 'react-native-esc-pos-xprinter';
const App = () => {
  const onConnectPrinter = async () => {
    // Select a printer to use
    const printerList = await RNXprinter.getDeviceList();
    const status = await RNXprinter.connectPrinter(printerList[0].address);
    console.log('Connect printer :', status);
  };

  const onPrinter = async () => {
    // print a text
    RNXprinter.pushText('Hello World!!', 0);
  };

  const onDisconnectPrinter = async () => {
    // Disconnect printer
    const status = await RNXprinter.disconnectPrinter();
    console.log('Disconnect printer :', status);
  };

  return (
    <View>
      <Text style={{ textAlign: 'center' }}>Example App</Text>
      <Button title="Connect Printer" onPress={onConnectPrinter} />
      <View>
        <Text> </Text>
      </View>
      <Button title="Printer" onPress={onPrinter} />
      <View>
        <Text> </Text>
      </View>
      <Button title="Disconnect Printer" onPress={onDisconnectPrinter} />
    </View>
  );
};

export default App;
