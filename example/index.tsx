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
    const msg =`COMPANY NAME
Folio: 1234-674
Date: 2022-02-04, 16:15:00
Name: JUAN PEREZ
\nPayment to Loan\n
Amortization No.: 24
Equity: $150.00
Interest: $35.00
Moratorium: $0.00
VAT: $20.00
Total Due: $205.00
\n ___________ ____________
     Promoter     Client
\n        Attended:
         DIEGO TORRES
   THIS PROOF WILL NOT BE VALID
WITHOUT SIGNATURE OF THE REPRESENTATIVE
P. SHERMAN, 42 WALLABY STREET, SYDNEY
  
       SPECIALIZED UNIT:
   SUGGESTIONS@EXAMPLE.COM
           PHONES:
987654321 AND 987654321 EXT 608A
          CUSTOMER\n\n`;

    RNXprinter.pushText(msg, 0);
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
