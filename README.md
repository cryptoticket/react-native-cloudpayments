# React Native CloudPayments

[React Native](http://facebook.github.io/react-native/) library for accepting payments with [CloudPayments](https://cloudpayments.ru) SDK

# Install
### Step 1:
Download package:
```shell
npm install --save react-native-cloudpayments
```

or

```shell
yarn add react-native-cloudpayments
```
### Step 2:
Link dependencies:
```shell
react-native link react-native-cloudpayments
```
### Step 3:
Add in your Podfile next line:
```shell
pod 'SDK-iOS', :git =>  "https://github.com/cloudpayments/SDK-iOS", :branch => "master"
pod 'RNCloudPayments', :path => '../node_modules/react-native-cloudpayments'
```
### Step 3:
Install Pods in ios directory:
```shell
cd ios && pod install
```

# Methods (android + ios)
### isValidCard()
Validate card.
Returns a `Promise` that resolve card status (`Boolean`).

__Arguments__
- `cardNumber` - `String` Number of payment card.
- `cardExp` - `String` Expire date of payment card.
- `cardCvv` - `String` CVV code of payment card.

__Examples__
```js
import RNCloudPayment from 'react-native-cloudpayments';

const demoCard = {
  number: '5105105105105100',
  extDate: '10/18',
  cvvCode: '123',
};

RNCloudPayment.isValidCard(demoCard.number, demoCard.extDate, demoCard.cvvCode)
  .then(cardStatus => {
    console.log(cardStatus); // true
  });
```

### getType()
Retrive card type.
Returns a `Promise` that resolve card type (`String`).

Card types:
- Unknown
- Visa
- MasterCard
- Maestro
- Mir
- JCB

__Arguments__
- `cardNumber` - `String` Number of payment card.
- `cardExp` - `String` Expire date of payment card.
- `cardCvv` - `String` CVV code of payment card.

__Examples__
```js
import RNCloudPayment from 'react-native-cloudpayments';

const demoCard = {
  number: '5105105105105100',
  extDate: '10/18',
  cvvCode: '123',
};

RNCloudPayment.getType(demoCard.number, demoCard.extDate, demoCard.cvvCode)
  .then(cardType => {
    console.log(getType); // MasterCard
  });
```

### createCryptogram()
Create cryptogram. Used in CloudPayment [API](https://cloudpayments.ru/Docs/Api#payWithCrypto).
Returns a `Promise` that resolve cryptogram (`String`).

__Arguments__
- `cardNumber` - `String` Number of payment card.
- `cardExp` - `String` Expire date of payment card.
- `cardCvv` - `String` CVV code of payment card.
- `publicId` - `String` Your Public ID, you need to get it in your [personal account](https://merchant.cloudpayments.ru/).

__Examples__
```js
import RNCloudPayment from 'react-native-cloudpayments';

const demoCard = {
  number: '5105105105105100',
  extDate: '10/18',
  cvvCode: '123',
};

const publicId = 'pk_xxxxxxxxxxxxxxxxxxxxxxxxxxxxx';

RNCloudPayment.createCryptogram(demoCard.number, demoCard.extDate, demoCard.cvvCode, publicId)
  .then(cryptogram => {
    console.log(cryptogram); // 025105105100/11004bpp9ltxt6c0jpdk8ErH+N33N9jZBm9Gr0jO7SVslLg/RdWYyjG5wiLrzmrUserhfblFVydij4wpjDvHH4kRnOskjnbn1XrPI8X9LMkvlR5Pkc63U5puXtnS0rkswS6JYaSErcKMq4TazimKY4rGobvhhYfg45LWdLlX0602t7ZybbaBTMff6wtta870/244s65GTbCI1zt6odDMckpEuiczwM68m6j0Rn2IuKpK8kR58x7tFFc7fWrrW0RHvLNxQIW9P+SpsySoiA4xaZfC7lXL57O80Ye6JDi6PWAim5dENNxIc81T1kmXnKn94x8h2+XS83yMHHfTUOeDb7J1fLg==
  });
```

# Methods (ios)

### convertPayment()
Converts PKPayment object to cryptogram. Used to create a cryptogram on payment via Apple Pay. [Example flow](https://developers.cloudpayments.ru/#primer-provedeniya-platezha).
Returns a `Promise` that resolve cryptogram (`String`).

__Arguments__
- `payment` - `PKPayment` Payment response from Apple Pay.

__Examples__
```js
import RNCloudPayment from 'react-native-cloudpayments';

const METHOD_DATA = [
	{
		supportedMethods: ['apple-pay'],
		data: {
			merchantIdentifier: 'merchant.my.app',
			supportedNetworks,
			countryCode,
			currencyCode,
		},
	},
];

const details = {
	id: PAYMENT_ID,
	displayItems: [
		{
			label: title,
			amount: {
				currency, 
				value: amountValue
			},
		},
	],
	total: {
		label: MERCHANT_NAME,
		amount: {currency, value: amountValue},
	},
};

// create Apple Pay payment request
const paymentRequest = new PaymentRequest(METHOD_DATA, details);

paymentRequest
      .show()
      .then((paymentResponse) => {
	  	const cryptogram = RNCloudPayment.convertPayment(paymentResponse);
		console.log(cryptogram); // 013515...Db7J1fLg==
	  });
```

# Methods (android)

### show3DSForm()
Opens dialog window where user should complete 3DS auth.
Returns a `Promise` that resolves to `null`.

__Arguments__
- `acsUrl` - `String` Access URL.
- `transactionId` - `String` Transaction ID.
- `paReq` - `String` PA request data.

__Examples__
```js
import RNCloudPayment from 'react-native-cloudpayments';

const paymentResponse = cloudpaymentAPI.make3DSRequest();

console.log(paymentResponse);
/**
*{
    "Model": {
        "TransactionId": 504,
        "PaReq": "eJxVUdtugkAQ/RXDe9mLgo0Z1nhpU9PQasWmPhLYAKksuEChfn13uVR9mGTO7MzZM2dg3qSn0Q+X\nRZIJxyAmNkZcBFmYiMgxDt7zw6MxZ+DFkvP1ngeV5AxcXhR+xEdJ6BhpEZnEYLBdfPAzg56JKSKT\nAhqgGpFB7IuSgR+cl5s3NqFTG2NAPYSUy82aETqeWPYUUAdB+ClnwSmrwtz/TbkoC0BtDYKsEqX8\nZfZkDGgAUMkTi8synyFU17V5N2nKCpBuAHRVs610VijCJgmZu17UXTxhFWP34l7evYPlegsHkO6A\n0C85o5hMsI3piNIZHc+IBaitg59qJYzgdrUOQK7/WNy+3FZAeSqV5cMqAwLe5JlQwpny8T8HdFW8\netFuBqUyahV+Hjf27vWCaSx22fe+KY6kXKZfJLK1x22TZkyUS8QiHaUGgDQN6s+H+tOq7O7kf8hd\nt30=",
        "AcsUrl": "https://test.paymentgate.ru/acs/auth/start.do"
    },
    "Success": false,
    "Message": null
}
*/

RNCloudPayment.show3DSForm(
	paymentResponse.Model.AcsUrl, 
	paymentResponse.Model.TransactionId, 
	paymentResponse.Model.PaReq
);
```

# How to get 3DS result in React Native (android)

### Step 1
Add Cloudpayments SDK to `android/app/build.gradle`:
```
implementation files('../../node_modules/react-native-cloudpayments/android/src/main/libs/cloudpayments-sdk-1.0.5.aar')
```

### Step 2
Implement `ThreeDSDialogListener` in `MainActivity`. Example:
```
import ru.cloudpayments.sdk.three_ds.ThreeDSDialogListener;

...

public class MainActivity extends NavigationActivity implements ThreeDSDialogListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.show(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAuthorizationCompleted(String md, String paRes) {
		// on successfull 3DS payment
        WritableMap params = Arguments.createMap();
        params.putString("status", "success");
		params.putString("md", md); // transaction id
		params.putString("paRes", paRes);
        sendEvent(((MainApplication)getApplication()).getReactNativeHost().getReactInstanceManager().getCurrentReactContext(), "Cloudpayments3DSResult", params);
    }

    @Override
    public void onAuthorizationFailed(String html) {
        // on failed 3DS payment
        WritableMap params = Arguments.createMap();
        params.putString("status", "failed");
		params.putString("html", html);
        sendEvent(((MainApplication)getApplication()).getReactNativeHost().getReactInstanceManager().getCurrentReactContext(), "Cloudpayments3DSResult", params);
    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
}
```

### Step 3
Listen for `Cloudpayments3DSResult` event in React Native. Example:
```
import {DeviceEventEmitter} from 'react-native';
const nativeEventListener = DeviceEventEmitter.addListener('Cloudpayments3DSResult',
    (e)=>{
        console.log("caught 3DS result");
        console.log(e);
		/**
		*{
			status: "success",
			md: "50000789",
			paRes: "asd2123...329hjs=="
		}
		*/
    });
```

# License
Licensed under the MIT License.
