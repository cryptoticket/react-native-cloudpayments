package com.rncloudpayments;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import ru.cloudpayments.sdk.cp_card.CPCard;
import ru.cloudpayments.sdk.three_ds.ThreeDsDialogFragment;

public class CloudPayments extends ReactContextBaseJavaModule {
  public CloudPayments(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "RNCloudPayments";
  }

  @ReactMethod
  public void isValidNumber(String cardNumber, String cardExp, String cardCvv, Promise promise) {
    try {
      CPCard card = new CPCard(cardNumber, cardExp, cardCvv);

      boolean numberStatus = card.isValidNumber();

      promise.resolve(numberStatus);
    } catch (Exception e) {
      promise.reject(e.getMessage());
    }
  }

  @ReactMethod
  public void getType(String cardNumber, String cardExp, String cardCvv, Promise promise) {
    try {
      CPCard card = new CPCard(cardNumber, cardExp, cardCvv);

      String cardType = card.getType();

      promise.resolve(cardType);
    } catch (Exception e) {
      promise.reject(e.getMessage());
    }
  }

  @ReactMethod
  public void createCryptogram(String cardNumber, String cardExp, String cardCvv, String publicId, Promise promise) {
    try {
      CPCard card = new CPCard(cardNumber, cardExp, cardCvv);

      String cryptoprogram = card.cardCryptogram(publicId);

      promise.resolve(cryptoprogram);
    } catch (Exception e) {
      e.printStackTrace();
      promise.reject(e.getMessage());
    }
  }

  @ReactMethod
  public void show3DSForm(String acsUrl, String transactionId, String paReq, Promise promise) {
    try {
      ThreeDsDialogFragment.newInstance(acsUrl, transactionId, paReq).show(getCurrentActivity().getFragmentManager(), "3DS");
      promise.resolve(null);
    } catch (Exception e) {
      e.printStackTrace();
      promise.reject(e.getMessage());
    }
  }
}