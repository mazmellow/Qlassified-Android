See an original [README](https://github.com/Q42/Qlassified-Android)

EncryptedPreferences class is a helper for created encrypted shared preferences. By using Qlassified library only, It can't save data that has length more than 46. So I create AES key then save in android KeyStore system by using this Qlassified library. Then using this AES key to encrypt-decrypt data and save in shared preferences instead.


## Getting started

Please clone this project then import qlassified module.

## Basic usage

Don't forget to initial EncryptedPreferences class. Also you can set enable log.

```java
Logger.setLogEnable(true);
EncryptedPreferrences.init(this, PreferenceManager.getDefaultSharedPreferences(this));

// Save a key/value pair encrypted
EncryptedPreferrences.putString("YOUR_KEY", "YOUR_VALUE");

// Get (and decrypt) a value by its key
String value = EncryptedPreferrences.getString("YOUR_KEY");
```

## Data types

Available data types at the moment:

* Integer
* Float
* Double
* Long
* Boolean
* String